FROM openjdk:8
ADD hc-datapase-ms.jar datapase.jar
EXPOSE 10613
ENTRYPOINT ["java","-Xmx512m","-Xms512m","-jar","-Duser.timezone=GMT+8","/datapase.jar","--spring.profiles.active=test"]
