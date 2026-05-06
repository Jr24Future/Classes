resource "google_service_account" "vm_service_account" {
  account_id   = "${var.name_prefix}-vm-sa"
  display_name = "SE4220 Gallery VM service account"

  depends_on = [google_project_service.required_apis]
}

resource "google_project_iam_member" "vm_logging" {
  project = var.project_id
  role    = "roles/logging.logWriter"
  member  = "serviceAccount:${google_service_account.vm_service_account.email}"
}

resource "google_project_iam_member" "vm_monitoring" {
  project = var.project_id
  role    = "roles/monitoring.metricWriter"
  member  = "serviceAccount:${google_service_account.vm_service_account.email}"
}
