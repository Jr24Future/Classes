# Gallery Flask App

Small Flask/MySQL photo gallery app used as the Project 4-compatible application for the SE 4220 final Terraform deployment.

Features:

- New user registration
- Login/logout
- Upload photos
- Search photos by title, description, or filename
- View/download photos
- `/health` endpoint that checks database connectivity

For local development, create a `.env` file based on `.env.example` and run:

```bash
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python main.py
```
