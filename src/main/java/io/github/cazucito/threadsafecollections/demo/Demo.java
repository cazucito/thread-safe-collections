package io.github.cazucito.threadsafecollections.demo;

/**
 * Contrato común para cada demostración disponible en la aplicación.
 */
public interface Demo {

    /**
     * Identificador estable para invocar la demo desde CLI.
     *
     * @return identificador único de la demo
     */
    String id();

    /**
     * Título legible para personas.
     *
     * @return título de la demo
     */
    String title();

    /**
     * Describe qué debería aprender la persona al ejecutar la demo.
     *
     * @return objetivo pedagógico principal
     */
    String learningObjective();

    /**
     * Describe qué salida o comportamiento conviene observar durante la ejecución.
     *
     * @return observación recomendada
     */
    String expectedObservation();

    /**
     * Resume la conclusión principal que deja la demo.
     *
     * @return conclusión breve
     */
    String keyTakeaway();

    /**
     * Ejecuta la demo y devuelve su salida estructurada.
     *
     * @return resultado estructurado de la demo
     */
    DemoResult run();
}
