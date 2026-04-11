# Plan de Mejora 2025
## thread-safe-collections

**Versión:** 1.0  
**Fecha:** 2025-04-11  
**Estándar:** ISO 21500 / Agile con OKRs  
**Estado:** Aprobado para implementación

---

## 1. Resumen Ejecutivo

### 1.1 Visión
Convertir `thread-safe-collections` en la referencia educativa de código abierto más completa para entender colecciones concurrentes en Java, manteniendo su naturaleza de PoC didáctica mientras se profesionaliza su calidad técnica y experiencia de desarrollo.

### 1.2 Objetivos Estratégicos (OKRs)

| Objetivo | Key Results (Q2-Q4 2025) | Métrica |
|----------|-------------------------|---------|
| **Calidad** | Cobertura de tests > 80%, 0 tests flaky | % cobertura, # builds fallidos por flaky tests |
| **Experiencia Developer** | Onboarding < 5 min, setup automático | Tiempo hasta primer `./mvnw test` exitoso |
| **Extensibilidad** | Agregar demo nueva < 30 min, 1 archivo modificado | Tiempo de implementación, # archivos tocados |
| **Comunidad** | Guía de contribución completa, 2+ contribuidores externos | # PRs mergeados de externos |

### 1.3 Alcance (In/Out)

**In Scope:**
- Testing exhaustivo de comportamientos concurrentes
- Refactorización de código común sin perder claridad pedagógica
- Automatización de setup y validación
- Documentación técnica y guías de estudio
- Demos adicionales (Queues, LongAdder)

**Out of Scope:**
- Conversión a librería distribuible (Maven Central)
- Framework de utilerías de concurrencia genérico
- UI gráfica o web
- Soporte para Java < 17

---

## 2. Épicas y Features

### Épica 1: Testing Robusto (TR)
**Objetivo:** Eliminar dependencia de temporización y alcanzar cobertura significativa de comportamientos concurrentes.

#### Feature TR-1.1: Tests Determinísticos de Concurrencia
**Descripción:** Reemplazar esperas implícitas (`ThreadPause`) con mecanismos de sincronización explícita donde sea viable pedagógicamente.

**Historias:**
- **TR-1.1.1:** Como desarrollador, quiero que `BasicCollectionDemo` use `CountDownLatch` para sincronizar hilos, para que los tests sean determinísticos.
- **TR-1.1.2:** Como mantenedor, quiero documentar qué demos requieren temporización artificial y por qué, para mantener claridad pedagógica.

**Criterios de Aceptación (Gherkin):**
```gherkin
Escenario: ArrayList pierde elementos en condiciones de carrera
  Dado una lista ArrayList no sincronizada
  Cuando 7 hilos agregan elementos simultáneamente
  Entonces el tamaño final debe ser menor a 7
  Y el test debe completarse en < 5 segundos sin depender de Thread.sleep

Escenario: SynchronizedList mantiene consistencia
  Dado una lista synchronizedList
  Cuando 7 hilos agregan elementos simultáneamente
  Entonces el tamaño final debe ser exactamente 7
  Y todos los elementos deben estar presentes
```

**Estimación:** 8 story points  
**Prioridad:** Alta (Bloquea releases estables)

---

#### Feature TR-1.2: Suite de Integración Concurrente
**Descripción:** Crear tests que capturen y verifiquen `DemoResult` con assertions específicos sobre comportamiento observable.

**Historias:**
- **TR-1.2.1:** Como desarrollador, quiero `ConcurrentHashMapDemoTest` que verifique mensajes de "fail-fast" vs "fail-safe".
- **TR-1.2.2:** Como mantenedor, quiero tests parametrizados para todas las demos, para validar que ninguna se rompe al refactorizar.

**Criterios de Aceptación:**
```gherkin
Escenario: Cada demo produce salida estructurada válida
  Dado el registro de todas las demos
  Cuando ejecuto cada demo en modo fast
  Entonces DemoResult debe contener al menos 1 mensaje
  Y el título debe coincidir con demo.title()
  Y no debe haber excepciones no manejadas
```

**Estimación:** 5 story points  
**Prioridad:** Alta

---

#### Feature TR-1.3: Cobertura de Código
**Descripción:** Alcanzar 80% de cobertura de líneas con JaCoCo.

**Historias:**
- **TR-1.3.1:** Como mantenedor, quiero reporte de cobertura en CI, para bloquear PRs que bajen cobertura.
- **TR-1.3.2:** Como desarrollador, quiero excluir clases de utilería de renderizado de la métrica de cobertura si son difíciles de testear.

**Criterios de Aceptación:**
```gherkin
Escenario: CI valida cobertura mínima
  Cuando se abre un PR
  Entonces GitHub Actions debe ejecutar JaCoCo
  Y fallar si cobertura < 80%
  Y generar reporte HTML accesible como artifact
```

**Estimación:** 3 story points  
**Prioridad:** Media

---

### Épica 2: Refactorización y Calidad de Código (RC)
**Objetivo:** Eliminar duplicación manteniendo claridad pedagógica.

#### Feature RC-2.1: Framework de Testing Concurrente Interno
**Descripción:** Crear `ConcurrentTestScenario` que encapsule el patrón repetido de executor + tasks + verificación.

**Historias:**
- **RC-2.1.1:** Como desarrollador de demos, quiero una clase `ConcurrentTestScenario` que reciba supplier de colección y task factory, para no repetir código de executor.
- **RC-2.1.2:** Como mantenedor, quiero que el framework permita configurar número de hilos, timeout y estrategia de verificación.

**Criterios de Aceptación:**
```gherkin
Escenario: Refactorización de BasicCollectionDemo
  Dado que BasicCollectionDemo usa el nuevo framework
  Cuando ejecuto la demo
  Entonces el comportamiento debe ser idéntico al original
  Y el código debe reducirse en > 30% de líneas
  Y la salida pedagógica debe mantenerse clara
```

**Estimación:** 8 story points  
**Prioridad:** Media

---

#### Feature RC-2.2: Separación Dominio/Presentación
**Descripción:** Clarificar separación entre modelo de dominio (qué se demuestra) y renderizado (cómo se muestra).

**Historias:**
- **RC-2.2.1:** Como desarrollador, quiero que `DemoResult` sea independiente de `ConsolePrinter`, para poder agregar otros formatos de salida (JSON, CSV).
- **RC-2.2.2:** Como usuario, quiero opción `--format json` para integración con herramientas externas.

**Criterios de Aceptación:**
```gherkin
Escenario: Salida JSON válida
  Dado que ejecuto con --format json --demo basic-collection
  Cuando la demo completa
  Entonces la salida debe ser JSON parseable
  Y debe contener campos: id, title, messages, timestamp
```

**Estimación:** 5 story points  
**Prioridad:** Baja (Nice to have)

---

### Épica 3: Developer Experience (DX)
**Objetivo:** Minimizar fricción para nuevos contribuidores y usuarios.

#### Feature DX-3.1: Setup Automatizado
**Descripción:** Scripts de validación de entorno y setup inicial.

**Historias:**
- **DX-3.1.1:** Como nuevo contribuidor, quiero ejecutar `./validate-env.sh` que verifique Java 17+, Maven, y dependencias, para confirmar que todo está listo.
- **DX-3.1.2:** Como usuario, quiero un Makefile con targets comunes (`make test`, `make demo`, `make demo-single DEMO=basic-collection`).

**Criterios de Aceptación:**
```gherkin
Escenario: Validación de entorno exitosa
  Dado que tengo Java 17+ instalado
  Cuando ejecuto ./validate-env.sh
  Entonces debe mostrar "✓ Java 17.0.x", "✓ Maven available"
  Y retornar exit code 0

Escenario: Validación detecta Java incorrecto
  Dado que tengo Java 11 instalado
  Cuando ejecuto ./validate-env.sh
  Entonces debe mostrar "✗ Java version 11, required 17+"
  Y sugerir instalación via SDKMAN
  Y retornar exit code 1
```

**Estimación:** 3 story points  
**Prioridad:** Media

---

#### Feature DX-3.2: CLI Mejorada
**Descripción:** Mejorar UX de CLI con validaciones y sugerencias.

**Historias:**
- **DX-3.2.1:** Como usuario, quiero que `--demo concurrent` sugiera `concurrent-hash-map` si es único match.
- **DX-3.2.2:** Como usuario, quiero `--verbose` separado de `--debug` para ver progreso sin verbosidad excesiva.
- **DX-3.2.3:** Como usuario, quiero `--duration` para estimar tiempo total de ejecución.

**Criterios de Aceptación:**
```gherkin
Escenario: Sugerencia de demo con partial match
  Dado que ejecuto --demo concurrent
  Cuando no existe demo exacta "concurrent"
  Pero existe "concurrent-hash-map"
  Entonces debe sugerir: "Did you mean: concurrent-hash-map?"
  Y listar otras opciones concurrent-*

Escenario: Verbose mode muestra progreso
  Dado que ejecuto --verbose --fast
  Cuando se ejecutan múltiples demos
  Entonces debe mostrar: "Running demo 1/6: basic-collection... OK"
```

**Estimación:** 5 story points  
**Prioridad:** Baja

---

#### Feature DX-3.3: Template de Nueva Demo
**Descripción:** Plantilla y guía para agregar demos fácilmente.

**Historias:**
- **DX-3.3.1:** Como contribuidor, quiero ejecutar `./new-demo.sh my-demo` que genere estructura base con TODOs marcados.
- **DX-3.3.2:** Como contribuidor, quiero guía visual (checklist) de pasos para agregar demo en < 30 min.

**Criterios de Aceptación:**
```gherkin
Escenario: Creación de demo via script
  Dado que ejecuto ./new-demo.sh queue-demo
  Entonces debe crear src/main/java/.../demo/QueueDemo.java
  Y debe registrarla automáticamente en DemoRegistry
  Y debe crear test stub básico
  Y el proyecto debe compilar inmediatamente
```

**Estimación:** 5 story points  
**Prioridad:** Media

---

### Épica 4: Contenido Educativo Extendido (CE)
**Objetivo:** Ampliar catálogo de demos sin degradar calidad.

#### Feature CE-4.1: Nuevas Colecciones
**Descripción:** Agregar demos de estructuras faltantes.

**Historias:**
- **CE-4.1.1:** Como estudiante, quiero demo de `ConcurrentLinkedQueue` mostrando non-blocking behavior.
- **CE-4.1.2:** Como estudiante, quiero demo de `ArrayBlockingQueue` vs `LinkedBlockingQueue` comparando throughput.
- **CE-4.1.3:** Como estudiante, quiero demo de `ConcurrentHashMap` + `LongAdder` para contadores concurrentes.

**Criterios de Aceptación:**
```gherkin
Escenario: Demo de BlockingQueue incluye producer/consumer
  Dado la demo blocking-queue
  Cuando se ejecuta
  Entonces debe mostrar patrón producer/consumer clásico
  Y debe demostrar back-pressure cuando buffer lleno
  Y debe incluir timeout y estrategias de rechazo
```

**Estimación:** 13 story points (5 + 5 + 3)  
**Prioridad:** Media

---

#### Feature CE-4.2: Guía de Decisión
**Descripción:** Documento que ayude a elegir qué colección usar.

**Historias:**
- **CE-4.2.1:** Como desarrollador, quiero tabla comparativa de tradeoffs: read-heavy vs write-heavy, ordenado vs no ordenado, blocking vs non-blocking.
- **CE-4.2.2:** Como estudiante, quiero diagrama de decisión (árbol) para elegir colección basado en requisitos.

**Criterios de Aceptación:**
```gherkin
Escenario: Tabla comparativa completa
  Dado que abro docs/decision-guide.md
  Entonces debe listar todas las colecciones implementadas
  Y cada una debe tener: casos de uso, complejidad temporal, thread-safety, memory overhead
  Y debe incluir "When NOT to use"
```

**Estimación:** 5 story points  
**Prioridad:** Baja

---

### Épica 5: Benchmarks Científicos (BM)
**Objetivo:** Agregar métricas cuantitativas de rendimiento.

#### Feature BM-5.1: Integración JMH
**Descripción:** Benchmarks con Java Microbenchmark Harness.

**Historias:**
- **BM-5.1.1:** Como investigador, quiero benchmarks de throughput (ops/sec) para cada colección con diferentes niveles de contención.
- **BM-5.1.2:** Como mantenedor, quiero CI ejecute benchmarks en máquina dedicada (no bloqueante para PRs).

**Criterios de Aceptación:**
```gherkin
Escenario: Benchmark ejecutable localmente
  Dado que ejecuto ./mvnw jmh:benchmark
  Cuando completa
  Entonces debe generar target/benchmark-results.json
  Y debe incluir: colección, threads, ops/sec, error margin

Escenario: Benchmarks no bloquean CI
  Dado que abro un PR
  Cuando CI ejecuta
  Entonces benchmarks deben correr en job separado (opcional)
  O solo en merge a master
```

**Estimación:** 13 story points  
**Prioridad:** Baja (Investigación futura)

---

## 3. Roadmap y Milestones

```
Q2 2025 (Abr-Jun)
├── Sprint 1-2: TR-1.1, TR-1.2 (Tests determinísticos)
├── Sprint 3: TR-1.3 (Cobertura JaCoCo)
└── Sprint 4: RC-2.1 (Framework testing)

Q3 2025 (Jul-Sep)
├── Sprint 5-6: DX-3.1, DX-3.3 (Setup y templates)
├── Sprint 7: CE-4.1 Parte 1 (ConcurrentLinkedQueue)
└── Sprint 8: CE-4.1 Parte 2 (BlockingQueues)

Q4 2025 (Oct-Dic)
├── Sprint 9: CE-4.1 Parte 3 (LongAdder), CE-4.2 (Guía decisión)
├── Sprint 10: RC-2.2 (Separación dominio/presentación)
├── Sprint 11: DX-3.2 (CLI mejorada)
└── Sprint 12: BM-5.1 (JMH - investigación)
```

**Milestones Clave:**

| Fecha | Milestone | Entregables |
|-------|-----------|-------------|
| 2025-06-30 | **Stability Release** | 80% cobertura, 0 flaky tests, CI verde |
| 2025-09-30 | **Developer Ready** | Setup automatizado, template de demos, guía contribución |
| 2025-12-31 | **Content Complete** | 9+ demos, guía de decisión, benchmarks iniciales |

---

## 4. Definición de Done

Para cualquier feature/historia:

- [ ] Código implementa requerimiento funcional
- [ ] Tests unitarios/integración pasan (`./mvnw test`)
- [ ] Cobertura no disminuye (JaCoCo verde)
- [ ] CI pasa en Java 17 y 21
- [ ] Documentación actualizada (README, ADR si aplica)
- [ ] CHANGELOG.md actualizado con descripción del cambio
- [ ] Revisión de código aprobada (PR mergeado)
- [ ] Demo ejecutable sin errores (`./mvnw exec:java`)

---

## 5. Gestión de Riesgos

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|-------------|---------|------------|
| Refactorización oscurece pedagogía | Media | Alto | Revisión por educador, mantener salida textual idéntica |
| Tests concurrentes flaky en CI | Media | Alto | Límite de reintentos (3), máquina dedicada, investigar sincronización |
| Contribuidores no llegan | Alta | Medio | Promoción en comunidades Java, issues "good first issue" |
| Scope creep a librería | Baja | Alto | Revisar OKRs trimestral, rechazar PRs que añaden APIs públicas |
| Incompatibilidad Java 21 LTS | Baja | Medio | CI valida ambas versiones, documentar decisiones en ADRs |

---

## 6. Métricas de Seguimiento

### Dashboard Trimestral

**Métricas Técnicas:**
- Cobertura de código (JaCoCo)
- Tiempo de build (CI)
- Flaky test rate (# builds fallidos / total)
- Deuda técnica (SonarQube si se adopta)

**Métricas de Proceso:**
- Lead time (días desde issue creado hasta merge)
- Cycle time (días desde "in progress" hasta merge)
- PR merge rate (% aceptados vs rechazados)
- Tiempo de revisión promedio

**Métricas de Comunidad:**
- # contribuidores únicos
- # issues reportados por externos
- # estrellas GitHub
- # forks

---

## 7. Decisiones Arquitectónicas Pendientes (ADRs)

Los siguientes temas requieren ADR (Architecture Decision Record) antes de implementar:

1. **ADR-001:** Uso de CountDownLatch vs ThreadPause — ¿cuándo sacrificar determinismo por claridad visual?
2. **ADR-002:** Auto-registro de demos via ServiceLoader vs registro manual — tradeoffs de magia vs claridad
3. **ADR-003:** Formato JSON de salida — ¿vale la pena el overhead de Jackson/Gson?
4. **ADR-004:** JMH en CI — estrategia de ejecución (dedicado vs compartido, frecuencia)

---

## 8. Recursos y Presupuesto

**Recursos Humanos (voluntarios/open source):**
- 1 mantenedor principal (review, arquitectura)
- 1-2 contribuidores activos (implementación)
- 0.5 documentador técnico

**Infraestructura:**
- GitHub Actions (gratis, público)
- GitHub Pages para docs (opcional)
- Máquina bare-metal para benchmarks (opcional, futuro)

**Herramientas:**
- JaCoCo (cobertura) - gratuito
- JMH (benchmarks) - gratuito
- SonarCloud (análisis estático) - gratuito para open source

---

## 9. Aprobaciones

| Rol | Nombre | Fecha | Estado |
|-----|--------|-------|--------|
| Product Owner | @cazucito | 2025-04-11 | ✅ Aprobado |
| Tech Lead | TBD | - | ⏳ Pendiente |
| Maintainer | TBD | - | ⏳ Pendiente |

---

## 10. Apéndice: Glosario

- **ADR:** Architecture Decision Record — documento que captura decisión técnica importante
- **CI:** Continuous Integration — integración continua
- **Flaky test:** Test que falla intermitentemente sin cambios en código
- **JMH:** Java Microbenchmark Harness — herramienta de benchmarks JVM
- **OKR:** Objectives and Key Results — framework de objetivos
- **PoC:** Proof of Concept — prueba de concepto
- **Story Point:** Unidad relativa de estimación ágil

---

*Documento generado siguiendo estándares ISO 21500 para gestión de proyectos y prácticas ágiles (SAFe/Scrum).*
