package poc.util;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
/**
 * Utilería para la impresión de colecciones
 * @author cazucito
 *
 */
public class Impresor {
	/**
	 * Manda a imprimir una colección de referencias a objetos tipo String
	 * 
	 * @param coleccion
	 */
	public static void imprime(Collection<String> coleccion) {
		try {
			System.out.print("\t< ");
			for (String str : coleccion) {
				System.out.print(str + " ");
				// "Pausa" el hilo un lapso de 0-200 milisegundos
				try {
					Thread.sleep(ThreadLocalRandom.current().nextInt(0, 200));
				} catch (Exception ex) {
					System.out.println("\tEX: " + ex.getMessage());
				}
			}
			System.out.println(">");
		} catch (Exception ex) {
			System.out.println("\tEx: " + ex);
		}
	}
}
