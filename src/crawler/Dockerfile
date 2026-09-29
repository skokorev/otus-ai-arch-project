FROM bellsoft/liberica-openjdk-alpine:21
MAINTAINER Yahoondex architecture chapter

COPY ./target/crawler.jar crawler.jar

ENTRYPOINT ["java", "-jar", "/crawler.jar"]