
# 🚀 Medifly

**Hyperlocal Emergency & Subscription Medicine Delivery Platform**

Medifly is a modern, ultra-fast medicine delivery application designed to bring essential healthcare products directly to your doorstep. Optimised for emergency and routine subscription medicine restocking, it connects users with pharmacy care precisely when they need it.

---

## 🛠️ Tech Stack

- **Frontend**: React 19, Vite, React Router, Lucide icons, CSS Modules, custom `MedicineAutocomplete` component, Clerk React SDK (`@clerk/clerk-react`) for authentication (Google 1-click & Email OTP), and a dedicated API service client (`fetch` with JWT authentication).
- **Backend**: Java 21, Spring Boot 3.3.5, Spring Security (OAuth2 Resource Server / JWT-based stateless authentication), JPA/Hibernate, WebSockets (STOMP/SockJS), and an automated database seeder (`MedicineDataSeeder`).
- **Database**: PostgreSQL (Production) / H2 In-Memory Database (Development Fallback).
- **DevOps & Deployment**: Docker (Dockerfile) and Railway deployment configuration (`railway.json`).

---
## 📦 Project Structure

- `/frontend` - React Vite client UI application.
- `/backend` - Java Spring Boot server & API logic.
- `package.json` - Root monorepo scripts for running both components concurrently.

---

## 🚀 Getting Started

### Prerequisites

1. **Java JDK 21+** (e.g. Eclipse Temurin JDK 21 or higher)
2. **Node.js LTS** (Node 24+ and NPM 11+)
3. PostgreSQL (Optional; if not configured, the backend automatically runs in H2 In-Memory mode).
4. **Clerk Account** (for Google 1-click & Email OTP authentication).

### Configuration

Configure your Clerk credentials in the frontend environment variables (e.g., `VITE_CLERK_PUBLISHABLE_KEY`).

### Database Seeding

On startup, the backend automatically detects if the database is empty and seeds medicine records using the `MedicineDataSeeder` from `medicines.json` (located in the classpath or `frontend/public/medicines.json`).

---

### Setup & Run Commands

From the repository root, install the frontend dependencies and start both services (the backend runs on port `5000` and the frontend on port `5173` / `3000`):

bash
# Install frontend packages
npm ci

# Start development mode (backend + frontend) concurrently
npm run dev


The `dev` script launches the Spring Boot backend (`./gradlew bootRun`) and the Vite‑powered React frontend with hot‑reloading. The application will be available at `http://localhost:5173` and the API at `http://localhost:8080`.

### Additional Notes

- The redesign introduces an **interactive medicine search** powered by the new `MedicineAutocomplete` component. No extra setup is required beyond the standard frontend install.
- **Express delivery** and **auto‑refill subscription** features are now part of the UI; they rely on the existing backend endpoints, so ensure the backend is running.
- If you prefer to run the services separately, you can use:
  bash
  # Backend only
  ./gradlew bootRun

  # Frontend only (from the frontend folder)
  cd frontend && npm run dev
  

Enjoy the refreshed MediFly experience!
# In PowerShell:
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass; npm run dev

# Or in Command Prompt (cmd) / via npm.cmd:
npm run dev
```

* This spins up:
  * **Frontend**: `http://localhost:3000`
  * **Backend**: `http://localhost:5000` (with H2 Console at `http://localhost:5000/h2-console`)

#### 2. Running Individual Services
* **Backend only**: `npm run dev:backend`
* **Frontend only**: `npm run dev:frontend`

#### 3. Build & Package
* **Build Frontend**: `npm run build:frontend`
* **Build Backend**: `npm run build:backend` (generates the Boot JAR file)
