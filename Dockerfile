FROM openjdk:17-jdk-alpine
RUN mkdir /app
WORKDIR /app
COPY target/*.jar /app/api-saldos-0.0.1-SNAPSHOT.jar
EXPOSE 8088
ENTRYPOINT ["java","-jar","api-saldos-0.0.1-SNAPSHOT.jar"]


