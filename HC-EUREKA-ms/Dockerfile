FROM java:8
ADD HC-EUREKA-ms eureka.jar
EXPOSE 10311
ENTRYPOINT ["java","-Xms100m","-Xmx150m","-jar","-Duser.timezone=GMT+8","/eureka.jar","--spring.profiles.active=ali"]
