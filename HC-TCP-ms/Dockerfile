FROM openjdk:8
ADD HC-TCP-ms.jar tcp.jar
EXPOSE 10615
ENTRYPOINT ["java","-Xms512m","-Xmx512m","-jar","-Duser.timezone=GMT+8","/tcp.jar","--spring.profiles.active=test"]
