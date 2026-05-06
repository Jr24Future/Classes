resource "google_compute_instance" "gallery_vm" {
  name         = "${var.name_prefix}-vm"
  machine_type = var.machine_type
  zone         = var.zone
  tags         = ["gallery-web"]
  labels       = local.labels

  boot_disk {
    initialize_params {
      image = "projects/debian-cloud/global/images/family/debian-12"
      size  = 20
      type  = "pd-balanced"
    }
  }

  network_interface {
    subnetwork = google_compute_subnetwork.subnet.id

    access_config {
      # Ephemeral public IP for HTTP access.
    }
  }

  service_account {
    email  = google_service_account.vm_service_account.email
    scopes = ["https://www.googleapis.com/auth/cloud-platform"]
  }

  metadata_startup_script = local.startup_script

  depends_on = [
    google_sql_database.gallery,
    google_sql_user.gallery_user,
    google_project_iam_member.vm_logging,
    google_project_iam_member.vm_monitoring
  ]
}
