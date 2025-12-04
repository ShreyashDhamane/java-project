# build stage
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -f pom.xml clean package -DskipTests

# runtime stage
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy JAR 
COPY --from=build /app/target/*.jar app.jar

COPY .env .env

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
