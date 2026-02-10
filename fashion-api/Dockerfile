# Build stage
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy gradle files
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Grant execution permission and download dependencies
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

# Copy source code and build
COPY src src
RUN ./gradlew bootJar --no-daemon

# Final stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the build output from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Create a directory for logs
RUN mkdir -p logs && chmod 777 logs

# Set environment variables for Render compatibility
ENV PORT=8080
EXPOSE ${PORT}

# Run the application
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
