FROM java:8
ADD HC-datapse-ms.jar datapse.jar
EXPOSE 10613
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+8","/mswk.jar","--spring.profiles.active=test"]
