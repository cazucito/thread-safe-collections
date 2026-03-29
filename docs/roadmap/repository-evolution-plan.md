# Plan de evolución del repositorio

## Resumen

Este documento define la visión técnica y el roadmap de evolución para `thread-safe-collections` durante los próximos 6 a 12 meses. El objetivo es fortalecer el proyecto como PoC educativa, no transformarlo en una librería productiva, mientras se mejora su mantenibilidad, testabilidad, experiencia de desarrollo y capacidad de recibir contribuciones.

## Propósito del proyecto

`thread-safe-collections` es una PoC educativa en Java para demostrar diferencias entre colecciones tradicionales y alternativas concurrentes en escenarios multihilo. Su valor principal está en hacer visibles comportamientos como `fail-fast`, `snapshot iteration`, consistencia débil y patrones de sincronización.

El repositorio no busca, en esta etapa, convertirse en una librería distribuible ni en un framework de utilerías de concurrencia.

## Estado actual

Actualmente el proyecto cuenta con:

- Estructura Maven estándar.
- Aplicación de consola ejecutable desde el punto de entrada principal.
- Registro central de demos con soporte CLI para listar y ejecutar demos concretas.
- Salida estructurada mediante un contrato común `Demo` y un resultado tipado `DemoResult`.
- Utilerías de soporte para recorridos, tareas asíncronas y renderizado de consola.
- Cobertura de pruebas base para CLI, utilerías y semántica representativa de iteradores.

## Problemas detectados

- Parte del comportamiento concurrente sigue dependiendo de temporización artificial para hacer visibles ciertas condiciones.
- Las demos aún comparten patrones repetidos que pueden factorizarse mejor sin perder claridad pedagógica.
- La cobertura de pruebas todavía es mínima para un proyecto que ya incluye CLI, renderizado y múltiples demos.
- El repositorio no tenía antes una zona navegable de documentación técnica y apenas empieza a consolidarla.
- Aún no existe una distinción fuerte entre modelo de dominio didáctico y detalles específicos de renderizado de consola.

## Estado objetivo

Se busca llevar el proyecto a un estado donde sea:

- Reproducible desde cualquier entorno con `./mvnw`.
- Verificable por CI en cada cambio.
- Extensible mediante demos nuevas registradas de forma uniforme.
- Fácil de entender por maintainers y contributors.
- Con documentación suficiente para justificar decisiones técnicas y priorizar mejoras.

## Principios de evolución

- Código en inglés y documentación en español neutro.
- Bajo acoplamiento entre ejecución, modelado de resultados y renderizado.
- Pruebas orientadas a comportamiento observable.
- Automatización reproducible en local y CI.
- Cambios colaborativos mediante ramas adicionales, Pull Requests y Conventional Commits.
- Enfoque pedagógico por encima de la sobreingeniería.

## Roadmap por horizontes

### Horizonte 1: `foundation`

Objetivo: consolidar la base operativa del repositorio.

- Mantener Maven Wrapper como punto de entrada estándar.
- Mantener plugins de Maven versionados y entorno mínimo fijado.
- Ejecutar `compile` y `test` en GitHub Actions sobre Java 17.
- Alinear `README.md` con comandos, CLI y documentación disponibles.

### Horizonte 2: `testability`

Objetivo: mejorar la capacidad de verificar y evolucionar el comportamiento.

- Seguir separando ejecución de demos y renderizado a consola.
- Expandir el uso de `DemoResult` como salida observable principal.
- Aumentar pruebas sobre comportamientos fail-fast, snapshot y consistencia de resultados.
- Reemplazar esperas implícitas por coordinación explícita donde sea viable sin degradar el valor didáctico.

### Horizonte 3: `developer-platform`

Objetivo: estabilizar la experiencia de desarrollo del repositorio.

- Consolidar la interfaz `Demo` como punto único de extensión.
- Mantener un registro central de demos y CLI consistente.
- Añadir ayuda más completa y mejores mensajes de error para la CLI.
- Considerar documentación adicional de arquitectura ligera y guía para agregar nuevas demos.

### Horizonte 4: `productization-lite`

Objetivo: ampliar el valor didáctico del proyecto sin cambiar su naturaleza.

- Agregar demos de `ConcurrentLinkedQueue`.
- Agregar demos de `BlockingQueue`.
- Agregar un caso didáctico de frecuencia con `ConcurrentHashMap` + `LongAdder`.
- Documentar criterios de elección, ventajas y tradeoffs de cada estructura.

## Métricas y criterios de éxito

- `./mvnw test` ejecuta el build esperado en local y CI.
- La CI se mantiene verde sobre la rama principal.
- La aplicación soporta `--list`, `--demo <id>` y `--debug`.
- Existe una base de pruebas útil sobre CLI, resultados estructurados y semántica concurrente representativa.
- `README.md` y `docs/` enlazan correctamente la documentación principal.

## Riesgos y decisiones explícitas

- Se debe evitar convertir la PoC en un framework accidental.
- La claridad pedagógica tiene prioridad sobre abstraer en exceso.
- Algunas demostraciones pueden seguir necesitando temporización controlada para hacer visibles ciertos comportamientos; esos casos deben quedar documentados y cubiertos por pruebas complementarias donde sea posible.
- Las mejoras deben favorecer extensibilidad y mantenibilidad, pero sin ocultar los conceptos que el proyecto busca enseñar.
