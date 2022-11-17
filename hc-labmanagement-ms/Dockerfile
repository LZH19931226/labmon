FROM java:8
ADD HC-labmanagement-ms.jar labmanagement.jar
EXPOSE 10613
ENTRYPOINT ["java","-Xms256m","-Xmx512m","-jar","-Duser.timezone=GMT+8","/labmanagement.jar","--spring.profiles.active=test"]
