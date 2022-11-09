FROM java:8
ADD hc-openway-ms.jar openway.jar
EXPOSE 10612
ENTRYPOINT ["java","-Xms512m","-Xmx512m","-jar","-Duser.timezone=GMT+8","/openway.jar","--spring.profiles.active=test"]
