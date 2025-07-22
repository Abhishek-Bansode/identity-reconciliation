# Identity Reconciliation System

An identity reconciliation service that intelligently links contacts based on shared identifiers (*email* and/or *phone number*). Designed to avoid duplication and enable a consistent representation of a person's identity across different data sources.

> 🤖 **Problem Statement:** <https://bitespeed.notion.site/Bitespeed-Backend-Task-Identity-Reconciliation-1fb21bb2a930802eb896d4409460375c>

> 🚀 **Deployed on:** <https://identity-reconciliation-ae5r.onrender.com/swagger-ui/index.html>

---

## ✨ Features

- 🔗 Intelligent linking of duplicate contacts based on phone/email
- 📇 Maintains a single **primary** identity with **secondary** relationships
- 🔄 Automatic merging and demotion of conflicting primary records
- 🧪 Interactive API docs via **Swagger UI**
- 🐘 **PostgreSQL** (hosted on **Neon DB**)
- 🐳 **Dockerized** for containerized builds
- ☁️ CI/CD via **Render Blueprint + Docker**
- 🔐 Uses **environment variables** (Render secrets) for sensitive data

---

## 📦 Tech Stack

| Layer        | Tool / Service              |
|--------------|-----------------------------|
| Language     | Java&nbsp;17                 |
| Framework    | Spring Boot&nbsp;3.5.3       |
| ORM          | Spring Data JPA (Hibernate) |
| Database     | PostgreSQL (Neon DB)        |
| Build Tool   | Maven                       |
| Container    | Docker                      |
| Deployment   | Render (Docker-based)       |
| API Docs     | Swagger (Springdoc)         |
| API Testing  | Postman                     |

---

## 🧩 API Specification

### `POST /identify`
Create a new contact **or** link to an existing one based on the supplied *email* and/or *phoneNumber*.

#### ➡️ Request Body
```json
{
  "email": "john@example.com",
  "phoneNumber": "1234567890"
}
```

#### ⬅️ Response
```json
{
  "contact": {
    "primaryContactId": 1,
    "emails": ["john@example.com", "johnny@gmail.com"],
    "phoneNumbers": ["1234567890"],
    "secondaryContactIds": [2, 3]
  }
}
```

---

## 🔎 API Documentation with Swagger

You can interact with the API via Swagger UI:

🧭 **URL:** [/swagger-ui.html](https://identity-reconciliation-ae5r.onrender.com/swagger-ui/index.html)

Explore endpoints, try real-time API calls, and view schemas live.

---

## 🛠️ Local Development Setup

### Clone the repo
```bash
git clone https://github.com/Abhishek-Bansode/identity-reconciliation.git
cd identity-reconciliation
```

### Set environment variables
```bash
DB_URL=jdbc:postgresql://<host>:<port>/<dbname>
DB_USERNAME=<your_db_user>
DB_PASSWORD=<your_db_password>
```

### Run locally
```bash
./mvnw spring-boot:run
```

---

## 🐳 Run via Docker

### 1. Build the image
```bash
docker build -t identity-reconciliation-sb .
```

### 2. Start the container
```bash
docker run -d -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://<your-db-host>/<db> \
  -e DB_USERNAME=<your_user> \
  -e DB_PASSWORD=<your_pass> \
  identity-reconciliation-sb
```

---

## 🚀 Deployment (Render)

| Setting | Value |
|---------|-------|
| Service Type | Web Service |
| Runtime | Docker |
| Build Command | (external image – none) |
| Start Command | Handled via Dockerfile |

1. Push Docker image to Docker Hub (or use Dockerfile in repo)
2. Create Docker-based web service on [Render](https://render.com/)
3. Add these Environment Variables:
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`

---

## 📦 Render Blueprint Deployment (CI/CD Ready)

This project supports auto-deployments using a `render.yaml` file (Infrastructure-as-Code).

🚀 **Steps:**
1. Fork the GitHub repo
2. Update environment variables in the [Render dashboard](https://dashboard.render.com/)
3. Click "New Blueprint" → "Connect Repo"
4. Render will read `render.yaml` and spin up your service automatically

✅ No hardcoded credentials  
🔁 Auto-deploys on each Git push to main

---

## 🔐 Secrets & Security

- ✅ No sensitive credentials in code
- ✅ DB credentials are injected securely via Render Environment Variables
- ✅ Supports SSL connections for NeonDB in production
- ✅ Docker builds do not expose secrets

---

## 📬 Postman Collection

You can test all scenarios with the bundled Postman collection.

📥 **Download:** [Postman Collection (Click Here)](https://www.postman.com/collections/YOUR-COLLECTION-LINK)

Includes tests for:
- ✅ New contacts
- ✅ Email-only or phone-only matches
- ✅ Multi-secondary merging
- ✅ Demotion of duplicate primaries

---

## 🧹 Git Commit Hygiene

- ✅ Descriptive commit messages
- ✅ Atomic changes (feature-wise)
- ✅ Follows `.gitignore` for clean history

---

## 🧠 Learnings Applied

- ✅ Built end-to-end system using Spring Boot + Docker
- ✅ Mastered identity reconciliation logic (primary/secondary merging)
- ✅ Used NeonDB + Render + Swagger + Postman ecosystem
- ✅ Used `render.yaml` for production-ready deployment pipeline
- ✅ Applied secure practices (env vars, Docker secrets, no hardcoding)