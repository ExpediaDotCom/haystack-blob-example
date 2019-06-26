FROM openjdk:8-jre
MAINTAINER Haystack <haystack@expedia.com>

ARG user=blobs
ARG group=blobs
ARG uid=1001
ARG gid=1001

ENV APP_NAME span-blob-example-service-1.0-SNAPSHOT
ENV APP_HOME /app/bin/blobs_home

RUN mkdir -p ${APP_HOME} && chown ${uid}:${gid} ${APP_HOME} && groupadd -g ${gid} ${group} && useradd -d "${APP_HOME}" -u ${uid} -g ${gid} -m -s /bin/bash ${user}

RUN mkdir -p ${APP_HOME}/JMH-BenchmarkingResults

RUN chown -R ${user} "$APP_HOME"

RUN chmod 777 ${APP_HOME}/JMH-BenchmarkingResults

VOLUME $APP_HOME

COPY /target/${APP_NAME}.jar ${APP_HOME}/

WORKDIR ${APP_HOME}

USER ${user}

ENTRYPOINT ["java", "-jar", "span-blob-example-service-1.0-SNAPSHOT.jar", "benchmark"]