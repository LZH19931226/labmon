FROM java:8
ADD HC-labmanagement-ms.jar labmanagement.jar
EXPOSE 10610
ENTRYPOINT ["java","-Xms200m","-Xmx250m","-jar","-Duser.timezone=GMT+8","/labmanagement.jar","--spring.profiles.active=dev"]
