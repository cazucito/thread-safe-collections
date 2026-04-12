# Demos Disponibles

Esta sección contiene 9 demostraciones interactivas de colecciones concurrentes en Java. Cada demo incluye código fuente, explicación pedagógica y ejercicios sugeridos.

## 📚 Demos Disponibles

### Colecciones Básicas
- **basic-collection** - ArrayList sincronizado vs no sincronizado

### Copy-on-Write
- **copy-on-write-array-list** - Iteración snapshot sin bloqueos
- **copy-on-write-array-set** - Set con copy-on-write

### Mapas Concurrentes
- **concurrent-hash-map** - Mapa lock por segmento
- **concurrent-skip-list-map** - Mapa ordenado concurrente

### Sets Concurrentes
- **concurrent-skip-list-set** - Set ordenado sin duplicados

### Colas Concurrentes
- **concurrent-linked-queue** - Cola lock-free FIFO
- **array-blocking-queue** - Productor/consumidor con límite

### Contadores
- **long-adder** - Contadores de alta frecuencia

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

1. **Básico**: `basic-collection` - Entiende el problema
2. **Listas**: `copy-on-write-array-list` - Snapshot iteration
3. **Mapas**: `concurrent-hash-map` - El estándar de facto
4. **Colas**: `concurrent-linked-queue` - Lock-free structures
5. **Avanzado**: `long-adder` - Striped counters

## 📊 Benchmarks

Cada demo tiene benchmarks JMH asociados. Ver la sección de Benchmarks.

---

¿Listo para comenzar? Ejecuta `./mvnw exec:java -Dexec.args="--list"` para ver todas las opciones.
