package poc.cc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import poc.util.Impresor;
import poc.util.Temporizador;
import poc.util.TipoMensajes;
import poc.util.AdicionadorAColeccionNoSincronizado;

/**
 * DEMO public class CopyOnWriteArrayList<E> extends Object implements List<E>,
 * RandomAccess, Cloneable, Serializable
 * 
 * "A thread-safe variant of ArrayList in which all mutative operations (add,
 * set, and so on) are implemented by making a fresh copy of the underlying
 * array. This is ordinarily too costly, but may be more efficient than
 * alternatives when traversal operations vastly outnumber mutations, and is
 * useful when you cannot or don't want to synchronize traversals, yet need to
 * preclude interference among concurrent threads. The "snapshot" style iterator
 * method uses a reference to the state of the array at the point that the
 * iterator was created. This array never changes during the lifetime of the
 * iterator, so interference is impossible and the iterator is guaranteed not to
 * throw ConcurrentModificationException. The iterator will not reflect
 * additions, removals, or changes to the list since the iterator was created.
 * Element-changing operations on iterators themselves (remove, set, and add)
 * are not supported. These methods throw UnsupportedOperationException.
 * 
 * All elements are permitted, including null.
 * 
 * Memory consistency effects: As with other concurrent collections, actions in
 * a thread prior to placing an object into a CopyOnWriteArrayList happen-before
 * actions subsequent to the access or removal of that element from the
 * CopyOnWriteArrayList in another thread.
 * 
 * This class is a member of the Java Collections Framework.
 * {@link https://docs.oracle.com/javase/10/docs/api/java/util/concurrent/CopyOnWriteArrayList.html}"
 * 
 * 
 * @author cazucito
 *
 */
public class CopyOnWriteArrayListDemo {
	/**
	 * Probando constructores
	 */
	public static void testConstructores() {
		/////////////////
		// CONSTRUCTORES
		Impresor.muestraEnConsola(TipoMensajes.SUBTITULO, "CONSTRUCTORES");
		// CopyOnWriteArrayList() => Creates an empty list
		CopyOnWriteArrayList<String> cowal01 = new CopyOnWriteArrayList<>();
		cowal01.add("Uno");
		cowal01.add("Dos");
		cowal01.add("Tres");
		Impresor.muestraEnConsola(TipoMensajes.DEPURACION, cowal01.toString());
		// CopyOnWriteArrayList​(E[] toCopyIn) => Creates a list holding a copy of the
		// given array.
		String[] arregloDeCadenas = { "Uno", "Dos", "Tres" };
		CopyOnWriteArrayList<String> cowal02 = new CopyOnWriteArrayList<>(arregloDeCadenas);
		Impresor.muestraEnConsola(TipoMensajes.DEPURACION, cowal02.toString());
		// CopyOnWriteArrayList​(Collection<? extends E> c) => Creates a list containing
		// the elements of the specified collection, in the order they are returned by
		// the collection's iterator.
		List<String> coleccion = new ArrayList<>();
		coleccion.add("Uno");
		coleccion.add("Dos");
		coleccion.add("Tres");
		CopyOnWriteArrayList<String> cowal03 = new CopyOnWriteArrayList<>(coleccion);
		Impresor.muestraEnConsola(TipoMensajes.DEPURACION, cowal03.toString());
	}

	/**
	 * Prueba CopyOnWriteArrayList
	 */
	public static void testCopyOnWriteArrayList() {
		// ArrayList
		Impresor.muestraEnConsola(TipoMensajes.SUBTITULO, "ArrayList - FAIL-FAST");
		final ArrayList<String> al01 = new ArrayList<>(Arrays.asList("Uno", "Dos", "Tres"));
		ExecutorService hilos1 = Executors.newCachedThreadPool();
		hilos1.execute(new AdicionadorAColeccionNoSincronizado(al01, "CUATRO"));
		hilos1.shutdown();
		Impresor.imprime(al01);
		Impresor.muestraEnConsola(TipoMensajes.MENSAJE, "Estado final: " + al01.toString());
		// CopyOnWriteArrayList
		Impresor.muestraEnConsola(TipoMensajes.SUBTITULO, "CopyOnWriteArrayList - FAIL-SAFE");
		final CopyOnWriteArrayList<String> cowal01 = new CopyOnWriteArrayList<>(Arrays.asList("Uno", "Dos", "Tres"));
		ExecutorService hilos2 = Executors.newCachedThreadPool();
		hilos2.execute(new AdicionadorAColeccionNoSincronizado(cowal01, "CUATRO"));
		hilos2.shutdown();
		Impresor.imprime(cowal01);
		Impresor.muestraEnConsola(TipoMensajes.MENSAJE, "Estado final: " + cowal01.toString());
	}
}
