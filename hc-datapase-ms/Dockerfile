FROM java:8
ADD hc-datapase-ms.jar datapase.jar
EXPOSE 10613
ENTRYPOINT ["java","-Xms1024m","-Xmx1024m","-jar","-Duser.timezone=GMT+8","/datapase.jar","--spring.profiles.active=test"]
