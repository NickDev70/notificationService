FROM openjdk:17-jdk-slim

WORKDIR /app

COPY mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY pom.xml ./

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests

RUN addgroup --system spring && adduser --system spring --ingroup spring
RUN chown -R spring:spring /app
USER spring:spring

EXPOSE 8080

CMD ["java", "-jar", "target/notification-service-0.0.1-SNAPSHOT.jar"]