package poc.util;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Adiciona el elemento -(llave/valor) pasado como parámetro, al mapa
 * @author cazucito
 *
 */
public class ModificadorDeMapa implements Runnable {
	private Map<Integer, String> mapa;
	private Integer llave;
	private String valor;

	public ModificadorDeMapa(Map<Integer, String> mapa, Integer llave, String valor) {
		this.mapa = mapa;
		this.llave = llave;
		this.valor = valor;
	}

	@Override
	public void run() {
		try {
			// "Pausa" el hilo un lapso de 0-200 milisegundos
			Thread.sleep(ThreadLocalRandom.current().nextInt(0, 200));
		} catch (Exception ex) {
			System.out.println("\tEX: " + ex.getMessage());
		}
		mapa.put(llave, valor);
		//Impresor.imprimeToString(mapa);
	}

}