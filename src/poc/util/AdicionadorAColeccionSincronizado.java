package poc.util;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Adiciona el elemento pasado como parámetro (dato), a la colección (coleccion)
 * 
 * @author cazucito
 *
 */
public class AdicionadorAColeccionSincronizado implements Runnable {
	private Collection<String> coleccion;
	private String dato;

	public AdicionadorAColeccionSincronizado(Collection<String> coleccion, String dato) {
		this.coleccion = coleccion;
		this.dato = dato;
	}
	/**
	 * Adiciona (de manera sincronizada) un elemento a la colección 
	 */
	@Override
	public void run() {
		String colStr = "[]";
		try {
			synchronized (coleccion) {
				Temporizador.pausar(100);
				coleccion.add(dato);
				colStr = coleccion.toString();
			}
		} catch (Exception ex) {
			Impresor.muestraEnConsola(TipoMensajes.EXCEPCION, ex.toString());
		} finally {
			Impresor.muestraEnConsola(TipoMensajes.DEPURACION,
					"adición de '" + dato + "' a la colección: " + colStr);
		}
	}

}