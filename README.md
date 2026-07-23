# 🚀 Medifly

**Hyperlocal Emergency & Subscription Medicine Delivery Platform**

Medifly is a modern, ultra-fast medicine delivery application designed to bring essential healthcare products directly to your doorstep. Optimised for emergency and routine subscription medicine restocking, it connects users with pharmacy care precisely when they need it.

---

## 🛠️ Tech Stack

- **Frontend**: React 19, Vite, React Router, Socket.io Client, Lucide icons.
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

You can manage both the frontend and backend from the root directory using the following NPM scripts:

#### 1. Running in Development
To run both the backend and frontend concurrently:
```bash
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
