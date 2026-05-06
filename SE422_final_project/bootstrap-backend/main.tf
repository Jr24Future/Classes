# Optional helper to create a Terraform state bucket.
# This is separate because the main Terraform backend bucket must exist before `terraform init`.

terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = ">= 4.0.0, < 7.0.0"
    }
    random = {
      source  = "hashicorp/random"
      version = ">= 3.5.0"
    }
  }
}

provider "google" {
  project = var.project_id
  region  = var.region
}

resource "random_id" "suffix" {
  byte_length = 4
}

resource "google_storage_bucket" "tf_state" {
  name                        = "${var.project_id}-se4220-tfstate-${random_id.suffix.hex}"
  location                    = "US"
  uniform_bucket_level_access = true
  public_access_prevention    = "enforced"
  force_destroy               = false

  versioning {
    enabled = true
  }
}

output "bucket_name" {
  value = google_storage_bucket.tf_state.name
}
