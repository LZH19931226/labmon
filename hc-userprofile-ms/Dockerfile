FROM openjdk:8
ADD HC-userprofile-ms.jar userprofile.jar
EXPOSE 10614
ENTRYPOINT ["java","-Xms256m","-Xmx256m","-jar","-Duser.timezone=GMT+8","/userprofile.jar","--spring.profiles.active=test"]
