FROM adoptopenjdk/openjdk10
MAINTAINER Alex Lisle <alex.lisle@gmail.com>

#Create the zero trust directory
RUN mkdir -p /usr/share/zerotrust

ARG JAR_FILE
COPY ${JAR_FILE} /usr/share/zerotrust/zerotrust-oauth.jar

#Create the jwt signing key
RUN openssl req -x509 -newkey rsa:4096 -keyout /usr/share/zerotrust/jwt_key.pem -out /usr/share/zerotrust/jwt_cert.pem -days 365 --nodes -subj "/C=US/ST=Texas/L=Austin/O=ZeroTrust/OU=JWTSigning Department/CN=com.zerotrust.oauth"

# Export the jwt signing key  into pkcs12 format.
RUN openssl pkcs12 -export -in /usr/share/zerotrust/jwt_cert.pem -inkey /usr/share/zerotrust/jwt_key.pem -certfile /usr/share/zerotrust/jwt_cert.pem -out /usr/share/zerotrust/jwt_keystore.p12 -passout pass:wake_up_sheeple

# Generate the bloody jks file.
RUN keytool -v -importkeystore -srckeystore /usr/share/zerotrust/jwt_keystore.p12 -srcstoretype PKCS12 -destkeystore /usr/share/zerotrust/jwt_keystore.jks -deststoretype JKS -storepass wake_up_sheeple -srcstorepass wake_up_sheeple

#Create an alias so we can feel warm and fuzzy about finding it.
RUN keytool -changealias -storepass wake_up_sheeple -keystore /usr/share/zerotrust/jwt_keystore.jks -alias 1 -destalias jwt_signing_key





ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
#ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.edg=file:/dev/./urandom -jar /usr/share/zerotrust/zerotrust-oauth.jar" ]

EXPOSE 8080
EXPOSE 5005







