FROM node:18-alpine

RUN mkdir /app

COPY frontend/package.json /app/package.json

RUN npm install

COPY . /app
RUN npm run build