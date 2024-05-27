package logica;


import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import cu.edu.cujae.ceis.graph.LinkedGraph;
import cu.edu.cujae.ceis.graph.edge.Edge;
import cu.edu.cujae.ceis.graph.edge.WeightedEdge;
import cu.edu.cujae.ceis.graph.vertex.Vertex;


public class Knn extends AlgoritmoK {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int k;

	public Knn(int k) {
		super();
		this.k = k;
		
		// TODO Auto-generated constructor stub
	}


	public LinkedGraph getGrafo() {

		return this.getGrafo();
	}

	public void setGrafo(LinkedGraph grafo) {
		this.grafo = grafo;

	}
	
	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}


	public LinkedList<Vertex> getVerticesList() {

		return this.grafo.getVerticesList();
	}

	// Metodo que aplica el algoritmo KNN para la clasificación de los vértices "no clasificados" del grafo

	public void ejecutarAlgoritmo() {
		Iterator<Vertex> iterVertices = this.grafo.getVerticesList().iterator();

		while (iterVertices.hasNext()) {
			Vertex aux = iterVertices.next(); // se obtiene al siguiente vertice
			if (!((Elemento) aux.getInfo()).isClasificado()) // si el vertice es no clasificado
				this.clasificarVertice(aux); // se clasifica el vertice no clasificado de acuerdo al algoritmo KNN
		}
	}


	// Metodo para clasificar un vertice no clasificado del grafo
	private void clasificarVertice (Vertex vertex) {

		Iterator<Edge> vecinosVertex = vertex.getEdgeList().iterator(); // se obtienen las aristas adjacentes para encontrar a los K-vecinos más cercanos
		PriorityQueue<WeightedEdge> aristasMasCercanas = new PriorityQueue<WeightedEdge>(); // se crea una cola de prioridad para almacenar las aristas en orden ascendente (menor a mayor)
		Queue<Vertex> kVecinosMasCercanos = new ArrayDeque<Vertex>(); // cola de los K vecinos más cercanos
		HashMap<Clase, Integer> clases = new HashMap<Clase, Integer>(); // se crea un mapa para almacenar las repeticiones
		Clase claseSeleccionada = null; // esta variable representa la clase que será asignada al vértice como resultado de la ejecución del algoritmo
		// se iteran la lista de vecinos para obtener a los k-vecinos mas cercanos
		// para ello se insertan las aristas vecinas en una cola de prioridad 
		while (vecinosVertex.hasNext()) {
			WeightedEdge aux = (WeightedEdge) vecinosVertex.next(); // se obtiene a la arista
			aristasMasCercanas.offer(aux);
		}

		// luego se obtienen los elementos primeros en la cola de prioridad hasta el valor K seleccionado
		int count = 0;
		while (!aristasMasCercanas.isEmpty() && count++ < this.k) {
			Vertex vertexAux = aristasMasCercanas.poll().getVertex(); // se obtiene el vértice al cual apunta la arista
			Clase clase = ((Elemento) vertexAux.getInfo()).getClase();

			// se almacena en la cola de los K vecinos mas cercanos
			kVecinosMasCercanos.offer(vertexAux);
			// se realiza el conteo por clases
			if (clases.containsKey(clase)) // si ya existe la clase
				clases.put(clase, clases.get(clase) + 1);  // se incrementa el conteo
			else
				clases.put(clase, 1); // se añade como nueva llave con una sola repitencia
		}

		// Se determina la clase con mayor repitencia
		// Para ello se itera el mapa
		int mayorRepitencia = -1;
		LinkedList<Clase> clasesEmpatadas = new LinkedList<Clase>(); // se crea una lista para guardar las clases empatadas
		for (Map.Entry<Clase, Integer> entrada : clases.entrySet()) {

			Integer valor = entrada.getValue();
			if (valor > mayorRepitencia) {
				mayorRepitencia = valor;
				clasesEmpatadas.clear();
				clasesEmpatadas.add(entrada.getKey());
			}
			else if (valor == mayorRepitencia) { // en caso de empate
				clasesEmpatadas.add(entrada.getKey()); // se añade a la lista de clases empatadas
			}
		}


		// se procesa cola de los k-vecinos más cercanos para buscar el de menor distancia en caso de empates
		while (!kVecinosMasCercanos.isEmpty() && claseSeleccionada == null) { // mientras la cola no haya sido procesada y no se haya seleccionado una clase para el vértice
			Vertex vertexAux = kVecinosMasCercanos.poll();
			Clase claseVertexAux = ((Elemento) vertexAux.getInfo()).getClase(); // se obtiene la clase del vertice a procesar
			// si la clase del vertice a procesar se encuentra dentro de la lista de empates significa que es la clase del vértice más cercano al vértice a clasificar

			if (clasesEmpatadas.contains(claseVertexAux))
				claseSeleccionada = claseVertexAux;
		}

		// Se clasifica el vértice en cuestión con la clase seleccionada como resultado de la aplicación del algoritmo

		((Elemento) vertex.getInfo()).setClase(claseSeleccionada);

	}

	
	// Operaciones

	// Metodo para insertar un conjunto de elementos en el grafo
	public void addElementos (List<Elemento> elementos) throws Exception {
		// antes de insertar se comprueba que ningun elemento se encuentre en el grafo
		if (!siHayAlMenosUnElementoEnGrafo(elementos)) { // si no hay al menos un elemento que ya se encuentre en el grafo
			for (Elemento elemento : elementos) {
				if (elemento.isClasificado()) // si es un elemento clasificado
					this.addElemento(elemento.getId(), elemento.getX(), elemento.getY(), elemento.getClase().getNombre());
				else
					this.addElemento(elemento.getId(), elemento.getX(), elemento.getY());
			}
		}
		else
			throw new Exception("No se permiten vertices con elementos duplicados");
	}

	private boolean siHayAlMenosUnElementoEnGrafo (List<Elemento> elementos) {
		boolean veredicto = false;
		Iterator<Elemento> iter = elementos.iterator();

		while (iter.hasNext() && !veredicto) {
			if (this.buscarVerticeByElementoId(iter.next().getId()) != null) // si existe un vertice que tenga un elemento con ese id
				veredicto = true; // se indica que si existe al menos un vértice
		}

		return veredicto;
	}


	


	// Metodo para insertar un elemento clasificado en el grafo
	public void addElemento (String id, float x, float y, String nombreClase) throws Exception {
		if (this.buscarVerticeByElementoId(id) == null) { // si no existe un vértice con un elemento repetido
			Clase clase = new Clase(nombreClase);
			// se añade la clase a la lista de clases
			this.addClase(clase);
			this.grafo.insertVertex(new Elemento(id, x, y, clase));
			// se trazan las aristas de distancia entre los vertices no clasificados del grafo con  los vértices clasificados
			this.trazarAristasDistanciasClasificado(this.grafo.getVerticesList().getLast());
			this.ejecutarAlgoritmo(); // se aplica el algoritmo KNN despues de la insercción del vertice
		}
		else
			throw new Exception("No se permiten vertices con elementos duplicados");
	}


	// Metodo para trazar las aristas de distancia del vertice insertado a los demas vertices del grafo
			protected void trazarAristasDistancias (Vertex vertex) {
				Iterator<Vertex> vertices = this.grafo.getVerticesList().iterator();
				Elemento elementoInicial = (Elemento) vertex.getInfo(); // se obtiene el elemento inicial
				// Se recorre cada vertice y se trazan las aristas de distancia correspondientes
				while (vertices.hasNext()) {
					Vertex aux = vertices.next();
					Elemento elementoFinal = (Elemento) aux.getInfo(); // se obtiene al elemento final
					if (!aux.equals(vertex)) // si el nodo a trazar es distinto del nodo de la iteración 
						this.grafo.insertWEdgeDG(this.grafo.getVerticesList().indexOf(vertex), 
								this.grafo.getVerticesList().indexOf(aux), this.distanciaEuclidiana(elementoInicial.getX(), elementoInicial.getY(), 
										elementoFinal.getX(), elementoFinal.getY()));
				}
			}

	// Metodo para trazar las aristas de distancia de los nodos no clasificados al nodo clasificado insertado
	private void trazarAristasDistanciasClasificado (Vertex vertex) {
		Iterator<Vertex> vertices = this.grafo.getVerticesList().iterator();
		Elemento elementoInicial = (Elemento) vertex.getInfo(); // se obtiene el elemento inicial
		// Se recorre cada vertice y se trazan las aristas de distancia correspondientes
		while (vertices.hasNext()) {
			Vertex aux = vertices.next();
			Elemento elementoFinal = (Elemento) aux.getInfo(); // se obtiene al elemento final
			if (!aux.equals(vertex) && !elementoFinal.isClasificado()) // si el nodo no es clasificado y además si el nodo es distinto del nodo al que se le van a trazar las aristas
				this.grafo.insertWEdgeDG(this.grafo.getVerticesList().indexOf(aux), 
						this.grafo.getVerticesList().indexOf(vertex), this.distanciaEuclidiana(elementoInicial.getX(), elementoInicial.getY(), 
								elementoFinal.getX(), elementoFinal.getY()));
		}
	}

	// Metodo para elminar un vertice del grafo

	public void eliminarVertice (Vertex vertex) {
		int posVertex = this.grafo.getVerticesList().indexOf(vertex); // se obtiene la posicion del vertice a eliminar
		if (posVertex != -1) { // si fue encontrado el vertice a eliminar
			Vertex vertexEliminado = this.grafo.deleteVertex(posVertex); // se elimina el vertice del grafo
			// si fue eliminado un nodo clasificado entonces se elimina igualmente la clase de la lista de clases
			if (vertexEliminado != null) { // si fue eliminado el vértice correctamente
				Elemento elemento = (Elemento) vertexEliminado.getInfo(); // se obtiene al elemento de la clase
				if (elemento.isClasificado()) // si el elemento eliminado es un clasificado
					this.clases.remove(elemento.getClase()); // se elimina la clase del elemento de la lista de clases

				this.ejecutarAlgoritmo(); // se ejecuta el algoritmo KNN para actualizar la información de las clases
			}
		}
	}

}
