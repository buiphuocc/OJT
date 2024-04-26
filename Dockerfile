#FROM openjdk:17-slim
#
#WORKDIR /app
#
#COPY . .
#
#RUN chmod +x ./mvnw
#
#RUN ./mvnw clean package -DskipTests
#
#RUN ./mvnw dependency:go-offline
#
#COPY src ./src
#
#EXPOSE 8080
#
#CMD ["./mvnw", "spring-boot:run"]

FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build target/FresherAcademyManagementSystem-0.0.1.jar demo1.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo1.jar"]