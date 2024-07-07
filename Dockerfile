
# Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
# Click nbfs://nbhost/SystemFileSystem/Templates/Other/Dockerfile to edit this template

FROM openjdk:24-jdk-oraclelinux8
RUN mkdir app
ADD target/gateway-0.0.1-SNAPSHOT.jar app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app/app.jar"]
CMD ["/bin/sh"]
