package poc.util;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Adiciona el elemento pasado como parámetro (dato), a la colección (coleccion)
 * @author cazucito
 *
 */
public class ModificadorDeColeccion implements Runnable {
	private Collection<String> coleccion;
	private String dato;

	public ModificadorDeColeccion(Collection<String> coleccion, String dato) {
		this.coleccion = coleccion;
		this.dato = dato;
	}

	@Override
	public void run() {
		try {
			// "Pausa" el hilo un lapso de 0-200 milisegundos
			Thread.sleep(ThreadLocalRandom.current().nextInt(0, 200));
		} catch (Exception ex) {
			System.out.println("\tEX: " + ex.getMessage());
		}
		coleccion.add(dato);
		System.out.println("\n\t\tNumeros: " + coleccion);
	}

}