FROM openjdk:17-jdk-slim

WORKDIR /app

# Add healthcheck to ensure Spring Boot is healthy
HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:8085/actuator/health || exit 1

# Copy the wait-for-it script
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Copy the built JAR from Jenkins workspace
COPY target/Anywrpfe-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8085

# Entry point to wait for MySQL and start the application
ENTRYPOINT ["/wait-for-it.sh", "mysql-db:3306", "--", "java", "-jar", "app.jar"]
