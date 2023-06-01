FROM openjdk:8
ADD hc-message-ms.jar message.jar
EXPOSE 10609
ENTRYPOINT ["java","-Xms256m","-Xmx256m","-jar","-Duser.timezone=GMT+8","/message.jar","--spring.profiles.active=test"]
