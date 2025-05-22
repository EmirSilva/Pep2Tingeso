FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} m6-rack-service.jar
ENTRYPOINT ["java","-jar","./m6-rack-service.jar"]