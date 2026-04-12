# 🔒 Thread-Safe Collections

[![Java](https://img.shields.io/badge/Java-17%2B-blue?logo=openjdk)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9%2B-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![Tests](https://img.shields.io/badge/Tests-198%20passing-success)](https://github.com/cazucito/thread-safe-collections/actions)
[![Coverage](https://img.shields.io/badge/Coverage-89.8%25-success)](https://github.com/cazucito/thread-safe-collections)
[![License](https://img.shields.io/badge/License-GPL%20v3.0-blue)](LICENSE)

> **PoCS** sobre colecciones seguras en ambientes multihilo con Java. Aprende mediante demostraciones interactivas, benchmarks y guías prácticas.

[![Docs](https://img.shields.io/badge/Documentation-GitHub%20Pages-222?logo=github)](https://cazucito.github.io/thread-safe-collections/)

## ✨ Características

- 📚 **9 Demos Interactivas** - Desde ArrayList básico hasta LongAdder
- ⚡ **Benchmarks JMH** - Mediciones científicas de rendimiento
- 🎯 **Guía de Decisión** - Árbol de decisión para elegir colecciones
- ✅ **89.8% Cobertura** - Tests unitarios e integración
- 🚀 **Multiplataforma** - Windows, Linux, macOS
- 📖 **Documentación** - Sitio profesional en GitHub Pages

## 🚀 Comienza en 30 segundos

```bash
# Clonar
git clone https://github.com/cazucito/thread-safe-collections.git
cd thread-safe-collections

# Validar entorno
./mvnw validate -Pcheck-env

# Ejecutar todas las demos
./mvnw exec:java --fast

# O una demo específica
./mvnw compile exec:java -Pdemo -Ddemo.id=concurrent-hash-map
```

## 📊 Demos Disponibles

| Colección | Tipo | Característica Principal |
|-----------|------|------------------------|
| `ArrayList` | Lista | Sincronización manual vs automática |
| `CopyOnWriteArrayList` | Lista | Snapshot iteration sin bloqueos |
| `CopyOnWriteArraySet` | Set | Sin duplicados, lecturas rápidas |
| `ConcurrentHashMap` | Mapa | Lock por segmento (default choice) |
| `ConcurrentSkipListMap` | Mapa | Ordenado concurrente O(log n) |
| `ConcurrentSkipListSet` | Set | Ordenado sin duplicados |
| `ConcurrentLinkedQueue` | Cola | Lock-free FIFO |
| `ArrayBlockingQueue` | Cola | Productor/consumidor con límite |
| `LongAdder` | Contador | Striped counters para alta contención |

## 🎯 Comandos Útiles

```bash
# Tests (198 tests, 89.8% cobertura)
./mvnw test

# Listar demos
./mvnw exec:java -Dexec.args="--list"

# Ejecutar demo con salida JSON
./mvnw exec:java -Dexec.args="--format json --demo concurrent-hash-map"

# Benchmarks JMH
./mvnw jmh:benchmark

# Crear nueva demo
./mvnw compile exec:java -Pnew-demo
```

## 📚 Documentación

- 📖 **[Documentación Completa](https://cazucito.github.io/thread-safe-collections/)** - Guías, demos y benchmarks
- 🎯 **[Guía de Decisión](https://cazucito.github.io/thread-safe-collections/guides/decision-guide/)** - ¿Qué colección usar?
- 📊 **[Benchmarks](https://cazucito.github.io/thread-safe-collections/benchmarks/)** - Resultados de rendimiento

## 🏗️ Arquitectura

```
thread-safe-collections/
├── src/main/java/
│   └── io/github/cazucito/threadsafecollections/
│       ├── demo/                    # 9 implementaciones de Demo
│       ├── cli/                     # CLI y herramientas
│       ├── concurrency/             # Utilidades de concurrencia
│       └── format/                  # Formateadores (JSON/Console)
├── src/test/java/
│   └── benchmark/                   # Benchmarks JMH
├── docs/                            # Documentación MkDocs
└── .github/workflows/               # CI/CD (GitHub Actions)
```

## 🎓 Para Estudiantes

**Ruta de aprendizaje sugerida:**

1. **`basic-collection`** - Observa el problema de concurrencia
2. **`copy-on-write-array-list`** - Entiende snapshot iteration
3. **`concurrent-hash-map`** - El estándar de facto para mapas
4. **`concurrent-linked-queue`** - Estructuras lock-free
5. **`long-adder`** - Contadores de alta frecuencia

## 🔬 Benchmarks

Comparaciones de rendimiento con JMH (Java Microbenchmark Harness):

- **MapBenchmark** - HashMap vs ConcurrentHashMap
- **ListBenchmark** - ArrayList vs CopyOnWriteArrayList
- **QueueBenchmark** - Colas concurrentes
- **CounterBenchmark** - AtomicLong vs LongAdder

Ejecutar: `./mvnw jmh:benchmark`

## 🛠️ Desarrollo

### Requisitos
- Java 17+ (recomendado 21 LTS)
- Maven 3.9+ (o usar Maven Wrapper incluido)

### Validar entorno
```bash
./mvnw validate -Pcheck-env
```

### Compilar y testear
```bash
./mvnw clean verify
```

## 📝 Convenciones

- Código en **inglés**
- Documentación en **español**
- Commits con [Conventional Commits](https://www.conventionalcommits.org/)
- Build reproducible con Maven Wrapper

## 🤝 Contribuir

¡Las contribuciones son bienvenidas! Lee [CONTRIBUCION.md](CONTRIBUCION.md) para empezar.

## 📄 Licencia

[GNU General Public License v3.0](LICENSE)

---

<p align="center">
  <a href="https://cazucito.github.io/thread-safe-collections/">📖 Documentación</a> •
  <a href="https://github.com/cazucito/thread-safe-collections/issues">🐛 Reportar Issues</a> •
  <a href="https://github.com/cazucito/thread-safe-collections/discussions">💬 Discussions</a>
</p>
