FROM adoptopenjdk/openjdk10
MAINTAINER Alex Lisle <alex.lisle@gmail.com>

ARG JAR_FILE
COPY ${JAR_FILE} /usr/share/zerotrust/zerotrust.jar

ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.edg=file:/dev/./urandom -jar /usr/share/zerotrust/zerotrust.jar" ]

EXPOSE 8080








