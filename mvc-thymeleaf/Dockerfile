FROM eclipse-temurin:21-jre-noble
MAINTAINER Luiz Toni
WORKDIR /
COPY target/*.jar application.jar
ENV PORT=${PORT}
EXPOSE ${PORT}
CMD java -jar app.jar

# .env file content example: PORT=8080
# Run: docker run -p ${PORT}:${PORT} --env-file .env app
