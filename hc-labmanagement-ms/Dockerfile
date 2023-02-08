FROM java:8
ADD HC-labmanagement-ms.jar labmanagement.jar
EXPOSE 10613
ENTRYPOINT ["java","-Xmx256m","-Xms256m","-jar","-Duser.timezone=GMT+8","/labmanagement.jar","--spring.profiles.active=test"]
