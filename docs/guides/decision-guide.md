# Guía de Decisión: Colecciones Concurrentes en Java

Esta guía te ayuda a elegir la colección concurrente adecuada para tu caso de uso.

---

## 📊 Tabla Comparativa Rápida

| Colección | Tipo | Ordenada | Bloqueante | Mejor para |
|-----------|------|----------|------------|------------|
| `ArrayList` + `synchronized` | Lista | No | N/A | Legacy, evitar si es posible |
| `CopyOnWriteArrayList` | Lista | No | No | Lecturas frecuentes, pocas escrituras |
| `CopyOnWriteArraySet` | Set | No | No | Lecturas frecuentes, sin duplicados |
| `ConcurrentHashMap` | Mapa | No | No | Mapas de alto rendimiento (default choice) |
| `ConcurrentSkipListMap` | Mapa | Sí | No | Mapas ordenados concurrentes |
| `ConcurrentSkipListSet` | Set | Sí | No | Sets ordenados sin duplicados |
| `ConcurrentLinkedQueue` | Cola | FIFO | No | Colas FIFO no bloqueantes |
| `ArrayBlockingQueue` | Cola | FIFO | Sí | Productor/consumidor con límite |
| `LinkedBlockingQueue` | Cola | FIFO | Sí | Productor/consumidor sin límite definido |

---

## 🌳 Árbol de Decisión

```
¿Necesitas una lista (índice/posición)?
├── SÍ → ¿Muchas lecturas y pocas escrituras?
│   ├── SÍ → CopyOnWriteArrayList
│   └── NO → Reconsidera (¿Quizás un Mapa?)
│
└── NO → ¿Necesitas una cola (FIFO)?
    ├── SÍ → ¿Necesitas bloquear cuando esté llena/vacía?
    │   ├── SÍ → ArrayBlockingQueue (con límite) o LinkedBlockingQueue
    │   └── NO → ConcurrentLinkedQueue
    │
    └── NO → ¿Necesitas un Set (sin duplicados)?
        ├── SÍ → ¿Necesitas ordenación natural?
        │   ├── SÍ → ConcurrentSkipListSet
        │   └── NO → CopyOnWriteArraySet
        │
        └── NO → ¿Necesitas un Mapa (clave-valor)?
            ├── SÍ → ¿Necesitas ordenación por clave?
            │   ├── SÍ → ConcurrentSkipListMap
            │   └── NO → ConcurrentHashMap (default)
            │
            └── NO → Considera colecciones especializadas o Atomic variables
```

---

## 📈 Por Categoría de Uso

### Lectura Intensiva + Escrituras Esporádicas
**Mejor opción:** `CopyOnWriteArrayList` / `CopyOnWriteArraySet`

- ✅ Sin bloqueos en lectura (snapshot iteration)
- ✅ Thread-safe por diseño
- ❌ Costoso en escritura (copia completa)
- 🎯 Ideal: Configuraciones, listeners, suscriptores

### Mapa de Alto Rendimiento General
**Mejor opción:** `ConcurrentHashMap`

- ✅ Lock por segmento (no bloqueo global)
- ✅ Operaciones atómicas: putIfAbsent, compute, merge
- ✅ Excelente throughput concurrente
- ❌ No ordenado
- 🎯 Default choice para mapas concurrentes

### Mapa Ordenado Concurrente
**Mejor opción:** `ConcurrentSkipListMap`

- ✅ Ordenación por clave (Comparable o Comparator)
- ✅ O(log n) operaciones
- ✅ Submapas concurrentes (headMap, tailMap)
- ❌ Más overhead que CHM
- 🎯 Rangos, ordenación, navegación

### Cola No Bloqueante
**Mejor opción:** `ConcurrentLinkedQueue`

- ✅ Lock-free (algoritmo de Michael-Scott)
- ✅ No bloquea threads
- ✅ Weakly consistent iterator
- ❌ No limita tamaño
- ❌ No soporta null
- 🎯 Trabajos en background, pipelines

### Productor/Consumidor
**Mejor opción:** `ArrayBlockingQueue` (con límite) o `LinkedBlockingQueue`

- ✅ Back-pressure integrado
- ✅ put() / take() bloqueantes
- ✅ Timeout configurable
- 🎯 Pipelines, thread pools, batch processing

### Contadores Concurrentes
**Mejor opción:** `LongAdder` (contadores) / `DoubleAdder`

- ✅ Stripe de contadores (reduce CAS contention)
- ✅ Mucho mejor throughput que AtomicLong bajo alta contención
- ❌ Mayor consumo de memoria
- ❌ sum() puede no reflejar valor exacto en momento de lectura
- 🎯 Métricas, estadísticas, contadores de alta frecuencia

---

## ⚠️ Cuándo NO Usar

| Colección | NO usar cuando... | Alternativa |
|-----------|-------------------|-------------|
| `CopyOnWriteArrayList` | Escrituras frecuentes | `Collections.synchronizedList` o reconsidera diseño |
| `ConcurrentHashMap` | Necesitas ordenación | `ConcurrentSkipListMap` |
| `ConcurrentLinkedQueue` | Necesitas tamaño exacto frecuentemente | `LinkedBlockingQueue` |
| `ArrayBlockingQueue` | No conoces capacidad máxima | `LinkedBlockingQueue` |
| `LongAdder` | Necesitas lectura consistente siempre | `AtomicLong` |
| Cualquier colección concurrente | Acceso single-thread | Colección no concurrente (mejor rendimiento) |

---

## 🔢 Complejidad Temporal

| Operación | CHM | CSLM | CLQ | ABQ |
|-----------|-----|------|-----|-----|
| get | O(1) | O(log n) | O(n)* | O(1) |
| put/add | O(1) | O(log n) | O(1) | O(1) |
| remove | O(1) | O(log n) | O(1) | O(1) |
| contains | O(1) | O(log n) | O(n) | O(n) |
| size() | O(n) | O(1) | O(n) | O(1) |

*CLQ: búsqueda es O(n) pero inserción/espera es O(1) con operaciones CAS

---

## 💡 Patrones Comunes

### 1. Caché Concurrente
```java
ConcurrentHashMap<Key, Value> cache = new ConcurrentHashMap<>();

// ComputeIfAbsent es atómico
Value value = cache.computeIfAbsent(key, k -> loadFromDatabase(k));
```

### 2. Contadores de Frecuencia
```java
ConcurrentHashMap<String, LongAdder> frequencies = new ConcurrentHashMap<>();

// Incremento thread-safe
frequencies.computeIfAbsent(word, k -> new LongAdder()).increment();
```

### 3. Work Queue
```java
BlockingQueue<WorkItem> queue = new ArrayBlockingQueue<>(1000);

// Productor
queue.put(item); // Bloquea si llena

// Consumidor
WorkItem item = queue.take(); // Bloquea si vacía
```

### 4. Event Listeners
```java
CopyOnWriteArrayList<EventListener> listeners = new CopyOnWriteArrayList<>();

// Registro (copia interna)
listeners.add(listener);

// Notificación (itera snapshot sin bloqueos)
for (EventListener l : listeners) {
    l.onEvent(event);
}
```

---

## 🎯 Checklist de Decisión

Antes de elegir una colección, responde:

- [ ] ¿Es concurrente el acceso? (Si no, usa HashMap/ArrayList normales)
- [ ] ¿Qué operaciones predominan? (lectura/escritura/mezcla)
- [ ] ¿Necesitas ordenación?
- [ ] ¿Necesitas tamaño limitado?
- [ ] ¿Necesitas bloqueo cuando vacío/lleno?
- [ ] ¿Puedes tolerar iteradores "weakly consistent"?
- [ ] ¿Es crítico el uso de memoria?

---

## 📚 Referencias

- [Java Collections Framework](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/)
- [Java Concurrency in Practice](https://jcip.net/) - Brian Goetz
- [Concurrent Programming in Java](https://www.amazon.com/Concurrent-Programming-Java-Design-Principles/dp/0201310090) - Doug Lea

---

*Documento generado para thread-safe-collections v1.0.0*
