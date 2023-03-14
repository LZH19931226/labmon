FROM java:8
ADD HC-TCP-ms.jar tcp.jar
EXPOSE 10615
ENTRYPOINT ["java","-Xms1024m","-Xmx1024m","-jar","-Duser.timezone=GMT+8","/tcp.jar","--spring.profiles.active=test"]
