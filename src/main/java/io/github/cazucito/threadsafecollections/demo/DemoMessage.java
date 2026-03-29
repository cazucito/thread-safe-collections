package io.github.cazucito.threadsafecollections.demo;

import io.github.cazucito.threadsafecollections.support.MessageType;

/**
 * Mensaje estructurado generado por una demo.
 *
 * @param type tipo de mensaje
 * @param text contenido del mensaje
 */
public record DemoMessage(MessageType type, String text) {
}
