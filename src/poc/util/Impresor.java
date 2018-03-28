package poc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utilería para la impresión de mensajes
 * 
 * @author cazucito
 *
 */
public class Impresor {
	
	private final static String formatoEncabezado = "||:::::::|%70s ||%n||:::::::|%70s ||%n||:::::::|%70s ||%n";
	private final static String formatoPie = "||:::::::|%70s ||%n";
	private final static String formatoTitulo = "||:::::::|%70s ||%n";
	private final static String formatoSubtitulo = "         |%70s  |%n";
	private final static String formatoMensaje = "         | %-70s  |%n";
	private final static String formatoInfo = "     info| %-70s  |%n";
	private final static String formatoErrorLogico = "      bug| %-70s |%n";
	private final static String formatoMensajeOK = "       ok| %-70s |%n";
	private final static String formatoExcepcion = "       ex| %-70s  |%n";
	private final static String formatoError = "    error| %-70s  |%n";
	private final static String formatoDepuracion = "    debug| %-70s |%n";
	private static boolean depuracion = false;
	/**
	 * Habilita mensajes de depuración
	 */
	public static void habilitaDepuracion() {
		Impresor.depuracion = true;
	}
	/**
	 * Deshabilita mensajes de depuración
	 */
	public static void deshabilitaDepuracion() {
		Impresor.depuracion = false;
	}
	/**
	 * Bandera para indicar está en modo de depuración
	 * @return
	 */
	public static boolean isDepuracion() {
		return depuracion;
	}

	/**
	 * Imprime una colección de referencias a objetos tipo String
	 * 
	 * @param coleccion La colección a imprimir
	 */
	public static void imprime(Collection<String> coleccion) {
		try {
			System.out.print("\t< ");
			for (String str : coleccion) {
				System.out.println("\t" + str);
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
	 * Imprime un mapa de Integer / String
	 * 
	 * @param mapa El mapa a imprimir
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

	/**
	 * 
	 */
	public static void muestraEnConsola(FormatoMensajes _tipoMensaje, String... mensajes) {
		String tipoMensaje = "";
		List<String> msj = new ArrayList<String>(Arrays.asList(mensajes));
		switch (_tipoMensaje) {
		case ENCABEZADO:
			msj.add(0, "=====================================================================");
			msj.add("---------------------------------------------------------------------");
			tipoMensaje = formatoEncabezado;
			break;
		case PIE:
			tipoMensaje = formatoPie;
			break;
		case TITULO:
			tipoMensaje = formatoTitulo;
			break;
		case SUBTITULO:
			tipoMensaje = formatoSubtitulo;
			break;
		case MENSAJE:
			tipoMensaje = formatoMensaje;
			break;
		case MENSAJE_OK:
			tipoMensaje = formatoMensajeOK;
			break;
		case INFO:
			tipoMensaje = formatoInfo;
			break;
		case ERROR_LOGICO:
			tipoMensaje = formatoErrorLogico;
			break;
		case EXCEPCION:
			tipoMensaje = formatoExcepcion;
			break;
		case ERROR:
			tipoMensaje = formatoError;
			break;
		case DEPURACION:
			tipoMensaje = formatoDepuracion;
			if(!depuracion) {
				return;
			}
			break;
		}
		System.out.format(tipoMensaje, msj.toArray());
	}
}
