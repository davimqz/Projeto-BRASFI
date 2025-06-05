#!/bin/bash

# Define o diretório onde os logs serão salvos
LOG_DIR="./logs"
mkdir -p "$LOG_DIR" # Cria o diretório se não existir

# Nome dos arquivos de log
COMPOSE_LOG="$LOG_DIR/docker_compose_up.log"
FRONTEND_LOG="$LOG_DIR/frontend_service.log"
BACKEND_LOG="$LOG_DIR/backend_service.log"
DB_LOG="$LOG_DIR/db_service.log"
NGINX_LOG="$LOG_DIR/nginx_service.log"

echo "Iniciando docker-compose up --build -d..."
echo "O log completo desta operação será salvo em: $COMPOSE_LOG"
docker-compose up --build -d > "$COMPOSE_LOG" 2>&1

# Verificar se o docker-compose up foi bem-sucedido (opcional, mas bom para feedback)
if [ $? -eq 0 ]; then
  echo "Docker services iniciados (ou já estavam rodando) em background."
else
  echo "Houve um problema ao tentar iniciar os serviços com docker-compose up."
  echo "Verifique $COMPOSE_LOG para detalhes."
  # Mesmo com falha no 'up', tentamos pegar logs que possam existir
fi

echo ""
echo "Capturando logs dos serviços para arquivos individuais em $LOG_DIR/ ..."

# Usamos --no-color para evitar caracteres de escape de cor nos arquivos de log
# e 2>&1 para capturar stdout e stderr.
docker-compose logs --no-color frontend > "$FRONTEND_LOG" 2>&1
docker-compose logs --no-color backend > "$BACKEND_LOG" 2>&1
docker-compose logs --no-color db > "$DB_LOG" 2>&1
docker-compose logs --no-color nginx > "$NGINX_LOG" 2>&1

echo ""
echo "Logs capturados:"
echo "  - Build e Inicialização: $COMPOSE_LOG"
echo "  - Serviço Frontend:      $FRONTEND_LOG"
echo "  - Serviço Backend:       $BACKEND_LOG"
echo "  - Serviço DB:            $DB_LOG"
echo "  - Serviço Nginx:         $NGINX_LOG"
echo ""
echo "Para ver os logs dos serviços em tempo real (após executar este script), você pode usar:"
echo "  docker-compose logs -f frontend"
echo "  docker-compose logs -f backend"
echo "  (e assim por diante para outros serviços)"
echo ""
echo "Para parar todos os serviços, execute: docker-compose down" 