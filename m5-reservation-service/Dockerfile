FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} m5-reservation-service.jar
ENTRYPOINT ["java","-jar","./m5-reservation-service.jar"]