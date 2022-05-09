FROM java:8
ADD HC-labmanagement-ms.jar labmanagement.jar
EXPOSE 10613
ENTRYPOINT ["java","-Xms200m","-Xmx250m","-jar","-Duser.timezone=GMT+8","/labmanagement.jar","--spring.profiles.active=test"]
