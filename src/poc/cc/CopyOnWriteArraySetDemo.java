package poc.cc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import poc.util.Impresor;
import poc.util.Temporizador;
import poc.util.TipoMensajes;
import poc.util.AdicionadorAColeccionNoSincronizado;

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
		Impresor.muestraEnConsola(TipoMensajes.SUBTITULO, "CONSTRUCTORES");
		// CopyOnWriteArraySet() => Creates an empty list
		CopyOnWriteArraySet<String> cowas01 = new CopyOnWriteArraySet<>();
		cowas01.add("Uno");
		cowas01.add("Dos");
		cowas01.add("Tres");
		Impresor.muestraEnConsola(TipoMensajes.DEPURACION, cowas01.toString());
		// CopyOnWriteArraySet​(Collection<? extends E> c) Creates a set containing all
		// of the elements of the specified collection.
		List<String> coleccion = new ArrayList<>();
		coleccion.add("Uno");
		coleccion.add("Dos");
		coleccion.add("Tres");
		CopyOnWriteArraySet<String> cowas02 = new CopyOnWriteArraySet<>(coleccion);
		Impresor.muestraEnConsola(TipoMensajes.DEPURACION, cowas02.toString());
	}

	/**
	 * Prueba CopyOnWriteArrayList
	 */
	public static void testCopyOnWriteArraySet() {
		/////////////////
		// HashSet
		Impresor.muestraEnConsola(TipoMensajes.SUBTITULO, "HashSet - FAIL-FAST");
		final Set<String> hs01 = new HashSet<>(Arrays.asList("Uno", "Dos", "Tres"));
		hs01.add("Tres");
		Impresor.muestraEnConsola(TipoMensajes.MENSAJE_OK, ".add(\"Tres\") no altera la coleccion");
		ExecutorService hilos1 = Executors.newCachedThreadPool();
		hilos1.execute(new AdicionadorAColeccionNoSincronizado(hs01, "CUATRO"));
		hilos1.shutdown();
		Impresor.imprime(hs01);
		Impresor.muestraEnConsola(TipoMensajes.MENSAJE, "Estado final: " + hs01.toString());
		// CopyOnWriteArrayList
		Impresor.muestraEnConsola(TipoMensajes.SUBTITULO, "CopyOnWriteArraySet - FAIL-SAFE");
		final CopyOnWriteArraySet<String> cowas01 = new CopyOnWriteArraySet<>(Arrays.asList("Uno", "Dos", "Tres"));
		cowas01.add("Tres");
		Impresor.muestraEnConsola(TipoMensajes.MENSAJE_OK, ".add(\"Tres\") no altera la coleccion");
		ExecutorService hilos2 = Executors.newCachedThreadPool();
		hilos2.execute(new AdicionadorAColeccionNoSincronizado(cowas01, "CUATRO"));
		hilos2.shutdown();
		Impresor.imprime(cowas01);
		Impresor.muestraEnConsola(TipoMensajes.DEPURACION, "Estado final: " + cowas01.toString());
	}

}