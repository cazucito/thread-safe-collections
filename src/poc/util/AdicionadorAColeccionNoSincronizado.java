package poc.util;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Adiciona el elemento pasado como parámetro (dato), a la colección (coleccion)
 * La modificación no es sincronizada
 * 
 * @author cazucito
 *
 */
public class AdicionadorAColeccionNoSincronizado implements Runnable {
	private Collection<String> coleccion;
	private String dato;

	public AdicionadorAColeccionNoSincronizado(Collection<String> coleccion, String dato) {
		this.coleccion = coleccion;
		this.dato = dato;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(ThreadLocalRandom.current().nextInt(0, 200));
			coleccion.add(dato);
		} catch (Exception ex) {
			Impresor.muestraEnConsola(TipoMensajes.EXCEPCION, ex.toString());
		} finally {
			Impresor.muestraEnConsola(TipoMensajes.DEPURACION, "adición de '" + dato + "' a la colección: " + coleccion.toString());
		}

	}

}