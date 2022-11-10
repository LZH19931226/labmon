FROM java:8
ADD HC-labmon-ms.jar mon.jar
EXPOSE 10620
ENTRYPOINT ["java","-Xms512m","-Xmx512m","-jar","-Duser.timezone=GMT+8","/mon.jar","--spring.profiles.active=test"]
