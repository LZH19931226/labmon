FROM openjdk:8
ADD HC-MSCT-ms.jar msct.jar
EXPOSE 10088
ENTRYPOINT ["java","-Xms512m","-Xmx512m","-jar","-Duser.timezone=America/Phoenix","/msct.jar","--spring.profiles.active=test"]
