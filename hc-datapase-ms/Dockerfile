FROM openjdk:8
ADD hc-datapase-ms.jar datapase.jar
EXPOSE 10688
ENTRYPOINT ["java","-Xmx512m","-Xms512m","-jar","-Duser.timezone=America/Phoenix","/datapase.jar","--spring.profiles.active=test"]
