FROM java:8
ADD HC-TCP-ms.jar tcp.jar
EXPOSE 10619
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+8","/tcp.jar","--spring.profiles.active=test"]
