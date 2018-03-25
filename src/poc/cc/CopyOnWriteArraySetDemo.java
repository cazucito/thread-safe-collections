package poc.cc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import poc.util.Impresor;
import poc.util.ModificadorDeColeccion;

/**
 * DEMO public class CopyOnWriteArraySet<E> extends AbstractSet<E> implements
 * Serializable
 * 
 * "A Set that uses an internal CopyOnWriteArrayList for all of its operations.
 * Thus, it shares the same basic properties:
 * 
 * It is best suited for applications in which set sizes generally stay small,
 * read-only operations vastly outnumber mutative operations, and you need to
 * prevent interference among threads during traversal.
 * 
 * It is thread-safe.
 * 
 * Mutative operations (add, set, remove, etc.) are expensive since they usually
 * entail copying the entire underlying array. Iterators do not support the
 * mutative remove operation.
 *
 * Traversal via iterators is fast and cannot encounter interference from other
 * threads.
 * 
 * Iterators rely on unchanging snapshots of the array at the time the iterators
 * were constructed.
 * 
 * This class is a member of the Java Collections Framework.
 * 
 * {@link https://docs.oracle.com/javase/10/docs/api/java/util/concurrent/CopyOnWriteArraySet.html}"
 * 
 * 
 * @author cazucito
 *
 */
public class CopyOnWriteArraySetDemo {

	/**
	 * Probando constructores
	 */
	public static void testConstructores() {
		/////////////////
		// CONSTRUCTORES
		System.out.println("||=== CONSTRUCTORES CopyOnWriteArraySet() ===||");
		// CopyOnWriteArraySet() => Creates an empty list
		CopyOnWriteArraySet<String> cowal01 = new CopyOnWriteArraySet<>();
		cowal01.add("Uno");
		cowal01.add("Dos");
		cowal01.add("Tres");
		// CopyOnWriteArraySet​(Collection<? extends E> c) Creates a set containing all of the elements of the specified collection.
		List<String> coleccion = new ArrayList<>();
		coleccion.add("Uno");
		coleccion.add("Dos");
		coleccion.add("Tres");
		CopyOnWriteArraySet<String> cowal03 = new CopyOnWriteArraySet<>(coleccion);
	}

	/**
	 * Prueba CopyOnWriteArrayList
	 */
	public static void testCopyOnWriteArraySet() {
		/////////////////
		// Piscina de hilos
		ExecutorService hilos = Executors.newCachedThreadPool();
		// ArrayList
		System.out.println("||=== ArrayList ===||");
		final ArrayList<String> numeros = new ArrayList<>(Arrays.asList("Uno", "Dos", "Tres"));
		numeros.add("Tres");
		hilos.execute(new ModificadorDeColeccion(numeros, "CUATRO"));
		Impresor.imprime(numeros);
		// CopyOnWriteArrayList
		System.out.println("||=== CopyOnWriteArraySet ===||");
		final CopyOnWriteArraySet<String> numeros2 = new CopyOnWriteArraySet<>(Arrays.asList("Uno", "Dos", "Tres"));
		numeros2.add("Tres");
		hilos.execute(new ModificadorDeColeccion(numeros2, "CUATRO"));
		Impresor.imprime(numeros2);
	}

}