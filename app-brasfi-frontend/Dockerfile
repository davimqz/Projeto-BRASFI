# app-brasfi-frontend/Dockerfile

# ---- Build Stage ----
FROM node:18-alpine AS builder
WORKDIR /app

# Copiar package.json e package-lock.json (ou yarn.lock, pnpm-lock.yaml se aplicável)
COPY package*.json ./

# Instalar dependências
RUN npm install

# Copiar o restante do código fonte da aplicação
COPY . .

# Construir a aplicação para produção (saída em /app/dist)
RUN npm run build

# ---- Runtime Stage ----
FROM nginx:stable-alpine
WORKDIR /usr/share/nginx/html

# Remover o conteúdo padrão do Nginx
RUN rm -rf ./*

# Copiar os arquivos construídos da Build Stage
COPY --from=builder /app/dist .

# Copiar a configuração personalizada do Nginx para produção
# Assumindo que você tem um nginx.conf para produção no diretório do frontend
COPY nginx-prod.conf /etc/nginx/conf.d/default.conf

# Expor a porta 80 (padrão do Nginx)
EXPOSE 80

# O comando padrão da imagem nginx (nginx -g 'daemon off;') será executado. 