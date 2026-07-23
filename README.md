
# 🚀 Medifly

**Hyperlocal Emergency & Subscription Medicine Delivery Platform**

Medifly is a modern, ultra-fast medicine delivery application designed to bring essential healthcare products directly to your doorstep. Optimised for emergency and routine subscription medicine restocking, it connects users with pharmacy care precisely when they need it.

---

## 🛠️ Tech Stack

- **Frontend**: React 19, Vite, React Router, Socket.io Client, Lucide icons, CSS Modules, custom `MedicineAutocomplete` component for interactive search.
- **Backend**: Java 21, Spring Boot 3.3.5, Spring Security, JPA/Hibernate, WebSockets.
- **Database**: PostgreSQL (Production) / H2 In-Memory Database (Development Fallback).

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

---

### Setup & Run Commands

From the repository root, install the frontend dependencies and start both services:

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
