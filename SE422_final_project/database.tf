resource "google_sql_database_instance" "mysql" {
  name             = "${var.name_prefix}-mysql"
  database_version = "MYSQL_8_0"
  region           = var.region

  deletion_protection = false

  settings {
    tier              = var.db_tier
    availability_type = "ZONAL"
    disk_type         = "PD_SSD"
    disk_size         = 20

    ip_configuration {
      ipv4_enabled    = false
      private_network = google_compute_network.vpc.id
    }

    backup_configuration {
      enabled = true
    }

    user_labels = local.labels
  }

  depends_on = [google_service_networking_connection.private_vpc_connection]
}

resource "google_sql_database" "gallery" {
  name     = var.db_name
  instance = google_sql_database_instance.mysql.name
}

resource "google_sql_user" "gallery_user" {
  name     = var.db_user
  instance = google_sql_database_instance.mysql.name
  password = var.db_password
}
