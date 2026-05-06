# SE 4220 Final Project — Terraform Gallery App on GCP

This repository contains a from-scratch Project 4-compatible Gallery application and Terraform infrastructure for the SE 4220 final project.

The Gallery app is a Flask + MySQL web app with:

- New user registration
- User login/logout
- Photo upload
- Search by title, description, or filename
- Photo view/download
- `/health` endpoint with database connectivity check

The Terraform configuration creates:

- GCS remote backend configuration for Terraform state
- Custom VPC and subnet `10.0.0.0/16`
- Firewall rules for HTTP/HTTPS and optional SSH
- Cloud SQL MySQL instance using private IP
- Compute Engine VM `e2-standard-2`
- VM service account with limited logging/monitoring roles
- Startup deployment script that installs dependencies, initializes the schema, starts the app, and enables restart on boot

## Architecture

```text
User Browser
    |
    | HTTP port 80
    v
Compute Engine VM: e2-standard-2
Nginx reverse proxy -> Gunicorn/Flask Gallery app
    |
    | Private IP MySQL connection
    v
Cloud SQL MySQL: db-n1-standard-1
```

## Prerequisites

Install and configure:

- Google Cloud SDK
- Terraform
- A GCP project with billing enabled
- Permission to create Compute Engine, Cloud SQL, VPC, IAM, and Cloud Storage resources

Authenticate locally:

```bash
gcloud auth login
gcloud auth application-default login
gcloud config set project YOUR_PROJECT_ID
```

Enable the Service Usage API if needed:

```bash
gcloud services enable serviceusage.googleapis.com cloudresourcemanager.googleapis.com
```

## Step 1 — Create the GCS backend bucket

Terraform's GCS backend requires the bucket to exist before `terraform init`.

Choose a globally unique bucket name:

```bash
export PROJECT_ID="YOUR_PROJECT_ID"
export TF_STATE_BUCKET="YOUR_UNIQUE_BUCKET_NAME-se4220-tfstate"

gsutil mb -p "$PROJECT_ID" -l US "gs://$TF_STATE_BUCKET"
gsutil versioning set on "gs://$TF_STATE_BUCKET"
```

Then edit `backend.tf`:

```hcl
terraform {
  backend "gcs" {
    bucket = "YOUR_UNIQUE_BUCKET_NAME-se4220-tfstate"
    prefix = "se4220-final/gallery"
  }
}
```

## Step 2 — Configure Terraform variables

Copy the example file:

```bash
cp terraform.tfvars.example terraform.tfvars
```

Edit `terraform.tfvars`:

```hcl
project_id  = "your-gcp-project-id"
region      = "us-central1"
zone        = "us-central1-a"
name_prefix = "se4220-gallery"
db_password = "UseAStrongPassword123!"
```

Do not commit `terraform.tfvars` because it contains a password.

## Step 3 — Run Terraform

```bash
terraform init
terraform validate
terraform plan
terraform apply
```

After apply finishes, Terraform prints:

- `application_url`
- `health_check_url`
- `vm_public_ip`
- `cloud_sql_private_ip`
- `cloud_sql_connection_name`

## Step 4 — Validate the app

Open the health URL:

```bash
curl $(terraform output -raw health_check_url)
```

Expected result:

```json
{"database":"connected","status":"ok"}
```

Then open the application URL in a browser:

```bash
terraform output -raw application_url
```

Test:

1. Register a new user.
2. Log in.
3. Upload at least 10 photos.
4. Search for a photo.
5. Download at least 2 photos.
6. Refresh the page and confirm photos still appear.

## Step 5 — Screenshots for validation report

Take screenshots of:

1. `terraform apply` success output
2. VM in GCP Compute Engine console
3. VPC/subnet/firewall rules
4. Cloud SQL instance with private IP
5. Gallery login screen
6. Uploaded photo gallery
7. Search result
8. Download test
9. `/health` endpoint showing database connected
10. Cost estimate or billing estimate

## Step 6 — Destroy resources after demo

To avoid extra cost:

```bash
terraform destroy
```

## Troubleshooting

### App does not load immediately

The VM startup script can take a few minutes after Terraform finishes. SSH into the VM or check serial logs, then run:

```bash
sudo tail -n 100 /var/log/gallery-startup.log
sudo systemctl status gallery
sudo systemctl status nginx
```

### Health endpoint says database is not connected

Check that Cloud SQL has a private IP and that the VM is in the same VPC. Also check the startup log:

```bash
sudo tail -n 100 /var/log/gallery-startup.log
```

### Terraform backend error

Make sure the bucket in `backend.tf` exists and that you authenticated with:

```bash
gcloud auth application-default login
```

## Notes

This project stores uploaded image bytes in Cloud SQL as `LONGBLOB` values. That keeps the app simple for class demonstration because the app does not need a separate Cloud Storage bucket for photos.
