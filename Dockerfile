FROM azul/zulu-openjdk-centos:8
MAINTAINER Kim Christian Gaarder <kim.christian.gaarder@gmail.com>
RUN yum -y install yum-cron
RUN yum -y update
RUN yum -y install curl

# Install Application
RUN adduser kompoback
ADD target/kompoback-application-*.jar /home/kompoback/kompoback-application.jar"
ADD docker/kompoback.yml /home/kompoback/kompoback.yml
RUN chown kompoback:kompoback /home/kompoback/kompoback.yml
ADD docker/kompoback_override.properties /home/kompoback/kompoback-override.properties
RUN chown kompoback:kompoback /home/kompoback/kompoback.properties

EXPOSE 21500:21599

WORKDIR "/home/kompoback"
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
    "kompoback-application.jar" \
]
