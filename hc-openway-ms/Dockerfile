FROM openjdk:8
ADD hc-openway-ms.jar openway.jar
EXPOSE 10612
ENTRYPOINT ["java","-Xms256m","-Xmx256m","-jar","-Duser.timezone=GMT+8","/openway.jar","--spring.profiles.active=test"]
