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
public class AdicionadorAColeccionSincronizado implements Callable<Collection<String>> {
	private Collection<String> coleccion;
	private String dato;

	public AdicionadorAColeccionSincronizado(Collection<String> coleccion, String dato) {
		this.coleccion = coleccion;
		this.dato = dato;
	}

	@Override
	public Collection<String> call() {
		try {
			synchronized (coleccion) {
				Thread.sleep(ThreadLocalRandom.current().nextInt(0, 200));
				coleccion.add(dato);
			}
		} catch (Exception ex) {
			System.out.println("\tEX: " + ex.getMessage());
		}
		return coleccion;
	}

}