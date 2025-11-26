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
