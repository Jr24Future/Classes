set -euo pipefail

exec > >(tee -a /var/log/gallery-startup.log | logger -t gallery-startup -s 2>/dev/console) 2>&1

echo "Starting Gallery application deployment..."

APP_DIR="/opt/gallery"
APP_USER="galleryapp"
APP_PORT="${app_port}"
DB_HOST="${db_host}"
DB_PORT="${db_port}"
DB_NAME="${db_name}"
DB_USER="${db_user}"
DB_PASSWORD="$(echo '${db_password_b64}' | base64 -d)"

apt-get update -y
DEBIAN_FRONTEND=noninteractive apt-get install -y \
  python3 \
  python3-venv \
  python3-pip \
  unzip \
  default-mysql-client \
  nginx \
  openssl

if ! id "$APP_USER" >/dev/null 2>&1; then
  useradd --system --home "$APP_DIR" --shell /usr/sbin/nologin "$APP_USER"
fi

rm -rf "$APP_DIR"
mkdir -p "$APP_DIR"

echo '${app_zip_b64}' | base64 -d > /tmp/gallery-app.zip
unzip -q /tmp/gallery-app.zip -d "$APP_DIR"

echo '${init_sql_b64}' | base64 -d > /tmp/init-db.sql

cat > "$APP_DIR/.env" <<ENV
DB_HOST=$DB_HOST
DB_PORT=$DB_PORT
DB_NAME=$DB_NAME
DB_USER=$DB_USER
DB_PASSWORD=$DB_PASSWORD
SECRET_KEY=$(openssl rand -hex 32)
UPLOAD_MAX_MB=${upload_max_mb}
PORT=$APP_PORT
ENV

python3 -m venv "$APP_DIR/venv"
"$APP_DIR/venv/bin/pip" install --upgrade pip
"$APP_DIR/venv/bin/pip" install -r "$APP_DIR/requirements.txt"

chown -R "$APP_USER:$APP_USER" "$APP_DIR"
chmod 640 "$APP_DIR/.env"

# Wait for Cloud SQL private IP to become reachable, then initialize schema.
echo "Waiting for Cloud SQL at $DB_HOST..."
for attempt in $(seq 1 40); do
  if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" -e "SELECT 1;" >/dev/null 2>&1; then
    echo "Cloud SQL is reachable."
    mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < /tmp/init-db.sql
    break
  fi

  if [ "$attempt" -eq 40 ]; then
    echo "Cloud SQL did not become reachable in time. App will still start and /health will show DB status."
  else
    echo "Cloud SQL not ready yet. Attempt $attempt/40..."
    sleep 15
  fi
done

cat > /etc/systemd/system/gallery.service <<SERVICE
[Unit]
Description=SE4220 Gallery Flask Application
After=network-online.target
Wants=network-online.target

[Service]
User=$APP_USER
Group=$APP_USER
WorkingDirectory=$APP_DIR
EnvironmentFile=$APP_DIR/.env
ExecStart=$APP_DIR/venv/bin/gunicorn --workers 2 --bind 127.0.0.1:$APP_PORT main:app
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
SERVICE

cat > /etc/nginx/sites-available/gallery <<NGINX
server {
    listen 80 default_server;
    server_name _;

    client_max_body_size ${upload_max_mb}M;

    location / {
        proxy_pass http://127.0.0.1:$APP_PORT;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
NGINX

rm -f /etc/nginx/sites-enabled/default
ln -sf /etc/nginx/sites-available/gallery /etc/nginx/sites-enabled/gallery
nginx -t

systemctl daemon-reload
systemctl enable gallery
systemctl restart gallery
systemctl enable nginx
systemctl restart nginx

echo "Gallery deployment finished. Check http://EXTERNAL_IP/health"
