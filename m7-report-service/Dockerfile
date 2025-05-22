FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} m7-report-service.jar
ENTRYPOINT ["java","-jar","./m7-report-service.jar"]