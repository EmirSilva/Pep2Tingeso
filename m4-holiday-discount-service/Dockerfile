FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} m4-holiday-discount-service.jar
ENTRYPOINT ["java","-jar","./m4-holiday-discount-service.jar"]