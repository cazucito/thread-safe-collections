# Makefile para thread-safe-collections
# Proporciona targets comunes para desarrollo y demostración

.PHONY: help test demo demo-single clean validate env compile check

# Detectar Maven (wrapper o instalación del sistema)
MVN := $(shell if [ -f ./mvnw ]; then echo "./mvnw"; else echo "mvn"; fi)

# Default target
.DEFAULT_GOAL := help

## Mostrar esta ayuda
help:
	@echo "thread-safe-collections - Makefile"
	@echo ""
	@echo "Targets disponibles:"
	@echo "  make test          - Ejecutar todos los tests con Maven"
	@echo "  make demo          - Ejecutar todas las demos"
	@echo "  make demo-single DEMO=<id>  - Ejecutar una demo específica"
	@echo "  make compile       - Compilar el proyecto"
	@echo "  make clean         - Limpiar archivos compilados"
	@echo "  make validate      - Validar entorno de desarrollo"
	@echo "  make env           - Configurar entorno con SDKMAN"
	@echo "  make check         - Ejecutar tests y verificación de cobertura"
	@echo ""
	@echo "Ejemplos:"
	@echo "  make demo-single DEMO=basic-collection"
	@echo "  make demo-single DEMO=concurrent-hash-map"

## Validar entorno (Java 17+, Maven)
validate:
	@./validate-env.sh

## Configurar entorno con SDKMAN
env:
	@if command -v sdk >/dev/null 2>&1; then \
		sdk env; \
	else \
		echo "SDKMAN no encontrado. Instala desde: https://sdkman.io/install"; \
		exit 1; \
	fi

## Compilar el proyecto
compile:
	$(MVN) compile

## Ejecutar todos los tests
test:
	$(MVN) test

## Ejecutar tests con cobertura y verificación
check:
	$(MVN) clean verify

## Ejecutar todas las demos
demo:
	$(MVN) exec:java -Dexec.args="--fast"

## Ejecutar una demo específica (usar: make demo-single DEMO=<id>)
demo-single:
ifndef DEMO
	@echo "❌ Error: Debes especificar DEMO=<id>"
	@echo ""
	@echo "Ejemplos:"
	@echo "  make demo-single DEMO=basic-collection"
	@echo "  make demo-single DEMO=concurrent-hash-map"
	@echo "  make demo-single DEMO=copy-on-write-array-list"
	@echo ""
	@echo "Para ver todas las demos disponibles:"
	@echo "  $(MVN) exec:java -Dexec.args='--list'"
	@exit 1
endif
	$(MVN) exec:java -Dexec.args="--fast --demo $(DEMO)"

## Limpiar archivos compilados
clean:
	$(MVN) clean

## Listar todas las demos disponibles
list:
	$(MVN) exec:java -Dexec.args="--list"

## Ejecutar en modo debug
debug:
	$(MVN) exec:java -Dexec.args="--debug"

## Ejecutar en formato JSON
demo-json:
	$(MVN) exec:java -Dexec.args="--fast --format json"
