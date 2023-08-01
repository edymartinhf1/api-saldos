FROM openjdk:11.0.16
WORKDIR /app
COPY ./target/api-saldos-0.0.1-SNAPSHOT.jar .
EXPOSE 8088
ENTRYPOINT ["java","-jar","api-saldos-0.0.1-SNAPSHOT.jar"]


