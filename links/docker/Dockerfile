FROM adoptopenjdk/openjdk10
MAINTAINER Alex Lisle <alex.lisle@gmail.com>

ARG JAR_FILE
COPY ${JAR_FILE} /usr/share/zerotrust/zerotrust-links.jar

ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
#ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.edg=file:/dev/./urandom -jar /usr/share/zerotrust/zerotrust-rest.jar" ]

EXPOSE 5007
EXPOSE 8080








