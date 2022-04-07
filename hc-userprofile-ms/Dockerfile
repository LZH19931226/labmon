FROM java:8
ADD hc-user-ms.jar user.jar
EXPOSE 10610
ENTRYPOINT ["java","-Xms200m","-Xmx250m","-jar","-Duser.timezone=GMT+8","/user.jar","--spring.profiles.active=cloud"]
