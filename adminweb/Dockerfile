# Stage 0, "build-stage", based on Node.js, to build and compile the frontend
FROM node:20 as build-stage
WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY angular.json tsconfig.* /app/
COPY src/ /app/src
ARG configuration=production
RUN pwd && ls -ail && npm run build -- --output-path=./dist/out --configuration $configuration

# Stage 1, based on Nginx, to have only the compiled app, ready for production with Nginx
FROM --platform=linux/amd64 nginx:1.25
#Copy ci-dashboard-dist
COPY --from=build-stage /app/dist/out/ /usr/share/nginx/html
#Copy default nginx configuration
COPY ./scripts/nginx-custom.conf /etc/nginx/conf.d/default.conf