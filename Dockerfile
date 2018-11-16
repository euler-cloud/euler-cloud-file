FROM cfrost/java:8u171-jre-oracle

RUN yum update -y && mkdir /app

COPY target/euler-cloud-*.jar /app/app.jar
COPY run.sh /app/run.sh

RUN chmod -R 755 /app

EXPOSE 8080

WORKDIR /app

ENTRYPOINT [ "./run.sh" ]
