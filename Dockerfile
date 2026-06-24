FROM eclipse-temurin:21-jdk-alpine AS build

COPY . /app
WORKDIR /app

RUN ./gradlew build --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 10000

CMD ["java", "-jar", "app.jar"]