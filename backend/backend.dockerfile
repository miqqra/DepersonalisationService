FROM openjdk:18

RUN mkdir /app

COPY backend/build/libs/backend-0.0.1-SNAPSHOT-plain.jar /app/.

CMD java -jar ./app/backend-0.0.1-SNAPSHOT-plain.jar