FROM openjdk:8
ADD hc-openway-ms.jar openway.jar
EXPOSE 10612
ENTRYPOINT ["java","-Xms256m","-Xmx256m","-jar","-Duser.timezone=America/Phoenix","/openway.jar","--spring.profiles.active=test"]
