FROM maven:3.8.4-jdk-11
ADD . /usr/src/portuiu.com
WORKDIR /usr/src/portuiu.com

RUN mvn clean install