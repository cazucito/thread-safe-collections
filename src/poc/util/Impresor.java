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
				System.out.println("\\t" + str);
				// "Pausa" el hilo un lapso de 0-200 milisegundos
				try {
					Thread.sleep(ThreadLocalRandom.current().nextInt(0, 200));
				} catch (Exception ex) {
					System.out.println("\t\tEX: " + ex.getMessage());
				}
			}
			System.out.println("\\t>");
		} catch (Exception ex) {
			System.out.println("\t\tEx: " + ex);
		}
	}
	/**
	 * Manda a imprimir un mapa
	 * 
	 * @param mapa
	 */
	public static void imprime(Map<Integer, String> mapa) {
		try {
			System.out.println("\t< ");
			for (Integer k : mapa.keySet()) {
				System.out.println("\t\t" + k + "/" + mapa.get(k) + " ");
				// "Pausa" el hilo un lapso de 0-200 milisegundos
				try {
					Thread.sleep(ThreadLocalRandom.current().nextInt(0, 200));
				} catch (Exception ex) {
					System.out.println("\t\tEX: " + ex.getMessage());
				}
			}
			System.out.println("\t>");
		} catch (Exception ex) {
			System.out.println("\t\tEx: " + ex);
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
