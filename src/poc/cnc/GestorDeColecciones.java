package poc.cnc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import poc.util.TipoMensajes;
import poc.util.Impresor;
import poc.util.AdicionadorAColeccionNoSincronizado;
import poc.util.AdicionadorAColeccionSincronizado;
import poc.util.Temporizador;

public class GestorDeColecciones {
	private static final int CANTIDAD_DE_HILOS = 7;

	/**
	 * Adiciona (de manera no sincronizada) un elemento a la colección
	 */
	public static void adicionNoSincronizada() {
		Impresor.muestraEnConsola(TipoMensajes.SUBTITULO, "ArrayList - No sincronizado");
		List<String> coleccion = new ArrayList<>();
		ExecutorService hilos = Executors.newCachedThreadPool();
		for (int i = 1; i <= CANTIDAD_DE_HILOS; i++) {
			hilos.execute(new AdicionadorAColeccionNoSincronizado(coleccion, "" + i));
		}
		hilos.shutdown();
		Temporizador.pausar(1000);
		int cantidadElementos = coleccion.size();
		if (CANTIDAD_DE_HILOS != cantidadElementos) {
			Impresor.muestraEnConsola(TipoMensajes.ERROR_LOGICO,
					CANTIDAD_DE_HILOS + " " + '\u2260' + " " + cantidadElementos);
		} else {
			Impresor.muestraEnConsola(TipoMensajes.MENSAJE_OK, CANTIDAD_DE_HILOS + " = " + cantidadElementos);
		}

	}

	/**
	 * Adiciona (de manera sincronizada) un elemento a la colección
	 */
	public static void adicionSincronizada() {
		Impresor.muestraEnConsola(TipoMensajes.SUBTITULO, "ArrayList - synchronized");
		List<String> coleccion = new ArrayList<>();
		ExecutorService hilos = Executors.newCachedThreadPool();
		for (int i = 1; i <= CANTIDAD_DE_HILOS; i++) {
			hilos.execute(new AdicionadorAColeccionSincronizado(coleccion, "" + i));
		}
		hilos.shutdown();
		Temporizador.pausar(1000);
		int cantidadElementos = coleccion.size();
		if (CANTIDAD_DE_HILOS != cantidadElementos) {
			Impresor.muestraEnConsola(TipoMensajes.ERROR_LOGICO,
					CANTIDAD_DE_HILOS + " " + '\u2260' + " " + cantidadElementos);
		} else {
			Impresor.muestraEnConsola(TipoMensajes.MENSAJE_OK, CANTIDAD_DE_HILOS + " = " + cantidadElementos);
		}
	}

	/**
	 * Adiciona (de manera sincronizada) un elemento a la colección
	 */
	public static void adicionSincronizadaUtileria() {
		Impresor.muestraEnConsola(TipoMensajes.SUBTITULO, "ArrayList - Collections.synchronizedList()");
		List<String> coleccion = new ArrayList<>();
		ExecutorService hilos = Executors.newCachedThreadPool();
		coleccion = Collections.synchronizedList(coleccion);
		for (int i = 1; i <= CANTIDAD_DE_HILOS; i++) {
			hilos.execute(new AdicionadorAColeccionSincronizado(coleccion, " " + i));
		}
		hilos.shutdown();
		Temporizador.pausar(1000);
		int cantidadElementos = coleccion.size();
		if (CANTIDAD_DE_HILOS != cantidadElementos) {
			Impresor.muestraEnConsola(TipoMensajes.ERROR_LOGICO,
					CANTIDAD_DE_HILOS + " " + '\u2260' + " " + cantidadElementos);
		} else {
			Impresor.muestraEnConsola(TipoMensajes.MENSAJE_OK, CANTIDAD_DE_HILOS + " = " + cantidadElementos);
		}
	}
}
