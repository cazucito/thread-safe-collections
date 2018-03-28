package poc.util;
/**
 * Utilería para servicios de tiempo
 * @author cazucito
 *
 */
public class Temporizador {
	
	/**
	 * "Pausa" el hilo en el que se ejecute la llamada la cantidad de milisegundos pasada como argumento
	 * @param millisegundos Cantidad de milisegundos a 'pausar'
	 */
	public static void pausar(int millisegundos) {
		try {
			Thread.sleep(millisegundos);
		} catch (Exception ex) {
			Impresor.muestraEnConsola(FormatoMensajes.EXCEPCION, ex.getMessage());
		}
	}
}