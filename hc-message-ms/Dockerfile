FROM openjdk:8
ADD hc-message-ms.jar message.jar
EXPOSE 10609
ENTRYPOINT ["java","-Xms1024m","-Xmx1024m","-jar","-Duser.timezone=GMT+8","/message.jar","--spring.profiles.active=test"]
