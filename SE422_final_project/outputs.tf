output "application_url" {
  description = "Public URL for the Gallery application."
  value       = "http://${google_compute_instance.gallery_vm.network_interface[0].access_config[0].nat_ip}"
}

output "health_check_url" {
  description = "Health endpoint that checks the Flask app and database connection."
  value       = "http://${google_compute_instance.gallery_vm.network_interface[0].access_config[0].nat_ip}/health"
}

output "vm_public_ip" {
  description = "Compute Engine public IP."
  value       = google_compute_instance.gallery_vm.network_interface[0].access_config[0].nat_ip
}

output "cloud_sql_private_ip" {
  description = "Private IP address of Cloud SQL. Do not expose publicly."
  value       = google_sql_database_instance.mysql.private_ip_address
}

output "cloud_sql_connection_name" {
  description = "Cloud SQL connection name, useful if testing with Cloud SQL Auth Proxy or App Engine."
  value       = google_sql_database_instance.mysql.connection_name
}

output "database_name" {
  description = "MySQL database name."
  value       = google_sql_database.gallery.name
}

output "database_user" {
  description = "MySQL application username."
  value       = google_sql_user.gallery_user.name
}
