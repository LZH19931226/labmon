FROM java:8
ADD HC-MQTT-ms.jar mqtt.jar
EXPOSE 10615
ENTRYPOINT ["java","-Xms256m","-Xmx512m","-jar","-Duser.timezone=GMT+8","/mqtt.jar","--spring.profiles.active=test"]
