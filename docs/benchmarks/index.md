# Benchmarks

Esta sección documenta los benchmarks de rendimiento ejecutados con [Java Microbenchmark Harness (JMH)](https://github.com/openjdk/jmh).

## 🎯 Propósito

Los benchmarks permiten comparar el rendimiento real de diferentes implementaciones de colecciones concurrentes bajo diversas condiciones de carga.

## 🚀 Cómo ejecutar

```bash
# Ejecutar todos los benchmarks
./mvnw jmh:benchmark

# Ejecutar benchmark específico
./mvnw jmh:benchmark -Djmh.benchmarks=MapBenchmark

# Guardar resultados
./mvnw jmh:benchmark -Djmh.output=json > results.json
```

## 📊 Benchmarks disponibles

### MapBenchmark

Compara rendimiento de:
- `HashMap` (no thread-safe)
- `Collections.synchronizedMap(new HashMap())`
- `ConcurrentHashMap`

**Operaciones medidas:**
- `put`: Inserciones concurrentes
- `get`: Lecturas concurrentes
- `mixedReadWrite`: 80% lecturas, 20% escrituras

**Parámetros:**
- Threads: 1, 2, 4, 8

### ListBenchmark

Compara rendimiento de:
- `ArrayList` (no thread-safe)
- `Collections.synchronizedList(new ArrayList())`
- `CopyOnWriteArrayList`

**Operaciones medidas:**
- `add`: Inserciones
- `get`: Acceso por índice
- `iterate`: Recorrido completo

### QueueBenchmark

Compara rendimiento de:
- `LinkedList` (no thread-safe)
- `ConcurrentLinkedQueue`
- `ArrayBlockingQueue`

**Operaciones medidas:**
- `offer`: Agregar elementos
- `poll`: Remover elementos
- `producerConsumer`: Patrón productor/consumidor

### CounterBenchmark

Compara rendimiento de:
- `AtomicLong`
- `LongAdder`

**Operaciones medidas:**
- `increment`: Incremento bajo alta contención
- `sum`: Lectura del valor total

## 📈 Interpretación de resultados

Los resultados se muestran en **operaciones por milisegundo** (ops/ms). Mayor es mejor.

```
Benchmark                        (threadCount)   Mode  Cnt   Score   Error   Units
MapBenchmark.concurrentHashMap              1  thrpt   10  1234.5 ±  12.3  ops/ms
MapBenchmark.concurrentHashMap              4  thrpt   10  4567.8 ±  45.6  ops/ms
```

### Métricas clave

- **Score**: Throughput promedio
- **Error**: Intervalo de confianza (95%)
- **Units**: ops/ms (operaciones por milisegundo)

## 🎓 Lecciones aprendidas

### 1. Contención importa

`ConcurrentHashMap` escala mejor con más threads porque usa locks por segmento, no lock global.

### 2. CopyOnWrite es para lecturas

`CopyOnWriteArrayList` es excelente para lecturas frecuentes pero muy lento para escrituras (copia completa).

### 3. LongAdder > AtomicLong bajo contención

`LongAdder` usa striped counters que reducen la contención de CAS (Compare-And-Swap).

### 4. Lock-free != Siempre mejor

`ConcurrentLinkedQueue` es lock-free pero `ArrayBlockingQueue` puede ser mejor si necesitas back-pressure.

## 📚 Recursos adicionales

- [JMH Samples](https://github.com/openjdk/jmh/tree/master/jmh-samples) - Ejemplos oficiales
- [Java Performance: The Definitive Guide](https://www.oreilly.com/library/view/java-performance-the/9781449363512/) - Scott Oaks

---

*Los benchmarks se ejecutan automáticamente en CI con cada push a master. Los resultados se guardan como artifacts.*
