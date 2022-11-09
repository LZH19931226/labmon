FROM java:8
ADD HC-MSWK-ms.jar mswk.jar
EXPOSE 10617
ENTRYPOINT ["java","-Xms512m","-Xmx1g","-jar","-Duser.timezone=GMT+8","/mswk.jar","--spring.profiles.active=test"]
