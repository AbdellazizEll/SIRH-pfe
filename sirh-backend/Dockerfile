FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy only pom.xml first for caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the application code
COPY src ./src

# Copy the wait-for-it script
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Package the application
RUN mvn clean package -DskipTests

EXPOSE 8085

ENTRYPOINT ["/wait-for-it.sh", "mysql-db:3306", "--", "java", "-jar", "target/Anywrpfe-0.0.1-SNAPSHOT.jar"]
