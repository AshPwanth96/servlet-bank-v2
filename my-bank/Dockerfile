FROM tomcat:9-jdk8
RUN rm -rf /usr/local/tomcat/webapps/*
COPY target/my-bank.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]