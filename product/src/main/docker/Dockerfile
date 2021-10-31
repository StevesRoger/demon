FROM openjdk:8-slim
COPY @project.build.finalName@.jar @project.build.finalName@.jar
VOLUME /tmp
LABEL release-date="@timestamp@"
EXPOSE 8082
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/@project.build.finalName@.jar"]
