package logica;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;

import utils.Convert;

public class Controlador {
	private AlgoritmoK algoritmoK; // representa el algoritmo seleccionado
	private static Controlador controlador;


	private Controlador() {
		super();

	}

	public static Controlador getInstancie () {
		if (controlador == null)
			controlador = new Controlador();

		return controlador;
	}




	public AlgoritmoK getAlgoritmoK() {
		return algoritmoK;
	}

	public void setAlgoritmoK(AlgoritmoK algoritmoK) {
		this.algoritmoK = algoritmoK;
	}

	//Metodo para crear fichero csv (tanto de clasificados como de no clasificados porq al fin y al cabo ambos ficheros lo que tienen es elementos) (KNN)
	public void crearFicheroCsvElementos (ArrayList<Elemento> elementos, String rutaFichero) throws IOException {
		// Se crea un nuevo File en la ruta especificada
		File ficheroCsv = new File(rutaFichero); // se crea un fichero csv en la ruta especificada
		ficheroCsv.delete(); // se borra el fichero que exista en esta ruta para volver a escribirlo
		RandomAccessFile random = new RandomAccessFile(ficheroCsv, "rw"); // se crea un randomAccesFile para escribir en el fichero
		// se escribe el encabezado del fichero
		random.writeInt(elementos.size()); // se escribe con el tamaño de los elementos que se van a escribir

		// se escriben cada uno de los elementos en memoria externa
		for (Elemento elemento : elementos) {
			byte[] bytes = Convert.toBytes(elemento); // se obtiene el array de bytes del elemento
			random.writeInt(bytes.length); // se almacena el tamaño en bytes del elemento
			random.write(bytes); // se escriben los bytes del elemento en el fichero
		}

		random.close();
	}

	// Metodo para cargar un fichero csv (tanto de clasificados como de no clasificados porq al fin y al cabo ambos ficheros lo que tienen es elementos)

	public LinkedList<Elemento> cargarFicheroCsvElementos (String rutaFichero) throws IOException, ClassNotFoundException {
		LinkedList<Elemento> elementos = new LinkedList<Elemento>(); 
		File ficheroCsv = new File(rutaFichero); // con esto ya se obtiene el fichero de dicha ruta
		RandomAccessFile random = new RandomAccessFile(ficheroCsv, "rw"); // se crea un randomAccesFile para leer el fichero
		int cantElementos = random.readInt(); // se lee la cantidad de elementos del fichero

		// se recorren la cantidad de elementos almacenados en el fichero y se leen
		for (int i = 0; i < cantElementos; i++) {
			byte[] bytesElemento = new byte[random.readInt()]; // se crea un array de bytes cuyo tamaño es la cantidad de elementos
			random.read(bytesElemento); // se almacena en el array de bytes los bytes del elemento
			Elemento elemento = (Elemento) Convert.toObject(bytesElemento); // se crea el elemento apartir de sus bytes
			elementos.add(elemento); // se añade a la lista de elementos
		}

		random.close();
		return elementos;
	}

}
