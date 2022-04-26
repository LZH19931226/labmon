FROM java:8
ADD HC-userprofile-ms.jar userprofile.jar
EXPOSE 10610
ENTRYPOINT ["java","-Xms200m","-Xmx250m","-jar","-Duser.timezone=GMT+8","/userprofile.jar","--spring.profiles.active=test"]
