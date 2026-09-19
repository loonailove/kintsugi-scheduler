FROM gradle:8-jdk21 AS build
COPY --chown=gradle:gradle worker /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew build -x test

FROM openjdk:21-slim
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]