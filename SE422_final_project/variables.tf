variable "project_id" {
  description = "GCP project ID where resources will be created."
  type        = string

  validation {
    condition     = length(var.project_id) > 3
    error_message = "project_id must be a valid GCP project ID."
  }
}

variable "region" {
  description = "GCP region."
  type        = string
  default     = "us-central1"

  validation {
    condition     = can(regex("^[a-z]+-[a-z]+[0-9]$", var.region))
    error_message = "region must look like us-central1."
  }
}

variable "zone" {
  description = "GCP zone."
  type        = string
  default     = "us-central1-a"

  validation {
    condition     = can(regex("^[a-z]+-[a-z]+[0-9]-[a-z]$", var.zone))
    error_message = "zone must look like us-central1-a."
  }
}

variable "name_prefix" {
  description = "Prefix used for resource names."
  type        = string
  default     = "se4220-gallery"

  validation {
    condition     = can(regex("^[a-z][a-z0-9-]{2,24}$", var.name_prefix))
    error_message = "name_prefix must start with a lowercase letter and contain only lowercase letters, numbers, and hyphens."
  }
}

variable "subnet_cidr" {
  description = "Custom subnet CIDR required by the assignment."
  type        = string
  default     = "10.0.0.0/16"

  validation {
    condition     = var.subnet_cidr == "10.0.0.0/16"
    error_message = "The assignment requires the custom subnet to be 10.0.0.0/16."
  }
}

variable "private_services_cidr" {
  description = "CIDR range reserved for private services access to Cloud SQL. Must not overlap the subnet CIDR."
  type        = string
  default     = "10.10.0.0"
}

variable "machine_type" {
  description = "Compute Engine machine type required by the assignment."
  type        = string
  default     = "e2-standard-2"

  validation {
    condition     = var.machine_type == "e2-standard-2"
    error_message = "The assignment requires the VM machine type to be e2-standard-2."
  }
}

variable "db_tier" {
  description = "Cloud SQL machine tier required by the assignment."
  type        = string
  default     = "db-n1-standard-1"

  validation {
    condition     = var.db_tier == "db-n1-standard-1"
    error_message = "The assignment requires Cloud SQL tier db-n1-standard-1."
  }
}

variable "db_name" {
  description = "MySQL database name."
  type        = string
  default     = "gallerydb"

  validation {
    condition     = can(regex("^[A-Za-z0-9_]+$", var.db_name))
    error_message = "db_name may contain only letters, numbers, and underscores."
  }
}

variable "db_user" {
  description = "MySQL application user."
  type        = string
  default     = "galleryuser"

  validation {
    condition     = can(regex("^[A-Za-z0-9_]+$", var.db_user))
    error_message = "db_user may contain only letters, numbers, and underscores."
  }
}

variable "db_password" {
  description = "MySQL password for the application user. Provide this in terraform.tfvars and do not commit it."
  type        = string
  sensitive   = true

  validation {
    condition     = length(var.db_password) >= 12
    error_message = "db_password must be at least 12 characters."
  }
}

variable "app_port" {
  description = "Internal port used by Gunicorn. Nginx exposes the app on port 80."
  type        = number
  default     = 8080

  validation {
    condition     = var.app_port >= 1024 && var.app_port <= 65535
    error_message = "app_port must be between 1024 and 65535."
  }
}

variable "ssh_source_ranges" {
  description = "CIDR blocks allowed to SSH into the VM. For better security, replace 0.0.0.0/0 with your IP/32."
  type        = list(string)
  default     = ["0.0.0.0/0"]
}
