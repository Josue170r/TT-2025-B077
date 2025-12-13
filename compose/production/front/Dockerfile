FROM node:20-alpine as builder

WORKDIR /app

COPY front-tt-2025-b077/package*.json ./
RUN npm install

COPY front-tt-2025-b077 ./

ARG VITE_API_BACK
ARG VITE_ACO_API
ARG VITE_GOOGLE_MAP_KEY

RUN npm run build

FROM nginx:alpine

COPY front-tt-2025-b077/nginx.conf /etc/nginx/nginx.conf
COPY front-tt-2025-b077/default.conf /etc/nginx/conf.d/default.conf

COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]