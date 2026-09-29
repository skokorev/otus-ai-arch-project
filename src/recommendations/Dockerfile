FROM bellsoft/liberica-openjdk-alpine:21
MAINTAINER Yahoondex architecture chapter

COPY ./target/recommendations.jar recommendations.jar

ENTRYPOINT ["java", "-jar", "/recommendations.jar"]