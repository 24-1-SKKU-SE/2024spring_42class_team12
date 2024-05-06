FROM amd64/amazoncorretto:17

WORKDIR /app

COPY ./build/libs/fixskku-0.0.1-SNAPSHOT.jar /app/fixskku.jar

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=dev", "/app/fixskku.jar"]
