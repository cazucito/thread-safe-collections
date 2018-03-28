package poc.cc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import poc.util.Impresor;
import poc.util.TipoMensajes;
import poc.util.AdicionadorAColeccionNoSincronizado;

/**
 * DEMO public class ConcurrentSkipListSet<E> extends AbstractSet<E> implements
 * NavigableSet<E>, Cloneable, Serializable
 * 
 * A scalable concurrent NavigableSet implementation based on a
 * ConcurrentSkipListMap. The elements of the set are kept sorted according to
 * their natural ordering, or by a Comparator provided at set creation time,
 * depending on which constructor is used.
 * 
 * This implementation provides expected average log(n) time cost for the
 * contains, add, and remove operations and their variants. Insertion, removal,
 * and access operations safely execute concurrently by multiple threads.
 * Iterators are weakly consistent, returning elements reflecting the state of
 * the set at some point at or since the creation of the iterator. They do not
 * throw ConcurrentModificationException, and may proceed concurrently with
 * other operations. Ascending ordered views and their iterators are faster than
 * descending ones.
 * 
 * Beware that, unlike in most collections, the size method is not a
 * constant-time operation. Because of the asynchronous nature of these sets,
 * determining the current number of elements requires a traversal of the
 * elements, and so may report inaccurate results if this collection is modified
 * during traversal. Additionally, the bulk operations addAll, removeAll,
 * retainAll, containsAll, equals, and toArray are not guaranteed to be
 * performed atomically. For example, an iterator operating concurrently with an
 * addAll operation might view only some of the added elements.
 * 
 * This class and its iterators implement all of the optional methods of the Set
 * and Iterator interfaces. Like most other concurrent collection
 * implementations, this class does not permit the use of null elements, because
 * null arguments and return values cannot be reliably distinguished from the
 * absence of elements.
 * 
 * This class is a member of the Java Collections Framework.
 * 
 * {@link https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ConcurrentSkipListSet.html}"
 * 
 * 
 * @author cazucito
 *
 */
public class ConcurrentSkipListSetDemo {

	/**
	 * Probando constructores
	 */
	public static void testConstructores() {
		/////////////////
		// CONSTRUCTORES
		Impresor.muestraEnConsola(TipoMensajes.SUBTITULO, "CONSTRUCTORES");
		// ConcurrentSkipListSet() => Constructs a new, empty set that orders its
		// elements according to their natural ordering.
		ConcurrentSkipListSet<String> csls01 = new ConcurrentSkipListSet<>();
		csls01.add("Uno");
		csls01.add("Dos");
		csls01.add("Tres");
		Impresor.muestraEnConsola(TipoMensajes.DEPURACION, csls01.toString());
		// ConcurrentSkipListSet(Collection<? extends E> c) => Constructs a new set
		// containing the elements in the specified collection, that orders its elements
		// according to their natural ordering.
		List<String> coleccion = new ArrayList<>();
		coleccion.add("Uno");
		coleccion.add("Dos");
		coleccion.add("Tres");
		NavigableSet<String> csls02 = new ConcurrentSkipListSet<>(coleccion);
		Impresor.muestraEnConsola(TipoMensajes.DEPURACION, csls02.toString());
		// ConcurrentSkipListSet(Comparator<? super E> comparator) => Constructs a new,
		// empty set that orders its elements according to the specified comparator.
		Comparator<String> comparator = (str1, str2) -> (str1.length() > str2.length()) ? 1
				: (str1.length() == str2.length()) ? 0 : -1;
		NavigableSet<String> csls03 = new ConcurrentSkipListSet<>(comparator);
		csls03.add("4444");
		csls03.add("333");
		csls03.add("55555");
		csls03.add("22");
		csls03.add("22");
		Impresor.muestraEnConsola(TipoMensajes.DEPURACION, csls03.toString());
		// ConcurrentSkipListSet(SortedSet<E> s) => Constructs a new set containing the
		// same elements and using the same ordering as the specified sorted set.
		SortedSet<String> coleccion2 = new TreeSet<>();
		coleccion2.add("Uno");
		coleccion2.add("Dos");
		coleccion2.add("Tres");
		coleccion2.add("Tres");
		ConcurrentSkipListSet<String> csls04 = new ConcurrentSkipListSet<>(coleccion2);
		Impresor.muestraEnConsola(TipoMensajes.DEPURACION, csls04.toString());
	}

	/**
	 * Prueba ConcurrentSkipListSet
	 */
	public static void testConcurrentSkipListSet() {
		// SortedSet
		Impresor.muestraEnConsola(TipoMensajes.SUBTITULO, "SortedSet - FAIL-FAST");
		final SortedSet<String> ts = new TreeSet<>(Arrays.asList("Uno", "Dos", "Tres"));
		ts.add("Tres");
		Impresor.muestraEnConsola(TipoMensajes.MENSAJE_OK, ".add(\"Tres\") no altera la coleccion");
		ExecutorService hilos1 = Executors.newCachedThreadPool();
		hilos1.execute(new AdicionadorAColeccionNoSincronizado(ts, "CUATRO"));
		hilos1.shutdown();
		Impresor.imprime(ts);
		Impresor.muestraEnConsola(TipoMensajes.MENSAJE, "Estado final: " + ts.toString());
		// CopyOnWriteArrayList
		Impresor.muestraEnConsola(TipoMensajes.SUBTITULO, "ConcurrentSkipListSet - FAIL-SAFE");
		final ConcurrentSkipListSet<String> csls = new ConcurrentSkipListSet<>(Arrays.asList("Uno", "Dos", "Tres"));
		csls.add("Tres");
		Impresor.muestraEnConsola(TipoMensajes.MENSAJE_OK, ".add(\"Tres\") no altera la coleccion");
		ExecutorService hilos2 = Executors.newCachedThreadPool();
		hilos2.execute(new AdicionadorAColeccionNoSincronizado(csls, "CUATRO"));
		hilos2.shutdown();
		Impresor.imprime(csls);
		Impresor.muestraEnConsola(TipoMensajes.MENSAJE, "Estado final: " + csls.toString());
	}

}