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
			Thread.sleep(ThreadLocalRandom.current().nextInt(0, 200));
			mapa.put(llave, valor);
			Impresor.muestraEnConsola(TipoMensajes.DEPURACION, "adición de '" + llave + "/" + valor + "' al mapa: " + mapa.toString());
		} catch (Exception ex) {
			System.out.println("\tEX: " + ex.getMessage());
		}
	}

}