FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} m1-price-service.jar
ENTRYPOINT ["java","-jar","./m1-price-service.jar"]