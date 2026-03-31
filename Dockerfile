FROM openjdk:17
WORKDIR /app
COPY . .
RUN ./mvnw clean install
CMD ["java", "-jar", "target/*.jar"]
