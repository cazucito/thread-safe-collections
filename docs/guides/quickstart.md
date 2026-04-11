# Guía Rápida

Esta guía te llevará de cero a ejecutar tu primera demo en menos de 5 minutos.

## Requisitos Previos

- **Java 17+** (recomendado: Java 21 LTS)
- **Maven 3.9+** o usar el Maven Wrapper incluido

## Paso 1: Validar tu Entorno

```bash
./mvnw validate -Pcheck-env
```

Si no tienes Java 17+, el validador te mostrará instrucciones para instalarlo vía SDKMAN.

## Paso 2: Compilar el Proyecto

```bash
./mvnw clean compile
```

## Paso 3: Ejecutar tu Primera Demo

=== "Todas las demos"

    ```bash
    ./mvnw exec:java
    ```

=== "Demo específica"

    ```bash
    ./mvnw compile exec:java -Pdemo -Ddemo.id=basic-collection
    ```

=== "Con salida JSON"

    ```bash
    ./mvnw compile exec:java -Pdemo -Ddemo.id=concurrent-hash-map --json
    ```

## Paso 4: Ejecutar Tests

```bash
./mvnw test
```

Verás 198 tests ejecutándose con cobertura del 89.8%.

## Comandos Útiles

| Comando | Descripción |
|---------|-------------|
| `./mvnw validate -Pcheck-env` | Validar entorno de desarrollo |
| `./mvnw test` | Ejecutar tests unitarios |
| `./mvnw clean verify` | Tests + cobertura + verificaciones |
| `./mvnw exec:java -Dexec.args="--list"` | Listar demos disponibles |
| `./mvnw compile exec:java -Pnew-demo` | Crear nueva demo |
| `./mvnw jmh:benchmark` | Ejecutar benchmarks JMH |

## ¿Qué sigue?

- Explora las [demos disponibles](../demos/)
- Lee la [guía de decisión](../guides/decision-guide.md) para elegir colecciones
- Revisa los [benchmarks](../benchmarks/) para comparar rendimiento

---

¿Problemas? Consulta la [guía de instalación detallada](installation.md).
