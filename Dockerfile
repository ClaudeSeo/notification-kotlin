FROM amazoncorretto/amazoncorretto:17-alpine-jdk as builder

RUN mkdir /usr/app
WORKDIR /usr/app

COPY . .

RUN ./gradlew clean build --parallel -x ktlintMainSourceSetCheck -x ktlintTestSourceSetCheck --no-daemon

FROM amazoncorretto/amazoncorretto:17-alpine-jdk

ENV TZ "Asia/Seoul"

RUN mkdir /usr/app
WORKDIR /usr/app

RUN addgroup --system --gid 1001 notification
RUN adduser --system --uid 1001 --group notification
RUN chown -R notification:notification /usr/app
USER notification

COPY --from=builder /usr/app/notification-api/build/libs/notification-api.jar ./notification-api.jar
COPY --from=builder /usr/app/notification-core/build/libs/notification-core.jar ./notification-core.jar
COPY --from=builder /usr/app/entrypoint.sh ./entrypoint.sh

EXPOSE 3000
ENTRYPOINT ["sh", "entrypoint.sh"]
