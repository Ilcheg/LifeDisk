FROM openjdk:17

WORKDIR /app

EXPOSE 8080

ADD target/LifeDisk-0.0.1-SNAPSHOT.jar backend.jar

ENTRYPOINT ["java", "-jar", "backend.jar"]