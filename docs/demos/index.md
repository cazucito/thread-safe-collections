# Demos Disponibles

Esta sección contiene 9 demostraciones interactivas de colecciones concurrentes en Java. Cada demo incluye código fuente, explicación pedagógica y ejercicios sugeridos.

## 📚 Índice de Demos

<div class="grid cards" markdown>

-   :material-format-list-bulleted:{ .lg .middle } **Colecciones Básicas**

    ---

    [basic-collection](basic-collection.md) - ArrayList sincronizado vs no sincronizado

-   :material-content-copy:{ .lg .middle } **Copy-on-Write**

    ---

    [copy-on-write-array-list](copy-on-write.md) - Iteración snapshot sin bloqueos
    
    [copy-on-write-array-set](copy-on-write.md) - Set con copy-on-write

-   :material-map:{ .lg .middle } **Mapas Concurrentes**

    ---

    [concurrent-hash-map](concurrent-maps.md) - Mapa lock por segmento
    
    [concurrent-skip-list-map](concurrent-maps.md) - Mapa ordenado concurrente

-   :material-set-center:{ .lg .middle } **Sets Concurrentes**

    ---

    [concurrent-skip-list-set](concurrent-sets.md) - Set ordenado sin duplicados

-   :material-queue:{ .lg .middle } **Colas Concurrentes**

    ---

    [concurrent-linked-queue](queues.md) - Cola lock-free FIFO
    
    [array-blocking-queue](queues.md) - Productor/consumidor con límite

-   :material-counter:{ .lg .middle } **Contadores**

    ---

    [long-adder](counters.md) - Contadores de alta frecuencia

</div>

## 🎯 Cómo ejecutar las demos

```bash
# Listar todas las demos disponibles
./mvnw exec:java -Dexec.args="--list"

# Ejecutar una demo específica
./mvnw compile exec:java -Pdemo -Ddemo.id=concurrent-hash-map

# Ejecutar en modo rápido (para CI)
./mvnw exec:java -Dexec.args="--fast --demo concurrent-hash-map"

# Salida en formato JSON
./mvnw exec:java -Dexec.args="--format json --demo concurrent-hash-map"
```

## 📝 Estructura de cada demo

Cada demo sigue un formato consistente:

1. **Objetivo de aprendizaje** - Qué vas a aprender
2. **Código de demostración** - Implementación ejecutable
3. **Observaciones** - Qué debes notar al ejecutar
4. **Conclusión** - Key takeaway principal
5. **Ejercicios** - Experimentos sugeridos

## 🔄 Ruta de aprendizaje sugerida

Si eres nuevo en colecciones concurrentes, sigue este orden:

1. **Básico**: [basic-collection](basic-collection.md) - Entiende el problema
2. **Listas**: [copy-on-write-array-list](copy-on-write.md) - Snapshot iteration
3. **Mapas**: [concurrent-hash-map](concurrent-maps.md) - El estándar de facto
4. **Colas**: [concurrent-linked-queue](queues.md) - Lock-free structures
5. **Avanzado**: [long-adder](counters.md) - Striped counters

## 📊 Benchmarks

Cada demo tiene benchmarks JMH asociados en la sección [Benchmarks](../benchmarks/).

---

¿Listo para comenzar? Recomendamos empezar con [Colecciones Básicas](basic-collection.md).
