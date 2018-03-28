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
		System.out.println("||=== CONSTRUCTORES ConcurrentSkipListSet() ===||");
		// ConcurrentSkipListSet() => Constructs a new, empty set that orders its
		// elements according to their natural ordering.
		ConcurrentSkipListSet<String> cowal01 = new ConcurrentSkipListSet<>();
		cowal01.add("Uno");
		cowal01.add("Dos");
		cowal01.add("Tres");
		System.out.println(cowal01);
		// ConcurrentSkipListSet(Collection<? extends E> c) => Constructs a new set
		// containing the elements in the specified collection, that orders its elements
		// according to their natural ordering.
		List<String> coleccion = new ArrayList<>();
		coleccion.add("Uno");
		coleccion.add("Dos");
		coleccion.add("Tres");
		NavigableSet<String> cowal02 = new ConcurrentSkipListSet<>(coleccion);
		System.out.println(cowal02);
		// ConcurrentSkipListSet(Comparator<? super E> comparator) => Constructs a new,
		// empty set that orders its elements according to the specified comparator.
		Comparator<String> comparator = (str1, str2) -> (str1.length() > str2.length()) ? 1
				: (str1.length() == str2.length()) ? 0 : -1;
		NavigableSet<String> cowal03 = new ConcurrentSkipListSet<>(comparator);
		cowal03.add("4444");
		cowal03.add("333");
		cowal03.add("55555");
		cowal03.add("22");cowal03.add("22");
		System.out.println(cowal03);
		// ConcurrentSkipListSet(SortedSet<E> s) => Constructs a new set containing the same elements and using the same ordering as the specified sorted set.
		SortedSet<String> coleccion2 = new TreeSet<>();
		coleccion2.add("Uno");
		coleccion2.add("Dos");
		coleccion2.add("Tres");coleccion2.add("Tres");
		ConcurrentSkipListSet<String> cowal04 = new ConcurrentSkipListSet<>(coleccion2);
		System.out.println(cowal04);
	}

	/**
	 * Prueba ConcurrentSkipListSet
	 */
	public static void testConcurrentSkipListSet() {
		/////////////////
		// Piscina de hilos
		ExecutorService hilos = Executors.newCachedThreadPool();
		// SortedSet
		System.out.println("||=== SortedSet ===||");
		final SortedSet<String> numeros = new TreeSet<>(Arrays.asList("Uno", "Dos", "Tres"));
		numeros.add("Tres");
		hilos.execute(new AdicionadorAColeccionNoSincronizado(numeros, "CUATRO"));
		Impresor.imprime(numeros);
		// CopyOnWriteArrayList
		System.out.println("||=== ConcurrentSkipListSet ===||");
		final ConcurrentSkipListSet<String> numeros2 = new ConcurrentSkipListSet<>(Arrays.asList("Uno", "Dos", "Tres"));
		numeros2.add("Tres");
		hilos.execute(new AdicionadorAColeccionNoSincronizado(numeros2, "CUATRO"));
		Impresor.imprime(numeros2);
	}

}