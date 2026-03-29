package io.github.cazucito.threadsafecollections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.junit.jupiter.api.Test;

/**
 * Pruebas representativas de semántica fail-fast y snapshot.
 */
class CollectionSemanticsTest {

    @Test
    void arrayListIteratorIsFailFastAfterExternalMutation() {
        List<String> list = new ArrayList<>(List.of("Uno", "Dos", "Tres"));
        Iterator<String> iterator = list.iterator();

        assertEquals("Uno", iterator.next());
        list.add("CUATRO");

        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void copyOnWriteArrayListIteratorKeepsASnapshot() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>(List.of("Uno", "Dos", "Tres"));
        Iterator<String> iterator = list.iterator();
        List<String> traversed = new ArrayList<>();

        traversed.add(iterator.next());
        list.add("CUATRO");
        iterator.forEachRemaining(traversed::add);

        assertEquals(List.of("Uno", "Dos", "Tres"), traversed);
        assertEquals(List.of("Uno", "Dos", "Tres", "CUATRO"), list);
    }
}
