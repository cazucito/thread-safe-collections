package poc.util;

import java.util.Collection;
import java.util.Map;
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
	/**
	 * Manda a imprimir una colección mediante el llamado a so método toString()
	 * 
	 * @param coleccion
	 */
	public static void imprimeToString(Collection coleccion) {
		System.out.println("\t- Tamaño:" + coleccion.size() + " Datos: " + coleccion.toString());
	}
	/**
	 * Manda a imprimir un mapa mediante el llamado a so método toString()
	 * 
	 * @param Mapa
	 */
	public static void imprimeToString(Map mapa) {
		System.out.println("\t- Tamaño:" + mapa.size() + " Datos: " + mapa.toString());
	}
}
