FROM java:8
ADD HC-MSCT-ms.jar msct.jar
EXPOSE 10088
ENTRYPOINT ["java","-Xms200m","-Xmx250m","-jar","-Duser.timezone=GMT+8","/msct.jar","--spring.profiles.active=test"]
