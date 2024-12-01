#FROM node:16-alpine AS build
#WORKDIR /project/frontend
#COPY package.json ./
#COPY package-lock.json ./
#RUN npm install
#COPY . .
#RUN npm run build
#
#FROM nginx
#COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf
#COPY --from=build /project/frontend/build  /usr/share/nginx/html

# FROM node:16-alpine
# WORKDIR /project/frontend
# COPY package.json ./
# COPY package-lock.json ./
# RUN npm install
# COPY . .
# CMD ["npm", "run","start"]
# ENV CHOKIDAR_USEPOLLING=true

FROM node:20-alpine
WORKDIR /project/frontend
COPY package.json ./
COPY package-lock.json ./
RUN npm install
COPY . .
CMD ["npm", "run", "dev"]
ENV CHOKIDAR_USEPOLLING=true
