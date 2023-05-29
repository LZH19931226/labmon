FROM java:8
ADD hc-labredis-ms.jar labredis.jar
EXPOSE 10619
ENTRYPOINT ["java","-Xms512m","-Xmx512m","-jar","-Duser.timezone=GMT+8","/labredis.jar","--spring.profiles.active=test"]
