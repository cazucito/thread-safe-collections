# DEMOSTRACIONES DE COLECCIONES SEGURAS EN AMBIENTES MULTIHILOS

Ejemplos para la implementación de colecciones en un ambiente multihilos

## INICIANDO

La clase TSCMain es el punto de entrada a la Prueba de Concepto (PoC)

### PREREQUISITOS

JDK 8 +
Acceso a la línea de comandos / IDE


### INSTALANDO

Ejecutar la clase principal

```
java poc.TSCMain
```

Actualmente se tienen demostraciones para:

**ArrayList**, **synchronized** y **Collections.synchronizedList(coleccion)**

```
		// GestorColeccionNoSincronizada
		GestorDeColecciones.adicionNoSincronizada();
		GestorDeColecciones.adicionSincronizada();
		GestorDeColecciones.adicionSincronizadaUtileria();
```

**CopyOnWriteArrayList**

```
		// CopyOnWriteArrayList
		CopyOnWriteArrayListDemo.testConstructores();
		CopyOnWriteArrayListDemo.testCopyOnWriteArrayList();
```

**CopyOnWriteArraySet**

```
		// CopyOnWriteArraySet
		CopyOnWriteArraySetDemo.testConstructores();
		CopyOnWriteArraySetDemo.testCopyOnWriteArraySet();
```

**ConcurrentSkipListSet**

```
		// ConcurrentSkipListSet
		ConcurrentSkipListSetDemo.testConstructores();
		ConcurrentSkipListSetDemo.testConcurrentSkipListSet();
```

**ConcurrentSkipListMap**

```
		// ConcurrentSkipListMapDemo
		ConcurrentSkipListMapDemo.testConstructores();
		ConcurrentSkipListMapDemo.testConcurrentSkipListMap();
```

```
		// ConcurrentHashMapDemo
		ConcurrentHashMapDemo.testConstructores();
		ConcurrentHashMapDemo.testConcurrentHashMap();
```

## DESPLIEGE

JDK 8 +

## CONSTRUIDO

* [JDK](http://www.oracle.com/technetwork/java/javase/overview/index.html) - Java Development Kit


## CONTRIBUCIONES

Por favor lea [CONTRIBUCION.md](CONTRIBUCION.md) por detalles para la colaboración en este proyecto.

## VERSIONES

Versión 0.1a 

## AUTORES

* **Pedro Cazu** - *Trabajo inicial* - [cazucito] (https://github.com/cazucito/)

Ver lista de [contribuyentes] (https://github.com/cazucito/thread-safe-collections/contributors) que participan en este proyecto.

## LICENCIA

Este proyecto esta bajo licencia GNU General Public License v3.0 - ver el archivo [LICENSE](LICENSE) para detalles.

## AGRADECIMIENTOS

* A todos los que de alguna u otra manera están involucrados
