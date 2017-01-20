FROM registry.ng.bluemix.net/ibmliberty:latest

EXPOSE 9080

ENV CONNECTOR_HOME /opt/ibm/wlp/

COPY application.properties ${CONNECTOR_HOME}

COPY ./nokia-iot-connector.war /opt/ibm/wlp/usr/servers/defaultServer/dropins/

ENV LICENSE accept