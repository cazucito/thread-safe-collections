# Colecciones seguras en ambientes multihilo

PoC educativa en Java para mostrar diferencias entre colecciones tradicionales y colecciones concurrentes cuando varios hilos modifican o recorren datos al mismo tiempo.

## Requisitos

- JDK 17 o superior
- Maven 3.9 o superior

## Estructura del proyecto

```text
src/
  main/
    java/
      io/github/cazucito/threadsafecollections/
  test/
    java/
      io/github/cazucito/threadsafecollections/
```

## Cómo ejecutar

Compilar el proyecto:

```bash
./mvnw compile
```

Ejecutar la aplicación:

```bash
./mvnw exec:java
```

Listar demos disponibles:

```bash
./mvnw exec:java -Dexec.args="--list"
```

Ejecutar una demo puntual:

```bash
./mvnw exec:java -Dexec.args="--demo basic-collection"
```

Ejecutar una demo con depuración:

```bash
./mvnw exec:java -Dexec.args="--debug --demo concurrent-hash-map"
```

Ejecutar las pruebas:

```bash
./mvnw test
```

## Demos incluidas

- Comparación entre `ArrayList` no sincronizado, acceso sincronizado manual y `Collections.synchronizedList(...)`
- `CopyOnWriteArrayList`
- `CopyOnWriteArraySet`
- `ConcurrentSkipListSet`
- `ConcurrentSkipListMap`
- `ConcurrentHashMap`

## Convenciones del repositorio

- Código en inglés
- Comentarios y documentación en español neutro
- Build reproducible con Maven

## Documentación

- Índice técnico: [docs/README.md](docs/README.md)
- Plan de evolución del repositorio: [docs/roadmap/repository-evolution-plan.md](docs/roadmap/repository-evolution-plan.md)

## Contribución

La guía de contribución se encuentra en [CONTRIBUCION.md](CONTRIBUCION.md).

## Licencia

Este proyecto se distribuye bajo la licencia GNU General Public License v3.0. Consulta [LICENSE](LICENSE) para más detalles.
