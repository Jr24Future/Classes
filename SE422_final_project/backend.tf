# The GCS bucket must already exist before running `terraform init`.
# Edit bucket to a globally unique bucket name that you created for Terraform state.
terraform {
  backend "gcs" {
    bucket = "project-7836f0e1-aaf9-4834-a71-tfstate"
    prefix = "se4220-final/gallery"
  }
}
