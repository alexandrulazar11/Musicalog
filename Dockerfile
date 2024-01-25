FROM openjdk:21-slim
COPY build/libs/*.jar /app/musicalog.jar
RUN chmod 644 /app/musicalog.jar
ENTRYPOINT ["java", "-jar", "/app/musicalog.jar"]
