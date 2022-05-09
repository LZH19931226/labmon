FROM java:8
ADD HC-TCP-ms.jar tcp.jar
EXPOSE 10615
ENTRYPOINT ["java","-Xms200m","-Xmx250m","-jar","-Duser.timezone=GMT+8","/tcp.jar","--spring.profiles.active=test"]
