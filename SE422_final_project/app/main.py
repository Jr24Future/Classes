import os
from functools import wraps
from datetime import datetime

import pymysql
from pymysql.cursors import DictCursor
from flask import (
    Flask,
    Response,
    flash,
    redirect,
    render_template,
    request,
    send_file,
    session,
    url_for,
)
from werkzeug.security import check_password_hash, generate_password_hash
from werkzeug.utils import secure_filename
from io import BytesIO

app = Flask(__name__)
app.secret_key = os.environ.get("SECRET_KEY", "dev-secret-change-me")
app.config["MAX_CONTENT_LENGTH"] = int(os.environ.get("UPLOAD_MAX_MB", "8")) * 1024 * 1024

ALLOWED_EXTENSIONS = {"png", "jpg", "jpeg", "gif", "webp"}
_SCHEMA_READY = False

SCHEMA_SQL = """
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(80) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS photos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    image_data LONGBLOB NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_photos_user_title (user_id, title),
    CONSTRAINT fk_photos_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);
"""


def db_config():
    """Return PyMySQL connection settings for either private IP or App Engine Unix socket."""
    common = {
        "user": os.environ.get("DB_USER", "galleryuser"),
        "password": os.environ.get("DB_PASSWORD", "gallerypass"),
        "database": os.environ.get("DB_NAME", "gallerydb"),
        "cursorclass": DictCursor,
        "autocommit": True,
        "charset": "utf8mb4",
    }

    unix_socket = os.environ.get("DB_UNIX_SOCKET")
    if unix_socket:
        common["unix_socket"] = unix_socket
    else:
        common["host"] = os.environ.get("DB_HOST", "127.0.0.1")
        common["port"] = int(os.environ.get("DB_PORT", "3306"))

    return common


def get_db_connection():
    return pymysql.connect(**db_config())


def ensure_schema():
    global _SCHEMA_READY
    if _SCHEMA_READY:
        return
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            for statement in SCHEMA_SQL.split(";"):
                statement = statement.strip()
                if statement:
                    cursor.execute(statement)
    _SCHEMA_READY = True


@app.before_request
def before_every_request():
    if request.endpoint not in {"static"}:
        ensure_schema()


def current_user():
    user_id = session.get("user_id")
    if not user_id:
        return None
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute("SELECT id, username FROM users WHERE id = %s", (user_id,))
            return cursor.fetchone()


def login_required(view_func):
    @wraps(view_func)
    def wrapper(*args, **kwargs):
        if not session.get("user_id"):
            flash("Please log in first.", "warning")
            return redirect(url_for("login"))
        return view_func(*args, **kwargs)

    return wrapper


def allowed_file(filename):
    return "." in filename and filename.rsplit(".", 1)[1].lower() in ALLOWED_EXTENSIONS


@app.context_processor
def inject_user():
    return {"logged_in_user": current_user()}


@app.route("/health")
def health():
    try:
        with get_db_connection() as conn:
            with conn.cursor() as cursor:
                cursor.execute("SELECT 1 AS ok")
                row = cursor.fetchone()
        status = "ok" if row and row.get("ok") == 1 else "database_error"
        http_status = 200 if status == "ok" else 500
    except Exception as exc:  # shows useful info during class demo; avoid details in production
        return {"status": "error", "database": "not_connected", "message": str(exc)}, 500

    return {"status": status, "database": "connected", "time": datetime.utcnow().isoformat() + "Z"}, http_status


@app.route("/")
def index():
    if session.get("user_id"):
        return redirect(url_for("dashboard"))
    return render_template("index.html")


@app.route("/register", methods=["GET", "POST"])
def register():
    if request.method == "POST":
        username = request.form.get("username", "").strip()
        password = request.form.get("password", "")

        if len(username) < 3 or len(password) < 6:
            flash("Username must be at least 3 characters and password at least 6 characters.", "danger")
            return redirect(url_for("register"))

        password_hash = generate_password_hash(password)
        try:
            with get_db_connection() as conn:
                with conn.cursor() as cursor:
                    cursor.execute(
                        "INSERT INTO users (username, password_hash) VALUES (%s, %s)",
                        (username, password_hash),
                    )
            flash("Account created. Please log in.", "success")
            return redirect(url_for("login"))
        except pymysql.err.IntegrityError:
            flash("That username already exists.", "danger")

    return render_template("register.html")


@app.route("/login", methods=["GET", "POST"])
def login():
    if request.method == "POST":
        username = request.form.get("username", "").strip()
        password = request.form.get("password", "")

        with get_db_connection() as conn:
            with conn.cursor() as cursor:
                cursor.execute("SELECT * FROM users WHERE username = %s", (username,))
                user = cursor.fetchone()

        if user and check_password_hash(user["password_hash"], password):
            session["user_id"] = user["id"]
            flash("Logged in successfully.", "success")
            return redirect(url_for("dashboard"))

        flash("Invalid username or password.", "danger")

    return render_template("login.html")


@app.route("/logout")
def logout():
    session.clear()
    flash("You have been logged out.", "info")
    return redirect(url_for("index"))


@app.route("/dashboard")
@login_required
def dashboard():
    q = request.args.get("q", "").strip()
    user_id = session["user_id"]
    params = [user_id]
    query = """
        SELECT id, title, description, filename, content_type, uploaded_at
        FROM photos
        WHERE user_id = %s
    """

    if q:
        query += " AND (title LIKE %s OR description LIKE %s OR filename LIKE %s)"
        like_q = f"%{q}%"
        params.extend([like_q, like_q, like_q])

    query += " ORDER BY uploaded_at DESC"

    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute(query, params)
            photos = cursor.fetchall()

    return render_template("dashboard.html", photos=photos, q=q)


@app.route("/upload", methods=["POST"])
@login_required
def upload():
    title = request.form.get("title", "").strip()
    description = request.form.get("description", "").strip()
    file = request.files.get("photo")

    if not title:
        flash("Photo title is required.", "danger")
        return redirect(url_for("dashboard"))

    if not file or file.filename == "":
        flash("Please choose a photo to upload.", "danger")
        return redirect(url_for("dashboard"))

    if not allowed_file(file.filename):
        flash("Allowed file types: png, jpg, jpeg, gif, webp.", "danger")
        return redirect(url_for("dashboard"))

    filename = secure_filename(file.filename)
    image_bytes = file.read()

    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute(
                """
                INSERT INTO photos
                    (user_id, title, description, filename, content_type, image_data)
                VALUES
                    (%s, %s, %s, %s, %s, %s)
                """,
                (session["user_id"], title, description, filename, file.mimetype, image_bytes),
            )

    flash("Photo uploaded successfully.", "success")
    return redirect(url_for("dashboard"))


def fetch_photo_or_404(photo_id):
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute(
                "SELECT * FROM photos WHERE id = %s AND user_id = %s",
                (photo_id, session.get("user_id")),
            )
            photo = cursor.fetchone()
    return photo


@app.route("/photo/<int:photo_id>/view")
@login_required
def view_photo(photo_id):
    photo = fetch_photo_or_404(photo_id)
    if not photo:
        return Response("Photo not found", status=404)
    return Response(photo["image_data"], mimetype=photo["content_type"])


@app.route("/photo/<int:photo_id>/download")
@login_required
def download_photo(photo_id):
    photo = fetch_photo_or_404(photo_id)
    if not photo:
        return Response("Photo not found", status=404)

    return send_file(
        BytesIO(photo["image_data"]),
        mimetype=photo["content_type"],
        as_attachment=True,
        download_name=photo["filename"],
    )


@app.route("/photo/<int:photo_id>/delete", methods=["POST"])
@login_required
def delete_photo(photo_id):
    with get_db_connection() as conn:
        with conn.cursor() as cursor:
            cursor.execute(
                "DELETE FROM photos WHERE id = %s AND user_id = %s",
                (photo_id, session["user_id"]),
            )
    flash("Photo deleted.", "info")
    return redirect(url_for("dashboard"))


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=int(os.environ.get("PORT", "8080")), debug=True)
