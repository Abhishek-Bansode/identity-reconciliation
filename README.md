# Identity Reconciliation System

An identity reconciliation service that intelligently links contacts based on shared identifiers (*email* and/or *phone number*). Designed to avoid duplication and enable a consistent representation of a person’s identity across different data sources.

> 🤖 **Problem Statement:** <https://bitespeed.notion.site/Bitespeed-Backend-Task-Identity-Reconciliation-1fb21bb2a930802eb896d4409460375c>

> 🚀 **Deployed on:** <https://identity-reconciliation-sb.onrender.com/api/v1/identify>

---

## ✨ Features

- 🔗 Intelligent linking of duplicate contacts based on phone/email
- 📇 Maintains a single **primary** identity with **secondary** relationships
- 🛠️ Built using **Spring Boot&nbsp;3.5.3** and **Java&nbsp;17**
- 🐘 **PostgreSQL** (hosted on **Neon DB**)
- 🐳 **Dockerized** for containerized builds
- ☁️ Deployed to **Render** from a Docker image
- 🔐 Uses **environment variables** (or Docker/Render secrets) for sensitive data

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
| Deployment   | Render                      |
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

#### 📌 Rules
1. **No match found** → create a new *primary* contact.
2. **Match found** (by email **or** phone)
   - Link new/secondary contact to the existing primary.
3. If *both* matched contacts are *primary* → demote the **newer** one.
4. Circular linking is **prevented**.

---

## 🛠️ Local Development Setup

1. **Clone the repo**
   ```bash
   git clone https://github.com/Abhishek-Bansode/identity-reconciliation.git
   cd identity-reconciliation
   ```
2. **Set environment variables** (via `.env` or export):
   ```properties
   DB_URL=jdbc:postgresql://<host>:<port>/<dbname>
   DB_USERNAME=<your_db_user>
   DB_PASSWORD=<your_db_password>
   ```
3. **Run with Maven**
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

| Setting            | Value                       |
|--------------------|-----------------------------|
| **Service Type**   | Web Service                 |
| **Runtime**        | Docker                      |
| **Build Command**  | *(external image – none)*   |
| **Start Command**  | `java -jar app.jar`         |

1. Push Docker image to Docker Hub.
2. Create a **Docker-based** Web Service on Render.
3. Add the following **Environment Variables**:
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`
4. Deploy & verify – the service is live!

---

## 🔐 Secrets & Security

- **DB credentials are *never* hard-coded.**
- Use **Docker secrets** or **Render environment variables** to inject sensitive data.
- Production DB (Neon DB) requires **SSL-enabled** connections.

---

## 📬 Postman Collection
Test every scenario—including new contacts, partial matches, multiple secondaries—using the bundled Postman collection.

➡️ **Download:**

---

## 🧹 Git Commit Hygiene

- Descriptive, task-focused commit messages
- Logical commit grouping (init, entity, service, Docker, etc.)
- Excludes editor/OS artifacts (`.DS_Store`, build outputs, etc.)

---

## 🧠 Learnings Applied

- **Multi-stage Docker build** → smaller, cleaner images
- Migrated from Supabase to **Neon DB** for improved stability
- Modular Maven structure & package naming
- **Auto-deployment** via Docker image on Render
- Extensive identity-merging logic, validated against edge cases

---