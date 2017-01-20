FROM registry.ng.bluemix.net/ibmliberty:latest

COPY application.properties /opt/ibm/wlp/

COPY ./nokia-iot-connector.war /opt/ibm/wlp/usr/servers/defaultServer/dropins/

ENV LICENSE accept