# Build
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
# Injectează variabilele VITE_ din build (sau setează .env înainte de build)
RUN npm run build

# Serve static cu nginx
FROM nginx:1.27-alpine
COPY --from=build /app/dist /usr/share/nginx/html
# SPA history fallback
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
