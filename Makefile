.PHONY: help dev stop test test-backend test-frontend build clean

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

dev: ## Start development infrastructure (Postgres + Redis)
	cd docker && docker compose up -d postgres redis

stop: ## Stop all Docker services
	cd docker && docker compose down

test: test-backend test-frontend ## Run all tests

test-backend: ## Run backend tests
	cd backend && ./gradlew test

test-frontend: ## Run frontend tests
	cd frontend && npx ng test --watch=false --browsers=ChromeHeadless

build: ## Build both backend and frontend
	cd backend && ./gradlew bootJar
	cd frontend && npx ng build --configuration=production

build-docker: ## Build Docker images
	cd docker && docker compose build

up: ## Start full stack via Docker Compose
	cd docker && docker compose up -d --build

clean: ## Clean build artifacts
	cd backend && ./gradlew clean
	cd frontend && rm -rf dist node_modules/.cache
