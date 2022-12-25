
FROM openjdk:11

COPY build/libs/*.jar application.jar

ENV TZ=Asia/Seoul

EXPOSE 8888

CMD ["java", "-jar", "/application.jar"]