FROM openjdk:12-jdk-alpine
RUN apk add --no-cache bash
COPY build/libs/*.jar /app.jar
COPY src/main/bash/start.sh /start.sh
ENTRYPOINT ["bash","/start.sh"]