FROM docker.io/eclipse-temurin:22-jre-alpine

LABEL maintainer="Luiz Toni <luiztoni@example.com>"
LABEL version="1.0"
LABEL description="Rest API"

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# https://spring.io/guides/gs/spring-boot-docker
# Example 3. Dockerfile

# For maven 
# mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

ARG DEPENDENCY=target/dependency

COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","br.luiztoni.restapi.RestApiApplication"]
