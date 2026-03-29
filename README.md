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
mvn compile
```

Ejecutar la aplicación:

```bash
mvn exec:java
```

Ejecutar las pruebas:

```bash
mvn test
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

## Contribución

La guía de contribución se encuentra en [CONTRIBUCION.md](CONTRIBUCION.md).

## Licencia

Este proyecto se distribuye bajo la licencia GNU General Public License v3.0. Consulta [LICENSE](LICENSE) para más detalles.
