FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the wait-for-it script
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Copy the built JAR from Jenkins workspace
# The JAR is already present in sirh-backend/target after Jenkins build
COPY target/Anywrpfe-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8085

ENTRYPOINT ["/wait-for-it.sh", "mysql-db:3306", "--", "java", "-jar", "app.jar"]
