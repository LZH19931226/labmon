FROM openjdk:8
ADD hc-labredis-ms.jar labredis.jar
EXPOSE 10619
ENTRYPOINT ["java","-Xms256m","-Xmx256m","-jar","-Duser.timezone=America/Phoenix","/labredis.jar","--spring.profiles.active=test"]
