FROM java:8
ADD HC-userprofile-ms.jar userprofile.jar
EXPOSE 10614
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+8","/userprofile.jar","--spring.profiles.active=test"]
