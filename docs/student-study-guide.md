# Guía de estudio para estudiantes

Esta guía propone una ruta breve para empezar a estudiar el proyecto sin perderse entre las demos.

## Antes de ejecutar

- Confirmar que el equipo tenga JDK 17 o superior.
- Ejecutar `./mvnw test` para validar que el entorno está listo.
- Usar `--fast` durante la primera exploración para reducir esperas.

## Secuencia sugerida

### 1. Problema base

Ejecutar:

```bash
./mvnw exec:java -Dexec.args="--fast --demo basic-collection"
```

Preguntas guía:

- ¿Qué cambia entre `ArrayList` no sincronizado y el sincronizado?
- ¿Por qué el tamaño final puede ser incorrecto sin protección?

### 2. Colecciones copy-on-write

Ejecutar:

```bash
./mvnw exec:java -Dexec.args="--fast --demo copy-on-write-array-list"
./mvnw exec:java -Dexec.args="--fast --demo copy-on-write-array-set"
```

Preguntas guía:

- ¿Qué significa que el iterador trabaje sobre un snapshot?
- ¿Qué costo tiene este enfoque cuando hay muchas escrituras?

### 3. Colecciones ordenadas concurrentes

Ejecutar:

```bash
./mvnw exec:java -Dexec.args="--fast --demo concurrent-skip-list-set"
./mvnw exec:java -Dexec.args="--fast --demo concurrent-skip-list-map"
```

Preguntas guía:

- ¿Qué ventaja aporta mantener el orden en un contexto concurrente?
- ¿Qué diferencia observable hay frente a `TreeSet` y `TreeMap`?

### 4. Mapa concurrente de uso general

Ejecutar:

```bash
./mvnw exec:java -Dexec.args="--fast --demo concurrent-hash-map"
```

Preguntas guía:

- ¿Por qué `ConcurrentHashMap` suele aparecer más en aplicaciones reales?
- ¿Qué cambia respecto a `HashMap` durante el recorrido concurrente?

## Sugerencia para clase

- Primera pasada: usar `--fast`.
- Segunda pasada: repetir una o dos demos con `--debug`.
- Cierre: pedir a cada estudiante que explique cuál estructura elegiría y por qué.
