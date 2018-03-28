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

import poc.util.FormatoMensajes;
import poc.util.Impresor;
import poc.util.ModificadorDeColeccion;
import poc.util.ModificadorDeColeccionNoSynchronized;
import poc.util.Temporizador;

public class GestorDeColecciones {

	public static void adicionNoSincronizada() {
		Impresor.muestraEnConsola(FormatoMensajes.SUBTITULO, "ArrayList - No sincronizado");
		List<String> coleccion = new ArrayList<>();
		ExecutorService hilos = Executors.newCachedThreadPool();
		for (int i = 1; i <= 10; i++) {
			Future<Collection<String>> res = hilos.submit(new ModificadorDeColeccionNoSynchronized(coleccion, " " + i));
		}
		Temporizador.pausar(500);
		Impresor.muestraEnConsola(FormatoMensajes.ERROR_LOGICO, "10 " + '\u2260' + " " + coleccion.size());
	}

	public static void adicionSincronizada() {
		Impresor.muestraEnConsola(FormatoMensajes.SUBTITULO, "ArrayList - synchronized");
		List<String> coleccion = new ArrayList<>();
		ExecutorService hilos = Executors.newCachedThreadPool();
		for (int i = 1; i <= 10; i++) {
			hilos.execute(new ModificadorDeColeccion(coleccion, "x"));
		}
		Temporizador.pausar(500);
		Impresor.muestraEnConsola(FormatoMensajes.MENSAJE_OK, "10 = " + coleccion.size());
	}

	public static void adicionSincronizadaUtileria() {
		Impresor.muestraEnConsola(FormatoMensajes.SUBTITULO, "ArrayList - Collections.synchronizedList()");
		List<String> coleccion = new ArrayList<>();
		ExecutorService hilos = Executors.newCachedThreadPool();
		coleccion = Collections.synchronizedList(coleccion);
		for (int i = 1; i <= 10; i++) {
			Future<Collection<String>> res = hilos.submit(new ModificadorDeColeccionNoSynchronized(coleccion, " " + i));
		}
		Temporizador.pausar(500);
		Impresor.muestraEnConsola(FormatoMensajes.MENSAJE_OK, "10 = " + coleccion.size());
	}
}
