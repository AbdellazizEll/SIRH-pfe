FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the wait-for-it script into the image
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Copy the JAR file to the container
COPY target/Anywrpfe-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8085

# Use wait-for-it.sh to wait for MySQL before starting the app
ENTRYPOINT ["/wait-for-it.sh", "mysql-db:3306", "--", "java", "-jar", "app.jar"]
