FROM maven:3.9-eclipse-temurin-25 AS builder
LABEL authors="jgrafstrom"
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S netpulse && adduser -S netpulse -G netpulse
USER netpulse:netpulse

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]