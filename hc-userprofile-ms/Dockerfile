FROM openjdk:8
ADD HC-userprofile-ms.jar userprofile.jar
EXPOSE 10614
ENTRYPOINT ["java","-Xms256m","-Xmx256m","-jar","-Duser.timezone=America/Phoenix","/userprofile.jar","--spring.profiles.active=test"]
