FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER Alex Lisle <alex.lisle@gmail.com>

ARG JAR_FILE
COPY ${JAR_FILE} /usr/share/notrust/notrust.jar

ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.edg=file:/dev/./urandom -jar /usr/share/notrust/notrust.jar" ]

EXPOSE 8080








