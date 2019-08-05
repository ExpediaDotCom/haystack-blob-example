FROM openjdk:8-jre
MAINTAINER Haystack <haystack@expedia.com>

ARG run_type

ENV APP_NAME haystack-blob-example-service-1.0-SNAPSHOT
ENV APP_HOME /app/bin/blobs_home
ENV RUN_TYPE_VAL=$run_type

COPY /target/${APP_NAME}.jar ${APP_HOME}/

WORKDIR ${APP_HOME}

COPY . .

CMD java -jar haystack-blob-example-service-1.0-SNAPSHOT.jar $RUN_TYPE_VAL