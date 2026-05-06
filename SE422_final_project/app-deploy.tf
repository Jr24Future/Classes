data "archive_file" "gallery_app" {
  type        = "zip"
  source_dir  = "${path.module}/app"
  output_path = "${path.module}/gallery-app.zip"
}

data "local_file" "gallery_app_zip" {
  filename   = data.archive_file.gallery_app.output_path
  depends_on = [data.archive_file.gallery_app]
}

locals {
  startup_script = templatefile("${path.module}/scripts/startup.sh.tpl", {
    app_zip_b64     = data.local_file.gallery_app_zip.content_base64
    init_sql_b64    = filebase64("${path.module}/scripts/init-db.sql")
    db_host         = google_sql_database_instance.mysql.private_ip_address
    db_port         = 3306
    db_name         = var.db_name
    db_user         = var.db_user
    db_password_b64 = base64encode(var.db_password)
    app_port        = var.app_port
    upload_max_mb   = 8
  })
}
