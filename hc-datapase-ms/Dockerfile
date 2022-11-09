FROM java:8
ADD HC-datapase-ms.jar datapase.jar
EXPOSE 10613
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+8","/mswk.jar","--spring.profiles.active=test"]
