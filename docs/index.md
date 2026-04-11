---
title: Thread-Safe Collections
hide:
  - navigation
  - toc
---

<style>
.hero {
  text-align: center;
  padding: 4rem 2rem;
}
.hero h1 {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 1rem;
}
.hero p {
  font-size: 1.25rem;
  color: var(--md-default-fg-color--light);
  max-width: 700px;
  margin: 0 auto 2rem;
}
.hero .md-button {
  margin: 0.5rem;
}
.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}
.feature {
  text-align: center;
  padding: 1.5rem;
}
.feature-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}
.feature h3 {
  margin-bottom: 0.5rem;
}
.feature p {
  color: var(--md-default-fg-color--light);
}
</style>

<div class="hero">
  <h1>🔒 Thread-Safe Collections</h1>
  <p>PoC educativa profesional sobre colecciones seguras en ambientes multihilo con Java. Aprende mediante demostraciones interactivas, benchmarks científicos y guías prácticas.</p>
  
  <a href="guides/quickstart/" class="md-button md-button--primary">Comenzar</a>
  <a href="demos/" class="md-button">Ver Demos</a>
  <a href="https://github.com/cazucito/thread-safe-collections" class="md-button">GitHub</a>
</div>

<div class="features">
  <div class="feature">
    <div class="feature-icon">📚</div>
    <h3>9 Demos Interactivas</h3>
    <p>Desde ArrayList básico hasta LongAdder, cada colección con comportamiento observable y explicaciones pedagógicas.</p>
  </div>
  
  <div class="feature">
    <div class="feature-icon">⚡</div>
    <h3>Benchmarks JMH</h3>
    <p>Mediciones científicas de rendimiento con Java Microbenchmark Harness. Datos reales para decisiones informadas.</p>
  </div>
  
  <div class="feature">
    <div class="feature-icon">🎯</div>
    <h3>Guía de Decisión</h3>
    <p>¿No sabes qué colección usar? Nuestro árbol de decisión y tablas comparativas te guían paso a paso.</p>
  </div>
  
  <div class="feature">
    <div class="feature-icon">✅</div>
    <h3>Testeado</h3>
    <p>89.8% de cobertura de código con tests unitarios e integración. Código profesional y mantenible.</p>
  </div>
  
  <div class="feature">
    <div class="feature-icon">🚀</div>
    <h3>Multiplataforma</h3>
    <p>Funciona en Windows, Linux y macOS. Maven Wrapper incluido. Sin dependencias externas.</p>
  </div>
  
  <div class="feature">
    <div class="feature-icon">📖</div>
    <h3>Código Abierto</h3>
    <p>Licencia GPL v3.0. Documentación completa, contribuciones bienvenidas. Hecho con ❤️ para la comunidad.</p>
  </div>
</div>

## 🚀 Comienza en 30 segundos

```bash
# Clonar repositorio
git clone https://github.com/cazucito/thread-safe-collections.git
cd thread-safe-collections

# Validar entorno
./mvnw validate -Pcheck-env

# Ejecutar todas las demos
./mvnw exec:java -Dexec.args="--fast"

# Ejecutar demo específica
./mvnw compile exec:java -Pdemo -Ddemo.id=concurrent-hash-map
```

## 📊 ¿Qué vas a aprender?

<div class="grid cards" markdown>

-   :material-sync:{ .lg .middle } __Sincronización

    ---

    Entiende las diferencias entre colecciones sincronizadas, concurrentes y lock-free. Descubre race conditions en vivo.

-   :material-speedometer:{ .lg .middle } __Rendimiento

    ---

    Compara throughput real con benchmarks JMH. Aprende cuándo usar CopyOnWrite vs ConcurrentHashMap.

-   :material-decision-tree:{ .lg .middle } __Decisiones

    ---

    Domina el arte de elegir la colección correcta. Lee caso de uso → selecciona implementación → justifica.

-   :material-test-tube:{ .lg .middle } __Testing

    ---

    Aprende a testear código concurrente. Tests determinísticos, integración y cobertura de código.

</div>

## 🎓 ¿Para quién es esto?

- **Estudiantes** que quieren entender concurrencia Java con ejemplos prácticos
- **Desarrolladores** que necesitan elegir la colección correcta para producción
- **Profesores** que buscan material didáctico actualizado y profesional
- **Equipos** que quieren estándares de calidad en código concurrente

## 📈 Estadísticas del Proyecto

- **9** demos de colecciones concurrentes
- **198** tests automatizados
- **89.8%** cobertura de código
- **4** benchmarks JMH
- **20+** commits con historial limpio
- **5** épicas implementadas

---

¿Listo para comenzar? [→ Guía Rápida](guides/quickstart.md){ .md-button .md-button--primary }
