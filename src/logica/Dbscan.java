package logica;


import java.io.Console;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cu.edu.cujae.ceis.graph.LinkedGraph;
import cu.edu.cujae.ceis.graph.edge.Edge;
import cu.edu.cujae.ceis.graph.edge.WeightedEdge;
import cu.edu.cujae.ceis.graph.vertex.Vertex;

public class Dbscan extends AlgoritmoK {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private float radio;
	private int numeroMinPuntos;
	private Set<Vertex> allVisitados; // representan todos los vértices visitados en la ejecución del algoritmo

	public Dbscan(float radio, int numeroMinPuntos) {
		super();
		this.radio = radio;
		this.numeroMinPuntos = numeroMinPuntos;
		this.allVisitados = new HashSet<Vertex>();
	}




	public float getRadio() {
		return radio;
	}

	public void setRadio(float radio) {
		this.radio = radio;
	}

	public int getNumeroMinPuntos() {
		return numeroMinPuntos;
	}

	// Método que representa la implementación del algoritmo Dbscan
	public void setNumeroMinPuntos(int numeroMinPuntos) {
		this.numeroMinPuntos = numeroMinPuntos;
	}

	private void desclasificarElementos () {
		Iterator<Vertex> iterVertices = this.grafo.getVerticesList().iterator();

		// se recorre la lista de vertices del grafo
		while (iterVertices.hasNext()) {
			((Elemento) iterVertices.next().getInfo()).setClasificado(false); // se desclasifica el elemento
		}
	}

	public void ejecutarAlgoritmo() {
		// Lo primero se desclasificar a todos los vértices en caso de que ya se haya ejecutado al menos una vez el algoritmo
		System.out.println("Hola algoritmo");
		// se desclasifican los elementos que ya hayan sido clasificados
		this.desclasificarElementos();
		// se limpia de la lista de todos los visitados
		this.allVisitados.clear();
		Iterator<Vertex> iterVertices = this.grafo.getVerticesList().iterator();

		while (iterVertices.hasNext()) {
			Vertex aux = iterVertices.next(); // se obtiene al siguiente vertice
			if (!((Elemento) aux.getInfo()).isClasificado() && !this.allVisitados.contains(aux)) // si el vertice es no clasificado y si no ha pasado por el contexto de la densidad alcanzable
				this.ejecutarDensidadAlacanzable(aux); // se clasifica el vertice no clasificado de acuerdo al algoritmo DBSCAN
		}
		// se actualizan las clasificaciones por si un clúster desapareció
		this.actualizarClusters();
	}

	private void actualizarClusters () {
		Iterator<Clase> iterClases = this.clases.iterator();

		while (iterClases.hasNext()) {
			if (!this.isClaseInGrafo(iterClases.next())) // se esa clase no se encuentra en el grafo entonces será eliminada
				iterClases.remove();
		}
	}

	// Método para determinar si al menos un vértice tiene una clase específico
	private boolean isClaseInGrafo (Clase clase) {
		Iterator<Vertex> iter = this.grafo.getVerticesList().iterator();
		boolean encontrado = false;
		while(iter.hasNext() && !encontrado) {
			Elemento elementoAux = (Elemento) iter.next().getInfo();
			if (elementoAux.haveClasificado() && elementoAux.getClase().equals(clase)) // si el elemento ya tiene una clasificación y la clase del vértice es igual a la clase a verificar
				encontrado = true; // se indica que esa clase si se encuentra en un vértice
		}

		return encontrado;
	}

	// Metodo para ejecutar la densidad alcanzable del algoritmo Dbscan
	// Consiste en generar un "clúster" con los elementos que estén al alcance de un punto
	private void ejecutarDensidadAlacanzable (Vertex vertex) {
		Set<Vertex> visitados = new HashSet<Vertex>(); // considerar mejor usar un Set

		// se obtiene un posible cluster (en visitados)
		// despues de la ejecución de este método se tendrá la lista de nodos que forman parte de un posible clúster
		this.ejecutarDensidadAlacanzableRecurs(vertex, visitados);

		// si la cantidad de vértices del posible clúster es mayor o igual al número mínimo de puntos

		System.out.println("Cantidad un Cluser " + visitados.size());
		if (visitados.size() >= this.numeroMinPuntos) {
			this.clasificarVertices(visitados); // se clasifican los vértices visitados en el recorrido de determinación del clúster
		}
		else { // en caso de que no sea un clúster
			this.desClasificarElementosCluster(visitados); // se desclasifican los vértices
		}

	}

	// Este método es el que permite la creación del clúster con los elementos de un recorrido
	private void clasificarVertices (Set<Vertex> verticesAClasificar /* elementos del clúster*/) {
		Iterator<Vertex> iter = verticesAClasificar.iterator();
		Clase clasificacionIntoTheCluster = this.buscarClasificacionCluster(verticesAClasificar); // se busca una clasificación dentro de ese clúster si realmente existe
		if (iter.hasNext() && clasificacionIntoTheCluster == null) { // si el posible cluster no tiene una clasificación
			// Se crea una nueva clasificación
			Clase clasificacion = new Clase("Cluster " + (this.clases.size() + 1));
			// se añade la clasificación a la lista de clases
			this.clases.add(clasificacion);

			// se recorren los vértices a clasificar y se les asigna la clasificación
			for (Vertex vertex : verticesAClasificar) {
				((Elemento)   vertex.getInfo()).setClase(clasificacion);
				((Elemento)   vertex.getInfo()).setClasificado(true); // se marca como clasificado
			}
		}
		else {
			// se recorren los vértices a clasificar y se marcan como clasificados
			for (Vertex vertex : verticesAClasificar) {
				((Elemento)   vertex.getInfo()).setClasificado(true); // se marca como clasificado
				((Elemento)   vertex.getInfo()).setClase(clasificacionIntoTheCluster); // se le asignan a los clúster esa clasificación (en caso de que alguno no obstente esa clasificación)
			}
		}
	}

	private Clase buscarClasificacionCluster (Set<Vertex> verticesAClasificar) {
		Clase clasificacion = null;
		Iterator<Vertex> iter = verticesAClasificar.iterator();

		while (iter.hasNext() && clasificacion == null) {
			Elemento elemento= (Elemento) iter.next().getInfo();
			if (elemento.haveClasificado()) // si el elemento ya está clasificado
				clasificacion = elemento.getClase(); // se obtiene la clasificación del elemento del posible clúster
		}

		return clasificacion;

	}

	// Método para desclasificar los vértices de una expansión
	public void desClasificarElementosCluster (Set<Vertex> verticesAClasificar /* elementos del clúster*/) {
		// se recorren los vértices a clasificar y se les asigna la clasificación
		for (Vertex vertex : verticesAClasificar) {
			((Elemento)   vertex.getInfo()).setClase(null); // se desclasifica al vértice
		}
	}
	// El tipo de retorno de la función es para indicar a la pila de llamadas recursivas sobre la formación de un cluster
	private void ejecutarDensidadAlacanzableRecurs (Vertex vertex, Set<Vertex> visitados /* parámetro para evitar lazos infinitos */) {
		// Se marca el vértice como visitado en el contexto de ejecución de una bifuración de densidad alcanzable
		visitados.add(vertex);
		/* se marca el vértice en el contexto global de ejecución del algoritmo para saber en un futuro si un vértice en 
		 * específico pasó por el contexto de la densidad alcanzable*/
		this.allVisitados.add(vertex);
		Iterator<Edge> iterAristasAdyacentes = vertex.getEdgeList().iterator();


		// se recorren las aristas adyacentes
		while (iterAristasAdyacentes.hasNext()) {
			WeightedEdge aristaAux = (WeightedEdge) iterAristasAdyacentes.next();
			Vertex vertexAux = aristaAux.getVertex(); // se obtiene el vértice al cual apunta la arista
			if (!visitados.contains(vertexAux)  && (float) aristaAux.getWeight() <= this.radio) { /* si el vértice no ha sido visitado la distancia 
			hacia el punto vecino es menor que el radio	*/		

				this.ejecutarDensidadAlacanzableRecurs(vertexAux, visitados); // se ejecuta el mismo procedimiento con el vértice próximo que le sigue
			}
		}

	}

	@Override
	public void addElementos(List<Elemento> elementos) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void trazarAristasDistancias(Vertex vertex) {
		Iterator<Vertex> vertices = this.grafo.getVerticesList().iterator();
		Elemento elementoInicial = (Elemento) vertex.getInfo(); // se obtiene el elemento inicial
		// Se recorre cada vertice y se trazan las aristas de distancia correspondientes
		while (vertices.hasNext()) {
			Vertex aux = vertices.next();
			Elemento elementoFinal = (Elemento) aux.getInfo(); // se obtiene al elemento final
			if (!aux.equals(vertex)) // si el nodo a trazar es distinto del nodo de la iteración 
				this.grafo.insertWEdgeNDG(this.grafo.getVerticesList().indexOf(vertex), 
						this.grafo.getVerticesList().indexOf(aux), this.distanciaEuclidiana(elementoInicial.getX(), elementoInicial.getY(), 
								elementoFinal.getX(), elementoFinal.getY()));
		}
	}




	@Override
	public void eliminarVertice(Vertex vertex) {
		int posVertex = this.grafo.getVerticesList().indexOf(vertex); // se obtiene la posicion del vertice a eliminar
		if (posVertex != -1) { // si fue encontrado el vertice a eliminar
			this.grafo.deleteVertex(posVertex); // se elimina el vertice del grafo
			this.ejecutarAlgoritmo(); // se ejecuta el algoritmo KNN para actualizar la información de las clases

		}
	}




}
