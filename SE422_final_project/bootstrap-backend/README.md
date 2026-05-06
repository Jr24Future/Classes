# Optional backend bootstrap

Use this only if you want Terraform to create the GCS state bucket first.

```bash
cd bootstrap-backend
terraform init
terraform apply -var="project_id=YOUR_PROJECT_ID"
terraform output -raw bucket_name
```

Then copy the bucket name into the root `backend.tf` file and run Terraform from the root folder.
