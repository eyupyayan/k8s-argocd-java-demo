# --- build stage ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /src
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

# --- runtime stage ---
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /src/target/java-demo-1.0.0.jar /app/app.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
