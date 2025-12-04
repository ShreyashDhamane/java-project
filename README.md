# MoneyManager

Simple Java app using:

- Swing (desktop UI)
- Spring Boot (backend)
- Threads (background tasks)
- MongoDB (data storage)
- Docker (optional local deployment)
- HTTP (client ↔ server)

Quick run

1. Start backend (recommended: docker-compose)

   - docker-compose (starts MongoDB + backend)
     ```sh
     docker-compose up --build
     ```
   - or run backend locally:
     ```sh
     cd server-springboot
     mvn -DskipTests clean package
     java -jar target/*.jar
     ```
     Backend runs on http://localhost:8080

2. Run frontend (Swing)
   - simply right click the Main.java file and hit Run
   - Or
   - build and run JAR:
     ```sh
     cd client-swing
     mvn clean package
     java -jar target/*.jar
     ```
   - or run from your IDE: run [com.client.Main](http://_vscodecontentref_/0)

Notes

- Ensure backend is reachable at http://localhost:8080 before starting the Swing client.
- Docker-compose sets Mongo credentials and connection for the backend.
- Create a .env file directly inside the server-springboot, follow the .env.template for structure.

## API Documentation

Base URL: `http://localhost:8080`

All endpoints accept and return JSON (`Content-Type: application/json`).

1) Authentication

- POST `/api/auth/signup`
  - Body: `{ "firstName": "...", "lastName": "...", "email": "...", "username": "...", "password": "..." }`
  - Response: `{ "success": true|false, "message": "...", "userId": <id> }`
  - Purpose: create a new user account.

- POST `/api/auth/login`
  - Body: `{ "username": "...", "password": "..." }`
  - Response: `{ "success": true|false, "message": "..." }`
  - Purpose: authenticate a user (current implementation returns success flag).

2) Entries (transactions)

- GET `/api/entries?username={username}`
  - Response: array of entry objects: `[{ id, username, type, date (yyyy-MM-dd), category, note, amount, paymentType }, ...]`
  - Purpose: fetch entries for a user. Optional query params (server may support): `from`, `to`, `type`.

- POST `/api/entries`
  - Body: `{ "username":"...","type":"Income|Expense","date":"yyyy-MM-dd","category":"...","note":"...","amount":123,"paymentType":"Cash|Card|..." }`
  - Response: created entry object
  - Purpose: add a new transaction.

- PUT `/api/entries/{id}`
  - Body: same as POST
  - Response: updated entry object

- DELETE `/api/entries/{id}`
  - Response: success status

3) Categories

- GET `/api/categories?username={username}`
  - Response: `[{ id, username, name, color, ... }, ...]`
  - Purpose: list categories for a user.

- POST `/api/categories`
  - Body: `{ "username":"...","name":"...","color":"#hex" }`
  - Response: created category

- DELETE `/api/categories/{id}`
  - Response: success status

4) Users / Account

- GET `/api/users/{username}`
  - Response: user profile (non-sensitive fields)

- DELETE `/api/users/{username}`
  - Response: success status (deletes user and related data)
