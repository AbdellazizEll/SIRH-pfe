FROM nginx:alpine

# Copy your custom nginx.conf
COPY nginx.conf /etc/nginx/nginx.conf

WORKDIR /usr/share/nginx/html

# Remove default Nginx website
RUN rm -rf ./*

# Copy built Angular app from Jenkins workspace (already built by npm run build)
COPY dist/pfe-front/ .

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
