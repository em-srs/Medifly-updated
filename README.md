
# 🚀 Medifly

**Hyperlocal Emergency & Subscription Medicine Delivery Platform**

Medifly is a modern, ultra-fast medicine delivery application designed to bring essential healthcare products directly to your doorstep. Optimised for emergency and routine subscription medicine restocking, it connects users with pharmacy care precisely when they need it.

The platform features a static dataset UI browsing over 400+ medicines across multiple therapeutic categories (such as Pain Relief, Antibiotics, Diabetes, Heart Care, and more) with robust client-side search, sorting, and pagination. It also supports secure user authentication with Clerk, featuring seamless asynchronous sign-out and account switching capabilities directly from the login interface.
## 🛠️ Tech Stack

- **Frontend**: React 19, Vite, React Router, Clerk (for Email OTP & Google authentication), Vercel Analytics (for tracking page views and visitor metrics), Lucide icons, CSS Modules, custom `MedicineAutocomplete` component, and a dedicated API service client (`fetch` with JWT authentication).
- **Backend**: Java 21, Spring Boot 3.3.5, JPA/Hibernate, WebSockets (STOMP/SockJS), and an automated database seeder (`MedicineDataSeeder`).
- **Database**: Supabase PostgreSQL (Production) / H2 In-Memory Database (Development Fallback).

---
## 📦 Project Structure

- `/frontend` - React Vite client UI application.
- `/backend` - Java Spring Boot server & API logic.
- `Medifly_PostgreSQL_Plan.docx` - PostgreSQL database integration and schema migration plan.
- `package.json` - Root monorepo scripts for running both components concurrently.

---
## 🚀 Getting Started

### Prerequisites

1. **Java JDK 21+** (e.g. Eclipse Temurin JDK 21 or higher)
2. **Node.js LTS** (Node 24+ and NPM 11+)
3. **PostgreSQL** (Optional; configured to use Supabase PostgreSQL in production. If not configured, the backend automatically runs in H2 In-Memory mode. The `PostgreSQLDialect` is explicitly set in `application.properties` to bypass metadata discovery errors. Refer to `Medifly_PostgreSQL_Plan.docx` for database setup and migration details).
4. **Clerk Publishable Key** (Optional; set `VITE_CLERK_PUBLISHABLE_KEY` in your frontend environment variables to enable Clerk Email OTP & Google authentication. When configured, Clerk authentication state is seamlessly synced with `AuthContext` to manage user sessions and dashboard access. For production deployments on Vercel, ensure this environment variable is configured in your project settings to trigger and support successful redeploys).
5. **Docker** (Optional; for containerized backend deployment and cloud hosting using the production `Dockerfile` in the backend directory. The Dockerfile copies the exact `app.jar` build artifact (with the plain jar task disabled in Gradle to prevent duplicate archives) and executes `gradlew` via an explicit `sh` shell wrapper to support seamless Linux-based builds on platforms like Render).
6. **JWT & Security Configuration** (Configure `jwt.secret` and optionally `jwt.expiration.ms` in your backend properties to secure the custom JWT authentication filter. Role-Based Access Control (RBAC) is enforced via `@EnableMethodSecurity` to restrict administrative and pharmacy endpoints, such as `/api/admin/dashboard` to `ADMIN` and price/prescription updates to `ADMIN` or `PHARMACY` roles. Public GET access is permitted for `/api/medicines`, `/api/medicines/**`, `/api/v1/medicines`, and `/api/v1/medicines/**` to allow unauthenticated browsing of medicines. CORS is configured to allow all origin patterns (`*`) with credentials enabled to support seamless cross-origin requests).

### Database Seeding

On startup, the backend automatically detects if the database is empty and seeds medicine records using the provided `medicines.csv` or `meds_dB_original.csv` datasets located in the backend data directory.

---

### Setup & Run Commands

From the repository root, install the frontend dependencies and start both services (the backend runs on port `5000` and the frontend on port `5173` / `3000`):
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
