# KompoBack
A runnable microservice devops-ready java application built on DropWizard, Spring IoC, and more

## Build
```
mvn install
```

## Run
```
java -jar target/${artifactId}-${version}.jar
or 
./run.sh 
or 
run.bat
```

## Validate

```
https://localhost:8080/KompoBack
```

*Healthcheck*
```
http://localhost:8081/KompoBack/healthcheck
```

## Hystrix

We recomend to enhance your remote http calls within Hystrix Commands. 
It is quite simple, have a look at the ProxyExampleResource.

To generate example data use http://<host>:<port>/KompoBack/proxy?url=<some_host_to_get_data_from>


### Hystrix Dashboard

This application will forward Hystrix statistics and metrics on http://<host>:<adminPort>/hystrix.stream.
Use a Hystrix dashboard to view these data. See https://github.com/Netflix/Hystrix/tree/master/hystrix-dashboard
 
 Read more about Hystrix at:
 * https://github.com/Netflix/Hystrix/wiki/Getting-Started
 * https://github.com/zapodot/hystrix-dropwizard-bundle
 

