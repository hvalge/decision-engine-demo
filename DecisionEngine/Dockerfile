FROM gradle:8.4.0-jdk17 AS BUILD
WORKDIR /usr/app/
COPY . .
RUN gradle build

FROM eclipse-temurin:17-jdk-jammy
VOLUME /tmp

COPY --from=build /usr/app/build/libs/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","/app.jar"]