package poc.util;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Adiciona el elemento -(llave/valor) pasado como parámetro, al mapa
 * @author cazucito
 *
 */
public class AdicionadorAMapaNoSincronizado implements Runnable {
	private Map<Integer, String> mapa;
	private Integer llave;
	private String valor;

	public AdicionadorAMapaNoSincronizado(Map<Integer, String> mapa, Integer llave, String valor) {
		this.mapa = mapa;
		this.llave = llave;
		this.valor = valor;
	}

	@Override
	public void run() {
		try {
			Temporizador.pausar(0, 5000);
			mapa.put(llave, valor);
		} catch (Exception ex) {
			Impresor.muestraEnConsola(TipoMensajes.EXCEPCION, ex.toString());
		} finally {
			Impresor.muestraEnConsola(TipoMensajes.DEPURACION, "adición de '" + llave + "/" + valor + "' al mapa: " + mapa.toString());
		}
	}

}