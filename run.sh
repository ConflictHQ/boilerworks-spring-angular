#!/usr/bin/env bash
set -euo pipefail

# Boilerworks — Spring Boot + Angular
# Usage: ./run.sh [command]

COMPOSE_FILE=""

if [ -f "docker-compose.yml" ]; then
    COMPOSE_FILE="docker-compose.yml"
elif [ -f "docker-compose.yaml" ]; then
    COMPOSE_FILE="docker-compose.yaml"
elif [ -f "docker/docker-compose.yml" ]; then
    COMPOSE_FILE="docker/docker-compose.yml"
elif [ -f "docker/docker-compose.yaml" ]; then
    COMPOSE_FILE="docker/docker-compose.yaml"
fi

compose() {
    if [ -n "$COMPOSE_FILE" ]; then
        docker compose -f "$COMPOSE_FILE" "$@"
    else
        echo "No docker-compose file found"
        exit 1
    fi
}

case "${1:-help}" in
    up|start)
        compose up -d --build
        echo ""
        echo "Services starting. Check status with: ./run.sh status"
        ;;
    down|stop)
        compose down
        ;;
    restart)
        compose down
        compose up -d --build
        ;;
    status|ps)
        compose ps
        ;;
    logs)
        compose logs -f "${2:-}"
        ;;
    seed)
        echo "No seed command configured for this template"
        ;;
    test)
        cd backend && ./gradlew test
        cd ../frontend && npx ng test --watch=false --browsers=ChromeHeadless
        ;;
    lint)
        cd frontend && npx ng lint
        ;;
    shell)
        compose exec api sh
        ;;
    build)
        cd backend && ./gradlew bootJar
        cd ../frontend && npx ng build --configuration=production
        ;;
    help|*)
        echo "Usage: ./run.sh <command>"
        echo ""
        echo "Commands:"
        echo "  up, start     Start all services"
        echo "  down, stop    Stop all services"
        echo "  restart       Restart all services"
        echo "  status, ps    Show service status"
        echo "  logs [svc]    Tail logs (optionally for one service)"
        echo "  seed          Seed the database"
        echo "  test          Run tests (backend + frontend)"
        echo "  lint          Run linters"
        echo "  shell         Open a shell in the API container"
        echo "  build         Build backend JAR + frontend"
        echo "  help          Show this help"
        ;;
esac
