FROM java:8
ADD HC-MSWK-ms.jar mswk.jar
EXPOSE 10619
ENTRYPOINT ["java","-Xms200m","-Xmx250m","-jar","-Duser.timezone=GMT+8","/mswk.jar","--spring.profiles.active=test"]
