FROM java:8
ADD HC-labredis-ms.jar labredis.jar
EXPOSE 10619
ENTRYPOINT ["java","-Xms200m","-Xmx250m","-jar","-Duser.timezone=GMT+8","/labredis.jar","--spring.profiles.active=test"]
