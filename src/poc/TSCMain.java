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
//		// CopyOnWriteArrayList
		Impresor.muestraEnConsola(TipoMensajes.TITULO, "CopyOnWriteArrayList (java.util.concurrent)");
		CopyOnWriteArrayListDemo.testConstructores();
		CopyOnWriteArrayListDemo.testCopyOnWriteArrayList();
//		// CopyOnWriteArraySet
		Impresor.muestraEnConsola(TipoMensajes.TITULO, "CopyOnWriteArraySet (java.util.concurrent)");
		CopyOnWriteArraySetDemo.testConstructores();
		CopyOnWriteArraySetDemo.testCopyOnWriteArraySet();
//		// ConcurrentSkipListSet
		Impresor.muestraEnConsola(TipoMensajes.TITULO, "ConcurrentSkipListSet (java.util.concurrent)");
		ConcurrentSkipListSetDemo.testConstructores();
		ConcurrentSkipListSetDemo.testConcurrentSkipListSet();
		// ConcurrentSkipListMapDemo
		Impresor.muestraEnConsola(TipoMensajes.TITULO, "ConcurrentSkipListMap (java.util.concurrent)");
		ConcurrentSkipListMapDemo.testConstructores();
		ConcurrentSkipListMapDemo.testConcurrentSkipListMap();
		// ConcurrentHashMapDemo
		Impresor.muestraEnConsola(TipoMensajes.TITULO, "ConcurrentHashMapDemo (java.util.concurrent)");
		ConcurrentHashMapDemo.testConstructores();
		ConcurrentHashMapDemo.testConcurrentHashMap();
		//
		Impresor.muestraEnConsola(TipoMensajes.PIE, "");
	}

}
