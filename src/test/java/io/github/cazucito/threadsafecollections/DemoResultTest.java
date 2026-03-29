package io.github.cazucito.threadsafecollections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.cazucito.threadsafecollections.demo.DemoResult;
import io.github.cazucito.threadsafecollections.support.MessageType;
import org.junit.jupiter.api.Test;

/**
 * Pruebas para la salida estructurada de las demos.
 */
class DemoResultTest {

    @Test
    void builderCollectsMessagesInOrder() {
        DemoResult result = DemoResult.builder("basic-collection", "Colecciones")
                .add(MessageType.SUBTITLE, "Subtítulo")
                .add(MessageType.MESSAGE, "Contenido")
                .build();

        assertEquals("basic-collection", result.demoId());
        assertEquals("Colecciones", result.title());
        assertEquals(2, result.messages().size());
        assertEquals("Subtítulo", result.messages().get(0).text());
        assertEquals("Contenido", result.messages().get(1).text());
    }

    @Test
    void messagesAreImmutableAfterBuild() {
        DemoResult result = DemoResult.builder("id", "Título")
                .add(MessageType.MESSAGE, "Hola")
                .build();

        assertThrows(UnsupportedOperationException.class,
                () -> result.messages().add(null));
    }
}
