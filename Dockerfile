FROM node:erbium-alpine3.12
WORKDIR /opt/app
COPY gate-simulator/ .
CMD ["npm", "start"]
EXPOSE 9999
