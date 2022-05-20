FROM java:8
ADD HC-labmon-ms.jar mon.jar
EXPOSE 10620
ENTRYPOINT ["java","-Xms200m","-Xmx250m","-jar","-Duser.timezone=GMT+8","/mon.jar","--spring.profiles.active=test"]
