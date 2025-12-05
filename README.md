# 💰 MoneyManager — Java Personal Finance Desktop App

**MoneyManager** is a full-stack **personal finance manager** built with Java.  
It lets you securely track income & expenses, visualize trends, manage categories, and control your account — all inside a polished **Swing desktop UI** backed by a **Spring Boot + MongoDB** server.

---

## 🧭 Project Overview

MoneyManager is designed as a complete end-to-end Java project:

- **Frontend**: Java Swing desktop app
- **Backend**: Spring Boot REST API
- **Database**: MongoDB
- **Security**: JWT-based authentication + salted & peppered password hashes
- **DevOps**: Docker + docker-compose for easy backend + Mongo setup
- **Visuals**: Custom **Pie Chart** and **Time Series Chart** built manually using **Java2D** (no charting libraries)
- **UX**: Smooth navigation and non-blocking UI using **multithreading**

The app supports:

- User registration & login
- Add / edit / delete **income & expense** entries
- Filter transactions by **month & year**
- Visualize spending & income trends
- Manage categories and default currency
- Delete account with all related data

---

## 🧰 Tech Stack

### Frontend (Desktop)

- **Java 17+**
- **Swing UI**
- Custom:
  - Gradient backgrounds
  - Styled dropdowns, scrollbars, and buttons
  - Floating action button
- Shared style via:
  - `UIStyle`
  - `UIFonts`
  - `UIColors`

### Backend

- **Java + Spring Boot**
- **REST** endpoints returning JSON
- **MongoDB** via Spring Data
- **JWT** for authentication (login returns token)
- **Dotenv** for configuration (.env → EnvConfig)

### Infrastructure

- **MongoDB** database
- **Docker** & **docker-compose** for:
  - Mongo container
  - Spring Boot backend container
- Configuration via `.env` and `.env.template`

---

## 🖼 Screens & UX Flow

Below are all the major screens in **MoneyManager**, along with visuals and explanations of how the user interacts with them.

> ⚠️ Replace all placeholder image paths (`images/...`) with your actual screenshot paths.

---

### **1️⃣ Dashboard Screen**

The **Dashboard** is the main home screen that gives users a complete overview of their financial activity for the **selected month and year**.

**Features**

- Transactions automatically grouped by **date**
- Each day header displays:
  - _Total Income_
  - _Total Expense_
- Filters available:
  - **Month dropdown**
  - **Year dropdown**
- **Floating Action Button (+)** to add new transactions
- Clicking any transaction opens the **Edit Transaction** screen

![Dashboard](images/dashboard.png)

---

### **2️⃣ Add / Edit Transaction Screen**

This screen allows users to **create a new transaction** or **edit an existing one**.

**Fields**

- **Type:** Income / Expense (toggle)
- **Date:** Date spinner (`yyyy-MM-dd`)
- **Category:** User-defined custom categories
- **Payment Type:** Cash / Card / Bank Transfer / Debit Card
- **Note:** Optional description
- **Amount:** Must be `> 0` (validated)

**Behaviors**

- **Add Mode**
  - Submit button labeled **Save**
- **Edit Mode**
  - All fields pre-filled
  - Submit button labeled **Update**
  - **Delete** button shown at the bottom

![Add or Edit Transaction](images/new-entry.png)
![Add or Edit Transaction](images/edit-entry.png)

---

### **3️⃣ Statistics Screen — Custom Java2D Charts (No Libraries)**

This screen contains the richest visualization components in the app.  
Both charts are built manually using **pure Java2D** — **no external charting libraries** are used.

---

#### **A) Pie Chart**

Visualizes **expense distribution by category**.

**Features**

- Completely custom arc calculations
- Smooth anti-aliased drawing
- Custom color palette
- Category legend with:
  - Color chip
  - Category name
  - Total amount
- Auto updates when data changes

---

#### **B) Time Series Chart**

Shows **Income vs Expense** trends for a selected time period.

**Features**

- Custom axes & scaling
- Polyline graph drawing
- Two series (income & expense)
- Fully handcrafted using Java2D shapes
- Zero dependencies

![Stats](images/stats-piechart.png)
![Stats](images/stats-timeseries.png)

---

### **4️⃣ Manage Categories Screen**

Allows users to customize how their spending is categorized.

**Features**

- Add category (name + emoji/icon)
- Update category
- Delete category
- Changes reflect across:
  - Dashboard
  - Add/Edit Transaction screen
  - Statistics charts

![Categories](images/category.png)
![Categories](images/category-edit.png)

---

### **5️⃣ Settings Screen**

Central location for user preferences and account settings.

**Features**

- Change default **currency** (values fetched from backend `/currency`)
- Navigate to **Manage Categories**
- **Logout** (clears JWT token)
- **Delete Account** (with confirmation dialog)
- Clean and modern gradient UI

![Settings](images/settings.png)

---

### **6️⃣ Login & Registration Screens**

Optional, but part of the core UX flow.

**Login**

- Username + Password
- Backend returns a **JWT token**
- Token stored in **AppState**
- Expired/missing token → auto redirect to Login

**Registration**

- Secure password hashing & salting handled by backend
- Fully validated fields

![Login](images/login.png)
![Register](images/signup.png)

---

## ⚙️ Multithreading

MoneyManager uses **multithreading** to ensure the UI always stays smooth and responsive.  
All heavy or blocking operations run in **background threads**, while UI updates happen safely on the Swing Event Dispatch Thread.

### **Background Threads Handle**

#### **Loading**

- Entries
- Categories
- Currencies

#### **Saving**

- New entries
- Updated entries
- Category changes

#### **Deleting**

- Entries
- Categories
- User account

### **Safe UI Updates**

All UI refreshes are executed using:

```java
SwingUtilities.invokeLater(() -> {
    // UI updates here
});
```

## 🔐 JWT in MoneyManager (How It Works)

MoneyManager uses **JWT (JSON Web Token)** for secure login and protected API access.

### 1️⃣ Login → Receive Token

When the user logs in, the backend returns a signed JWT:

```json
{ "success": true, "token": "<JWT_TOKEN>" }
```

The Swing client stores this in `AppState`.

### 2️⃣ Client Sends Token on Every Request

All protected requests include the `Authorization` header:

```
Authorization: Bearer <JWT_TOKEN>
```

### 3️⃣ Backend Verifies Token

A custom `JwtAuthFilter`:

- extracts the token
- verifies signature
- checks expiration
- loads the authenticated user

Invalid or expired tokens → backend returns `401 Unauthorized`.

### 4️⃣ Auto Redirect on 401 (Client)

If the Swing client receives `401`:

- it clears the session
- redirects the user back to the Login screen

### 5️⃣ Protected vs Public Endpoints

- Public: `/auth/login`, `/auth/register`, `/currency`
- Protected: `/api/data-entries/**`, `/api/categories/**`, `/auth/**`

✅ Summary  
JWT ensures that only authenticated users can access or modify financial data, with automatic logout when the token expires.

## Docker & .env

- **What `docker-compose.yml` does:**

  - Orchestrates a MongoDB container and the Spring Boot backend container.
  - Typical command: `docker-compose up --build` — this builds the backend image (if changed) and starts both services.

- **What to expect after `docker-compose up --build`:**

  - MongoDB container will start and be available to the backend.
  - Spring Boot backend will start and listen on `http://localhost:8080` by default.

- **Environment variables (.env):**
  - The backend expects an environment file inside `server-springboot/` named `.env`.
  - Use `server-springboot/.env.template` (if present) as a reference and create `server-springboot/.env` with your values.
  - Example variables to include in `server-springboot/.env`:

```
MONGO_URI=mongodb://<host>:27017/moneymanager
JWT_SECRET=YOUR_LONG_SECRET_KEY
JWT_EXPIRY_MINUTES=60
SECURITY_PEPPER=SomeRandomPepperValue
```

    - Notes:
    	- If running everything through Docker compose, the MongoDB host may be the service name defined in `docker-compose.yml` (for example `mongodb` or `mongo`).
    	- For local development outside Docker you can point `MONGO_URI` at a local MongoDB instance (e.g. `mongodb://localhost:27017/moneymanager`).

- **How the backend loads the .env:**
  - The Spring Boot backend in `server-springboot/` loads environment variables using Dotenv in `EnvConfig`.
  - Make sure `server-springboot/.env` is present and contains the variables above before starting the backend (when not using Docker), or set them in your environment/compose file.

# Running the Project

1. Start Backend

Option A — Docker (recommended)

```bash
docker-compose up --build
```

Option B — Manual

```bash
cd server-springboot
mvn -DskipTests clean package
java -jar target/*.jar
```

Backend URL:

```
http://localhost:8080
```

2. Start Frontend (Swing)

Option A — from IDE

Run main class: `com.client.Main`

Option B — from terminal

```bash
cd client-swing
mvn clean package
java -jar target/*.jar
```

--
These instructions start the backend Spring Boot server and the Swing-based Java client.
If you want, I can also add a short Troubleshooting section or example `docker-compose` notes.

## Project Structure

```
MoneyManager/
 ├── client-swing/           # Java Swing desktop client
 │    ├── com/client/core    # AppState, ScreenManager, BasePanel, etc.
 │    ├── com/client/screens # Login, Dashboard, Stats, Settings, Categories...
 │    ├── com/client/model   # DataEntry, ExpenseCategory, Currency...
 │    └── com/client/components
 │    └── com/client/api
 │    └── com/client/constants
 │    └── com/client/utils
 │
 ├── server-springboot/      # Spring Boot backend
 │    ├── com/server/model       # Mongo entities
 │    ├── com/server/controller  # REST controllers
 │    ├── com/server/service     # Business logic
 │    ├── com/server/repository  # Spring Data repositories
 │    ├── com/server/security    # JWTUtil, JwtAuthFilter, Security config
 │    ├── com/server/config      # EnvConfig (.env loader)
 │    └── .env.template          # Env reference
 │
 ├── docker-compose.yml      # MongoDB + backend compose
 ├── README.md               # This file
 └── images/                 # Screenshots for docs
```

**API Documentation**

This document describes the HTTP API exposed by the MoneyManager server. It lists all endpoints, request/response shapes, authentication requirements, and example payloads.

**Base URL**: `http://localhost:8080`

**Authentication**:

- **Header**: `Authorization: Bearer <JWT>`
- **Notes**: Endpoints under `/auth/**` and `/currency` are public. All other endpoints require a valid JWT. Obtain a token via `POST /auth/login`.

**Schemas**

- **DataEntry** (model: `DataEntry`)

  - `id`: string
  - `username`: string
  - `type`: string (e.g. `Income` or `Expense`)
  - `date`: string (ISO date, e.g. `2025-12-04`)
  - `category`: string
  - `note`: string
  - `amount`: integer
  - `paymentType`: string (e.g. `Cash`, `Credit`)

- **ExpenseCategory** (model: `ExpenseCategory`)

  - `id`: string
  - `name`: string
  - `icon`: string
  - `username`: string

- **Currency** (model: `Currency`)
  - `id`: string
  - `name`: string
  - `code`: string (e.g. `USD`)
  - `symbol`: string (e.g. `$`)

**Auth Endpoints**

- `POST /auth/register`

  - Description: Register a new user.
  - Request JSON (all fields accepted by server):
    ```json
    {
      "firstName": "John",
      "lastName": "Doe",
      "email": "john@example.com",
      "username": "johndoe",
      "password": "s3cret"
    }
    ```
  - Response: plain text `"OK"` on success, `"EXISTS"` if user exists.

- `POST /auth/login`

  - Description: Login and receive JWT.
  - Request JSON:
    ```json
    {
      "username": "johndoe",
      "password": "s3cret"
    }
    ```
  - Response JSON (success):
    ```json
    {
      "success": true,
      "token": "<jwt-token>"
    }
    ```
  - Failure response JSON:
    ```json
    { "success": false, "message": "Invalid credentials" }
    ```

- `DELETE /auth/{username}`
  - Description: Delete user account by username.
  - Auth: Requires JWT (user must be authenticated).
  - Path parameter: `username` (string)
  - Response: `"OK"` on success, `"ERROR"` otherwise.

**Data Entry Endpoints** (base path: `/api/data-entries`)

- `POST /api/data-entries`

  - Description: Create a new data entry (income/expense).
  - Auth: Required (JWT)
  - Request JSON (`DataEntryRequest`):
    ```json
    {
      "username": "johndoe",
      "type": "Expense",
      "date": "2025-12-04",
      "category": "Food",
      "note": "Lunch",
      "amount": 1200,
      "paymentType": "Cash"
    }
    ```
  - Responses:
    - `"OK"` on success
    - `"INVALID"` if required fields are missing/invalid

- `GET /api/data-entries`

  - Description: Get all data entries (returns array of `DataEntry`).
  - Auth: Required
  - Response: `200 OK` with JSON array of `DataEntry` objects.

- `GET /api/data-entries/{id}`

  - Description: Get a single `DataEntry` by id.
  - Auth: Required
  - Path parameter: `id` (string)
  - Response: `200 OK` with `DataEntry` JSON or `204/404` depending on implementation (controller returns Optional).

- `GET /api/data-entries/user/{username}`

  - Description: Get all data entries for a specific user.
  - Auth: Required
  - Path parameter: `username` (string)
  - Response: `200 OK` JSON array of `DataEntry` objects.

- `PUT /api/data-entries/{id}`

  - Description: Update an existing data entry.
  - Auth: Required
  - Path parameter: `id` (string)
  - Request JSON: same as `POST /api/data-entries` (`DataEntryRequest`).
  - Responses: `"OK"` on success, `"NOT_FOUND"` if id does not exist.

- `DELETE /api/data-entries/{id}`
  - Description: Delete data entry by id.
  - Auth: Required
  - Response: `"OK"` after deletion.

**Expense Category Endpoints** (base path: `/categories`)

- `GET /categories/{username}`

  - Description: Get all categories for a user.
  - Auth: Required
  - Path parameter: `username` (string)
  - Response: `200 OK` JSON array of `ExpenseCategory`.

- `POST /categories`

  - Description: Create a new category.
  - Auth: Required
  - Request JSON (`CategoryRequest`):
    ```json
    {
      "name": "Food",
      "icon": "🍔",
      "username": "johndoe"
    }
    ```
  - Responses: `"OK"` on success, `"EXISTS"` if category already exists.

- `PUT /categories/{id}`

  - Description: Update a category by id.
  - Auth: Required
  - Request JSON: same as `POST /categories`.
  - Responses: `"OK"` on success, `"NOT_FOUND"` if missing.

- `DELETE /categories/{id}`
  - Description: Delete category by id.
  - Auth: Required
  - Responses: `"OK"` or `"NOT_FOUND"`.

**Currency Endpoints** (base path: `/currency`)

- `GET /currency`

  - Description: List all available currencies.
  - Auth: Public (no token required)
  - Response: `200 OK` JSON array of `Currency` objects.

- `POST /currency`

  - Description: Create a new currency.
  - Auth: Public (controller currently permits public access)
  - Request JSON (`CurrencyRequest`):
    ```json
    {
      "name": "US Dollar",
      "code": "USD",
      "symbol": "$"
    }
    ```
  - Responses: `"OK"` on success, `"EXISTS"` if already present.

- `PUT /currency/{id}`

  - Description: Update currency by id.
  - Auth: Public
  - Request JSON: same as `POST /currency`.
  - Responses: `"OK"` or `"NOT_FOUND"`.

- `DELETE /currency/{id}`
  - Description: Delete currency by id.
  - Auth: Public
  - Response: `"OK"` or `"NOT_FOUND"`.

**Headers / Examples**

- Example `Authorization` header:

  - `Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...`

- cURL example (login):
  ```bash
  curl -X POST "http://localhost:8080/auth/login" \
  	-H "Content-Type: application/json" \
  	-d '{"username":"johndoe","password":"s3cret"}'
  ```

**Notes & Next Steps**

- This README is intentionally API-only (no usage guide). If you want, I can:
  - generate a full OpenAPI (YAML/JSON) spec from these endpoints,
  - add example responses with HTTP status codes per endpoint,
  - or produce a Swagger UI static page.
