FROM nginx:alpine

COPY resources/nginx.conf /etc/nginx/conf.d/default.conf
WORKDIR /usr/share/nginx/html
COPY dist/ .
