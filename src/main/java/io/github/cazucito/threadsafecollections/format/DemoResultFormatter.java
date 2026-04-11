package io.github.cazucito.threadsafecollections.format;

import io.github.cazucito.threadsafecollections.demo.Demo;
import io.github.cazucito.threadsafecollections.demo.DemoResult;
import java.io.PrintStream;

/**
 * Interfaz para formateadores de resultados de demo.
 * 
 * Separa la presentación del dominio permitiendo múltiples
 * formatos de salida (consola, JSON, etc.) sin modificar
 * la lógica de las demos.
 */
public interface DemoResultFormatter {

    /**
     * Formatea y escribe el resultado completo de una demo.
     *
     * @param demo   la demo ejecutada
     * @param result resultado estructurado de la demo
     * @param output flujo de salida donde escribir
     */
    void formatDemoResult(Demo demo, DemoResult result, PrintStream output);

    /**
     * Formatea y escribe la lista de demos disponibles.
     *
     * @param demos  demos registradas
     * @param output flujo de salida donde escribir
     */
    void formatDemoList(java.util.List<Demo> demos, PrintStream output);

    /**
     * Formatea y escribe el encabezado de la aplicación.
     *
     * @param title  título a mostrar
     * @param output flujo de salida donde escribir
     */
    void formatHeader(String title, PrintStream output);

    /**
     * Formatea y escribe el pie de página de la aplicación.
     *
     * @param output flujo de salida donde escribir
     */
    void formatFooter(PrintStream output);

    /**
     * Formatea y escribe un mensaje informativo.
     *
     * @param message mensaje a mostrar
     * @param output  flujo de salida donde escribir
     */
    void formatInfo(String message, PrintStream output);
}
