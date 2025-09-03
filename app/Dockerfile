FROM gradle:8.3.0-jdk17 AS build

WORKDIR /app

COPY ../gradle gradle
COPY ../gradlew .
COPY settings.gradle .
COPY build.gradle .

RUN ./gradlew dependencies --no-daemon

COPY ../.. .

RUN ./gradlew bootJar --no-daemon

FROM amazoncorretto:17-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

FROM nginx:alpine

WORKDIR /usr/share/nginx/html

COPY --from=build /app/dist .

COPY ../nginx/nginx.conf /etc/nginx/nginx.conf

RUN mkdir -p /var/log/nginx

EXPOSE 8010

CMD ["nginx", "-g", "daemon off;"]
CMD ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=90.0", "-jar", "app.jar"]
