FROM nginx:stable-alpine
MAINTAINER Christophe Furmaniak <christophe.furmaniak@gmail.com>

LABEL DOCKERFILE_PATH=/etc/dockerfile/Dockerfile

RUN mkdir -p /usr/local/appli/config && \
    apk add --no-cache wget && \
    wget --no-check-certificate -O /usr/local/bin/confd https://github.com/kelseyhightower/confd/releases/download/v0.11.0/confd-0.11.0-linux-amd64 && \
    chmod +x /usr/local/bin/confd

ENV AB_OF= \
    JAVA_APP_DIR=/usr/local/appli \
    JAVA_APP_NAME=guestbook-rest-service-server \
    LOOZ_VERSION=v0.5.1 \
    CONFD_ENABLED=true

RUN mkdir -p /etc/nginx/html/config
COPY src/main/config/nginx-base.conf /etc/nginx/nginx.conf
COPY src/main/html/* /etc/nginx/html/
COPY Dockerfile /etc/dockerfile/Dockerfile
COPY src/main/confd /etc/confd
COPY src/main/scripts/startup.sh /usr/local/bin/

RUN  chmod +x /usr/local/bin/confd /usr/local/bin/startup.sh
VOLUME /etc/nginx/html

CMD "/usr/local/bin/startup.sh"
