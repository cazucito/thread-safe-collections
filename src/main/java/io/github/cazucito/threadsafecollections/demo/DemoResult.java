package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.cli.MessageType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Resultado estructurado de la ejecución de una demo.
 */
public final class DemoResult {

    private final String demoId;
    private final String title;
    private final List<DemoMessage> messages;

    private DemoResult(String demoId, String title, List<DemoMessage> messages) {
        this.demoId = demoId;
        this.title = title;
        this.messages = List.copyOf(messages);
    }

    /**
     * Crea un builder para un resultado de demo.
     *
     * @param demoId identificador de la demo
     * @param title  título de la demo
     * @return builder listo para agregar mensajes
     */
    public static Builder builder(String demoId, String title) {
        return new Builder(demoId, title);
    }

    /**
     * Devuelve el identificador de la demo.
     *
     * @return identificador único
     */
    public String demoId() {
        return demoId;
    }

    /**
     * Devuelve el título de la demo.
     *
     * @return título legible
     */
    public String title() {
        return title;
    }

    /**
     * Devuelve los mensajes de la demo en el orden en que fueron registrados.
     *
     * @return mensajes inmutables
     */
    public List<DemoMessage> messages() {
        return messages;
    }

    /**
     * Builder sencillo y seguro para múltiples hilos.
     */
    public static final class Builder {

        private final String demoId;
        private final String title;
        private final List<DemoMessage> messages = Collections.synchronizedList(new ArrayList<>());

        private Builder(String demoId, String title) {
            this.demoId = demoId;
            this.title = title;
        }

        /**
         * Agrega un mensaje al resultado.
         *
         * @param type tipo de mensaje
         * @param text contenido
         * @return el mismo builder para encadenar llamadas
         */
        public Builder add(MessageType type, String text) {
            messages.add(new DemoMessage(type, text));
            return this;
        }

        /**
         * Construye el resultado final.
         *
         * @return resultado inmutable de la demo
         */
        public DemoResult build() {
            synchronized (messages) {
                return new DemoResult(demoId, title, new ArrayList<>(messages));
            }
        }
    }
}
