FROM bellsoft/liberica-openjdk-alpine:21
MAINTAINER Yahoondex architecture chapter

COPY ./target/communicator.jar communicator.jar

ENTRYPOINT ["java", "-jar", "/communicator.jar"]