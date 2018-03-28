/**
 * 
 */
package poc;

import poc.cc.ConcurrentHashMapDemo;
import poc.cc.ConcurrentSkipListMapDemo;
import poc.cc.ConcurrentSkipListSetDemo;
import poc.cc.CopyOnWriteArrayListDemo;
import poc.cc.CopyOnWriteArraySetDemo;
import poc.cnc.GestorDeColecciones;
import poc.util.TipoMensajes;
import poc.util.Impresor;

/**
 * @author cazucito
 *
 */
public class TSCMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Impresor.habilitaDepuracion();
		Impresor.muestraEnConsola(TipoMensajes.ENCABEZADO, "COLECCIONES SEGURAS EN AMBIENTES MULTIHILOS");
		// GestorColeccionNoSincronizada
		Impresor.muestraEnConsola(TipoMensajes.TITULO, "COLECCIONES (java.util)");
		GestorDeColecciones.adicionNoSincronizada();
		GestorDeColecciones.adicionSincronizada();
		GestorDeColecciones.adicionSincronizadaUtileria();
		// CopyOnWriteArrayList
//		CopyOnWriteArrayListDemo.testConstructores();
//		CopyOnWriteArrayListDemo.testCopyOnWriteArrayList();
//		// CopyOnWriteArraySet
//		CopyOnWriteArraySetDemo.testConstructores();
//		CopyOnWriteArraySetDemo.testCopyOnWriteArraySet();
//		// ConcurrentSkipListSet
//		ConcurrentSkipListSetDemo.testConstructores();
//		ConcurrentSkipListSetDemo.testConcurrentSkipListSet();
		// ConcurrentSkipListMapDemo
//		ConcurrentSkipListMapDemo.testConstructores();
//		ConcurrentSkipListMapDemo.testConcurrentSkipListMap();
		// ConcurrentHashMapDemo
//		ConcurrentHashMapDemo.testConstructores();
//		ConcurrentHashMapDemo.testConcurrentHashMap();

		Impresor.muestraEnConsola(TipoMensajes.PIE, "");
	}

}
