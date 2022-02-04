FROM openjdk:8-slim
COPY @project.build.finalName@.jar @project.build.finalName@.jar
ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install vim -y && apt-get install -y wget
RUN apt-get install curl -y && apt-get install dnsutils -y && apt-get install telnet -y
ENV TZ=Asia/Phnom_Penh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
VOLUME /tmp
LABEL release-date="@timestamp@"
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/@project.build.finalName@.jar"]