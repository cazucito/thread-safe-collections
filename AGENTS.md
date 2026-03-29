# AGENTS

## Propósito

Este repositorio contiene una PoC educativa sobre colecciones seguras en ambientes multihilo con Java y Maven.

## Convenciones del proyecto

- El código fuente debe usar identificadores en inglés.
- Los comentarios, la documentación y los mensajes orientados a personas deben escribirse en español neutro.
- La estructura del proyecto debe seguir Maven estándar: `src/main/java` y `src/test/java`.
- Las demostraciones deben ser ejecutables desde consola sin depender de un IDE específico.
- Cuando se modifique el comportamiento de una demo, también se debe actualizar la documentación relevante.
- Las modificaciones deben realizarse en una rama adicional, no directamente sobre la rama principal.
- Todo cambio debe proponerse mediante un Pull Request para revisión y aceptación antes de integrarse.
- Los mensajes de commit deben seguir el estándar Conventional Commits.

## Verificación recomendada

- Compilar: `mvn compile`
- Ejecutar la aplicación: `mvn exec:java`
- Ejecutar pruebas: `mvn test`

## Alcance de cambios

- Evitar reintroducir archivos específicos de Eclipse u otros IDEs dentro del control de versiones.
- Preferir utilidades pequeñas y legibles antes que abstracciones innecesarias.
- Mantener el objetivo pedagógico del proyecto: comparar estructuras no seguras con alternativas concurrentes.
