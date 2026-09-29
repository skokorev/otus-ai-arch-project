FROM bellsoft/liberica-openjdk-alpine:21
MAINTAINER Yahoondex architecture chapter

COPY ./target/configurator.jar configurator.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/configurator.jar"]