package poc.cc;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import poc.util.Impresor;
import poc.util.ModificadorDeMapa;

/**
 * DEMO
 * 
 * public class ConcurrentSkipListMap<K,V> extends AbstractMap<K,V> implements
 * ConcurrentNavigableMap<K,V>, Cloneable, Serializable
 * 
 * A scalable concurrent ConcurrentNavigableMap implementation. The map is
 * sorted according to the natural ordering of its keys, or by a Comparator
 * provided at map creation time, depending on which constructor is used.
 * 
 * This class implements a concurrent variant of SkipLists providing expected
 * average log(n) time cost for the containsKey, get, put and remove operations
 * and their variants. Insertion, removal, update, and access operations safely
 * execute concurrently by multiple threads.
 * 
 * Iterators and spliterators are weakly consistent.
 * 
 * Ascending key ordered views and their iterators are faster than descending
 * ones.
 * 
 * All Map.Entry pairs returned by methods in this class and its views represent
 * snapshots of mappings at the time they were produced. They do not support the
 * Entry.setValue method. (Note however that it is possible to change mappings
 * in the associated map using put, putIfAbsent, or replace, depending on
 * exactly which effect you need.)
 * 
 * Beware that bulk operations putAll, equals, toArray, containsValue, and clear
 * are not guaranteed to be performed atomically. For example, an iterator
 * operating concurrently with a putAll operation might view only some of the
 * added elements.
 * 
 * This class and its views and iterators implement all of the optional methods
 * of the Map and Iterator interfaces. Like most other concurrent collections,
 * this class does not permit the use of null keys or values because some null
 * return values cannot be reliably distinguished from the absence of elements.
 * 
 * This class is a member of the Java Collections Framework.
 * 
 * {@link https://docs.oracle.com/javase/10/docs/api/java/util/concurrent/ConcurrentSkipListMap.html}"
 * 
 * @author cazucito
 *
 */
public class ConcurrentSkipListMapDemo {

	/**
	 * Probando constructores
	 */
	public static void testConstructores() {
		/////////////////
		// CONSTRUCTORES
		System.out.println("||=== CONSTRUCTORES ConcurrentSkipListMap() ===||");
		// ConcurrentSkipListMap() => Constructs a new, empty map, sorted according to
		// the natural ordering of the keys.
		ConcurrentNavigableMap<Integer, String> cslm1 = new ConcurrentSkipListMap<>();
		cslm1.put(1, "UNO");
		cslm1.put(2, "DOS");
		cslm1.put(3, "TRES");
		Impresor.imprimeToString(cslm1);
		// ConcurrentSkipListMap(Comparator<? super K> comparator) => Constructs a new,
		// empty map, sorted according to the specified comparator.
		Comparator<Integer> comparator = (k1, k2) -> (k1 > k2) ? -1 : (k1 == k2) ? 0 : 1;
		ConcurrentNavigableMap<Integer, String> cslm2 = new ConcurrentSkipListMap<>(comparator);
		cslm2.put(1, "UNO");
		cslm2.put(2, "DOS");
		cslm2.put(3, "TRES");
		Impresor.imprimeToString(cslm2);
		// ConcurrentSkipListMap(Map<? extends K,? extends V> m) => Constructs a new map
		// containing the same mappings as the given map, sorted according to the
		// natural ordering of the keys.
		Map<Integer, String> datos1 = new HashMap<>();
		datos1.put(1, "UNO");
		datos1.put(2, "DOS");
		datos1.put(3, "TRES");
		ConcurrentNavigableMap<Integer, String> cslm3 = new ConcurrentSkipListMap<>(datos1);
		Impresor.imprimeToString(cslm3);
		// ConcurrentSkipListMap(SortedMap<K,? extends V> m) => Constructs a new map
		// containing the same mappings and using the same ordering as the specified
		// sorted map.
		Comparator<Integer> comparator2 = (k1, k2) -> (k1 > k2) ? -1 : (k1 == k2) ? 0 : 1;
		SortedMap<Integer, String> datos2 = new TreeMap<>(comparator2);
		datos2.put(1, "UNO");
		datos2.put(2, "DOS");
		datos2.put(3, "TRES");
		datos2.put(4, "CUATRO");
		ConcurrentNavigableMap<Integer, String> cslm4 = new ConcurrentSkipListMap<>(datos2);
		Impresor.imprimeToString(cslm4);

	}

	/**
	 * Prueba ConcurrentSkipListSet
	 */
	public static void testConcurrentSkipListMap() {
		/////////////////
		// Piscina de hilos
		ExecutorService hilos = Executors.newCachedThreadPool();
		// TreeMap
		System.out.println("||=== TreeMap ===||");
		final Map<Integer, String> mapa = new TreeMap<>();
		mapa.put(1, "UNO");
		mapa.put(2, "DOS");
		mapa.put(3, "TRES");
		Impresor.imprimeToString(mapa);
		hilos.execute(new ModificadorDeMapa(mapa, 4, "CUATRO"));
		Impresor.imprime(mapa);
		// CopyOnWriteArrayList
		System.out.println("||=== ConcurrentSkipListMap ===||");
		ConcurrentNavigableMap<Integer, String> cslm = new ConcurrentSkipListMap<>();
		cslm.put(1, "UNO");
		cslm.put(2, "DOS");
		cslm.put(3, "TRES");
		Impresor.imprimeToString(cslm);
		hilos.execute(new ModificadorDeMapa(cslm, 4, "CUATRO"));
		Impresor.imprime(cslm);
	}

}