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
		String colStr = "[]";
		try {
			Temporizador.pausar(100);
			coleccion.add(dato);
			colStr = coleccion.toString();
		} catch (Exception ex) {
			Impresor.muestraEnConsola(TipoMensajes.EXCEPCION, ex.toString());
		} finally {
			Impresor.muestraEnConsola(TipoMensajes.DEPURACION, "adición de '" + dato + "' a la colección: " + colStr);
		}

	}

}