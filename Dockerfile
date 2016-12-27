FROM azul/zulu-openjdk-centos:8
MAINTAINER Kim Christian Gaarder <kim.christian.gaarder@gmail.com>
RUN yum -y install yum-cron
RUN yum -y update
RUN yum -y install curl

# Install Application
RUN adduser KompoBack
ADD target/KompoBack-application-*.jar /home/KompoBack/KompoBack-application.jar"
ADD docker/KompoBack.yml /home/KompoBack/KompoBack.yml
RUN chown KompoBack:KompoBack /home/KompoBack/KompoBack.yml
ADD docker/KompoBack_override.properties /home/KompoBack/KompoBack-override.properties
RUN chown KompoBack:KompoBack /home/KompoBack/KompoBack.properties

EXPOSE 21500:21599

WORKDIR "/home/KompoBack"
CMD [ \
    "java", \
    "-Xdebug", \
    "-Xrunjdwp:transport=dt_socket,address=21515,server=y,suspend=n", \
    "-Dcom.sun.management.jmxremote.port=21516", \
    "-Dcom.sun.management.jmxremote.rmi.port=21516", \
    "-Dcom.sun.management.jmxremote.ssl=false", \
    "-Dcom.sun.management.jmxremote.local.only=false", \
    "-Dcom.sun.management.jmxremote.authenticate=false", \
    "-Djava.rmi.server.hostname=localhost", \
    "-jar", \
    "KompoBack-application.jar" \
]
