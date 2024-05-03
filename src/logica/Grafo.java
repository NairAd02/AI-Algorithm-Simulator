package logica;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.*;
import cu.edu.cujae.ceis.graph.LinkedGraph;
import cu.edu.cujae.ceis.graph.edge.Edge;
import cu.edu.cujae.ceis.graph.edge.WeightedEdge;
import cu.edu.cujae.ceis.graph.interfaces.ILinkedWeightedEdgeDirectedGraph;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import utils.Convert;
import utils.ParVertex;

public class Grafo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ILinkedWeightedEdgeDirectedGraph grafoKNN;
	private LinkedList<Clase> clases; // atributo que representa las clases creadas
	private int k;
	//private ArrayList<ArrayList<Double>> distancias;

	public Grafo(int k){
		grafoKNN = new LinkedGraph();
		this.clases = new LinkedList<Clase>();
		this.k = k;


	}

	public ILinkedWeightedEdgeDirectedGraph getGrafoKNN() {
		return grafoKNN;
	}

	public void setGrafoKNN(ILinkedWeightedEdgeDirectedGraph grafoKNN) {
		this.grafoKNN = grafoKNN;
	}

	public LinkedList<Vertex> getVerticesList() {
		return this.grafoKNN.getVerticesList();
	}

	public Vertex getVertexPos (int pos) {
		Vertex vertex = null;
		if (pos >= 0 && pos < this.grafoKNN.getVerticesList().size())
			vertex = this.grafoKNN.getVerticesList().get(pos);

		return vertex;
	}


	public LinkedList<Clase> getClases() {
		return clases;
	}

	public void setClases(LinkedList<Clase> clases) {
		this.clases = clases;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
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
	// Metodo para insertar un elemento en el grafo
	public void addElemento (String id, float x, float y) throws Exception {
		if (this.buscarVerticeByElementoId(id) == null) { // si no existe un vértice con un elemento repetido
			this.grafoKNN.insertVertex(new Elemento(id, x, y));
			// se trazan las aristas de distancias con cada nodo del grafo
			this.trazarAristasDistancias(this.grafoKNN.getVerticesList().getLast()); // trazan las aristas de distancia del vertice insertado hacia los demás vértices del grafo
			this.KNN(); // se aplica el algoritmo KNN despues de la insercción del vertice
		}
		else
			throw new Exception("No se permiten vertices con elementos duplicados");
	}

	// Metodo para trazar las aristas de distancia del vertice insertado a los demas vertices del grafo
	private void trazarAristasDistancias (Vertex vertex) {
		Iterator<Vertex> vertices = this.grafoKNN.getVerticesList().iterator();
		Elemento elementoInicial = (Elemento) vertex.getInfo(); // se obtiene el elemento inicial
		// Se recorre cada vertice y se trazan las aristas de distancia correspondientes
		while (vertices.hasNext()) {
			Vertex aux = vertices.next();
			Elemento elementoFinal = (Elemento) aux.getInfo(); // se obtiene al elemento final
			if (!aux.equals(vertex)) // si el nodo a trazar es distinto del nodo de la iteración 
				this.grafoKNN.insertWEdgeDG(this.grafoKNN.getVerticesList().indexOf(vertex), 
						this.grafoKNN.getVerticesList().indexOf(aux), this.distanciaEuclidiana(elementoInicial.getX(), elementoInicial.getY(), 
								elementoFinal.getX(), elementoFinal.getY()));
		}
	}


	// Metodo para insertar un elemento clasificado en el grafo
	public void addElemento (String id, float x, float y, String nombreClase) throws Exception {
		if (this.buscarVerticeByElementoId(id) == null) { // si no existe un vértice con un elemento repetido
			Clase clase = new Clase(nombreClase);
			// se añade la clase a la lista de clases
			this.addClase(clase);
			this.grafoKNN.insertVertex(new Elemento(id, x, y, clase));
			// se trazan las aristas de distancia entre los vertices no clasificados del grafo con  los vértices clasificados
			this.trazarAristasDistanciasClasificado(this.grafoKNN.getVerticesList().getLast());
			this.KNN(); // se aplica el algoritmo KNN despues de la insercción del vertice
		}
		else
			throw new Exception("No se permiten vertices con elementos duplicados");
	}

	private boolean buscarClaseByNombre (String nombreClase) {
		boolean find = false;
		Iterator<Clase> iter = this.clases.iterator();

		while (iter.hasNext() && !find) {
			if (iter.next().getNombre().equalsIgnoreCase(nombreClase))
				find = true;
		}

		return find;

	}

	private void addClase (Clase clase) throws Exception {
		if (!buscarClaseByNombre(clase.getNombre())) // si la clase no se encuentra en la lista de clases
			this.clases.add(clase);
		else
			throw new Exception("No se permiten clases duplicadas");
	}

	// Metodo para trazar las aristas de distancia de los nodos no clasificados al nodo clasificado insertado
	private void trazarAristasDistanciasClasificado (Vertex vertex) {
		Iterator<Vertex> vertices = this.grafoKNN.getVerticesList().iterator();
		Elemento elementoInicial = (Elemento) vertex.getInfo(); // se obtiene el elemento inicial
		// Se recorre cada vertice y se trazan las aristas de distancia correspondientes
		while (vertices.hasNext()) {
			Vertex aux = vertices.next();
			Elemento elementoFinal = (Elemento) aux.getInfo(); // se obtiene al elemento final
			if (!aux.equals(vertex) && !elementoFinal.isClasificado()) // si el nodo no es clasificado y además si el nodo es distinto del nodo al que se le van a trazar las aristas
				this.grafoKNN.insertWEdgeDG(this.grafoKNN.getVerticesList().indexOf(aux), 
						this.grafoKNN.getVerticesList().indexOf(vertex), this.distanciaEuclidiana(elementoInicial.getX(), elementoInicial.getY(), 
								elementoFinal.getX(), elementoFinal.getY()));
		}
	}

	// Metodo para buscar un vertice que contenga un elemento con un id en específico
	public Vertex buscarVerticeByElementoId (String idElemento) {
		Vertex vertex = null;
		Iterator<Vertex> iter = this.grafoKNN.getVerticesList().iterator();
		while (iter.hasNext() && vertex == null) {
			Vertex aux = iter.next();
			if (((Elemento) aux.getInfo()).getId().equalsIgnoreCase(idElemento))
				vertex = aux;
		}

		return vertex;
	}


	// Metodo para insertar una arista en el grafo
	public void addArista (Vertex vertexInicial, Vertex vertexFinal) throws Exception {
		// si el vertice inicial es distinto del vertice final
		if (!vertexInicial.equals(vertexFinal)) {
			this.grafoKNN.insertWEdgeDG(this.grafoKNN.getVerticesList().indexOf(vertexInicial), this.grafoKNN.getVerticesList().indexOf(vertexFinal), 
					this.distanciaEuclidiana(((Elemento)vertexInicial.getInfo()).getX(), 
							((Elemento)vertexInicial.getInfo()).getY(), ((Elemento)vertexFinal.getInfo()).getX(), 
							((Elemento)vertexFinal.getInfo()).getY()));

		}
		else
			throw new Exception("No se puede trazar una arista hacia un mismo vertice");	

	}

	// Metodo para elminar un vertice del grafo

	public void eliminarVertice (Vertex vertex) {
		int posVertex = this.grafoKNN.getVerticesList().indexOf(vertex); // se obtiene la posicion del vertice a eliminar
		if (posVertex != -1) { // si fue encontrado el vertice a eliminar
			Vertex vertexEliminado = this.grafoKNN.deleteVertex(posVertex); // se elimina el vertice del grafo
			// si fue eliminado un nodo clasificado entonces se elimina igualmente la clase de la lista de clases
			if (vertexEliminado != null) { // si fue eliminado el vértice correctamente
				Elemento elemento = (Elemento) vertexEliminado.getInfo(); // se obtiene al elemento de la clase
				if (elemento.isClasificado()) // si el elemento eliminado es un clasificado
					this.clases.remove(elemento.getClase()); // se elimina la clase del elemento de la lista de clases

				this.KNN(); // se ejecuta el algoritmo KNN para actualizar la información de las clases
			}
		}
	}

	// Metodo para eliminar una arista del grafo

	public void eliminarArista (Vertex vertexInicial, Vertex vertexFinal) {
		// Se elimina la arista del grafo
		this.grafoKNN.deleteEdgeD(this.grafoKNN.getVerticesList().indexOf(vertexInicial), this.grafoKNN.getVerticesList().indexOf(vertexFinal));

	}

	public float distanciaEuclidiana(float x1, float y1, float x2, float y2){
		return (float)Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

	// Metodo para actualizar la operacion de una arista en tiempo de ejecucion
	public void actualizarPonderacionArista(WeightedEdge arista, Vertex vertexInicial, Vertex vertexFinal) {
		arista.setWeight(this.distanciaEuclidiana(((Elemento)vertexInicial.getInfo()).getX(), 
				((Elemento)vertexInicial.getInfo()).getY(), ((Elemento)vertexFinal.getInfo()).getX(), 
				((Elemento)vertexFinal.getInfo()).getY()));
	}

	// Metodo para buscar la "arista" mas cercana a un punto

	public ParVertex buscarAristaCercana (int x, int y)  {
		ParVertex parVertex = null;

		for (Vertex vertex : this.grafoKNN.getVerticesList()) {
			Elemento elementoInicial = (Elemento) vertex.getInfo(); // se obtiene al elemento inicial
			for (Edge edge : vertex.getEdgeList()) {
				Elemento elementoFinal = (Elemento) edge.getVertex().getInfo(); // se obtiene al elemento final
				// Variables que representan las posiciones donde se van a trazar las rectas
				int x1 = (int) (elementoInicial.getX() + elementoInicial.getAncho() / 2);
				int y1 = (int) (elementoInicial.getY() + elementoInicial.getLargo() / 2);
				int x2 = (int) (elementoFinal.getX() + elementoFinal.getAncho() / 2);
				int y2 = (int) (elementoFinal.getY() + elementoFinal.getLargo() / 2);

				// Calcula el ángulo de la línea entre los centros
				double angulo = Math.atan2(y2 - y1, x2 - x1);

				// Calcula las nuevas coordenadas de inicio y fin de la línea, teniendo en cuenta el tamaño de los elementos
				int nuevoX1 = (int) (x1 + (elementoInicial.getAncho() / 2) * Math.cos(angulo));
				int nuevoY1 = (int) (y1 + (elementoInicial.getLargo() / 2) * Math.sin(angulo));
				int nuevoX2 = (int) (x2 - (elementoFinal.getAncho() / 2) * Math.cos(angulo));
				int nuevoY2 = (int) (y2 - (elementoFinal.getLargo() / 2) * Math.sin(angulo));

				// si los puntos se encuentran cercanos a la arista
				if (Line2D.ptSegDist(nuevoX1, nuevoY1, nuevoX2, nuevoY2, x, y) < 5.0)
					parVertex = new ParVertex(vertex, edge.getVertex()); // se obtiene el par con los vertices de extremos de la arista
			}
		}

		return parVertex;
	}


	// Metodo para cambiar las posiciones del vértice
	public void actualizarPosicionesVertice (Vertex vertex, float posX, float posY) {
		((Elemento) vertex.getInfo()).setX(posX);
		((Elemento) vertex.getInfo()).setY(posY);
		this.KNN(); // se ejecuta el algoritmo KNN para actualizar las clases de acuerdo a la cercanias de los vecinos dado los nuevos valores de distancias
	}


	// Metodo para obtener la cantidad de vertices del grafo
	public int cantVertices () {
		return this.grafoKNN.getVerticesList().size();
	}

	// Metodo para obtener la cantidad de aristas del grafo

	public int cantAristas () {
		int cantAristas = 0;

		for (Vertex vertex : this.grafoKNN.getVerticesList()) {
			cantAristas += vertex.getEdgeList().size();
		}

		return cantAristas;
	}

	// Metodo que aplica el algoritmo KNN para la clasificación de los vértices "no clasificados" del grafo

	public void KNN () {
		Iterator<Vertex> iterVertices = this.grafoKNN.getVerticesList().iterator();

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

	public double DijkstraShortestPath(Vertex start, Vertex end, LinkedGraph graph) {
		// We keep track of which path gives us the shortest path for each node
		// by keeping track how we arrived at a particular node, we effectively
		// keep a "pointer" to the parent node of each node, and we follow that
		// path to the start
		double distanciaMinima = 0;
		HashMap<Vertex, Vertex> changedAt = new HashMap<>();
		changedAt.put(start, null);
		List<Vertex> visited = new ArrayList<Vertex>();

		// Keeps track of the shortest path we've found so far for every node
		HashMap<Vertex, Float> shortestPathMap = new HashMap<>();

		// Setting every node's shortest path weight to positive infinity to start
		// except the starting node, whose shortest path weight is 0
		for (Vertex node : graph.getVerticesList()) {
			if (node == start)
				shortestPathMap.put(start, 0.0F);
			else shortestPathMap.put(node, Float.POSITIVE_INFINITY);
		}

		// Now we go through all the nodes we can go to from the starting node
		// (this keeps the loop a bit simpler)
		for (Edge edge : start.getEdgeList()) {
			if(edge instanceof WeightedEdge){
				shortestPathMap.put(edge.getVertex(), (Float) ((WeightedEdge) edge).getWeight());
				changedAt.put(edge.getVertex(), start);
			}
		}

		visited.add(start);

		// This loop runs as long as there is an unvisited node that we can
		// reach from any of the nodes we could till then
		while (true) {
			Vertex currentNode = closestReachableUnvisited(shortestPathMap, graph, visited);
			// If we haven't reached the end node yet, and there isn't another
			// reachable node the path between start and end doesn't exist
			// (they aren't connected)
			if (currentNode == null) {
				System.out.println("There isn't a path between " + start.getInfo() + " and " + end.getInfo());
				return 0;
			}

			// If the closest non-visited node is our destination, we want to print the path
			if (currentNode == end) {
				System.out.println("The path with the smallest weight between "
						+ start.getInfo() + " and " + end.getInfo() + " is:");

				Vertex child = end;

				// It makes no sense to use StringBuilder, since
				// repeatedly adding to the beginning of the string
				// defeats the purpose of using StringBuilder
				String path = end.getInfo().toString();
				while(true) {
					Vertex parent = changedAt.get(child);
					if (parent == null) {
						break;
					}

					// Since our changedAt map keeps track of child -> parent relations
					// in order to print the path we need to add the parent before the child and
					// it's descendants
					path = parent.getInfo() + " " + path;
					child = parent;
				}
				System.out.println(path);
				System.out.println("The path costs: " + shortestPathMap.get(end));
				distanciaMinima = shortestPathMap.get(end);
				return distanciaMinima;
			}
			visited.add(currentNode);

			// Now we go through all the unvisited nodes our current node has an edge to
			// and check whether its shortest path value is better when going through our
			// current node than whatever we had before
			for (Edge edge : currentNode.getEdgeList()) {
				if (visited.contains(edge.getVertex()))
					continue;

				if (shortestPathMap.get(currentNode)
						+ (Float) ((WeightedEdge) edge).getWeight()
						< shortestPathMap.get(edge.getVertex())) {
					shortestPathMap.put(edge.getVertex(),
							shortestPathMap.get(currentNode) + (Float) ((WeightedEdge) edge).getWeight());
					changedAt.put(edge.getVertex(), currentNode);
				}
			}
		}
	}

	private Vertex closestReachableUnvisited(HashMap<Vertex, Float> shortestPathMap, LinkedGraph graph, List<Vertex> visited) {

		double shortestDistance = Double.POSITIVE_INFINITY;
		Vertex closestReachableNode = null;
		for (Vertex node : graph.getVerticesList()) {
			if (visited.contains(node))
				continue;

			double currentDistance = shortestPathMap.get(node);
			if (currentDistance == Double.POSITIVE_INFINITY)
				continue;

			if (currentDistance < shortestDistance) {
				shortestDistance = currentDistance;
				closestReachableNode = node;
			}
		}
		return closestReachableNode;
	}

	//Mio de mi
	public ArrayList<ArrayList<Double>> obtenerMatrizDistancias(){

		LinkedList <Vertex> lista1 = grafoKNN.getVerticesList();
		ArrayList<ArrayList<Double>> distancias = new ArrayList<ArrayList<Double>> (lista1.size()); // se crea la matriz de distancia cuyo tamaño será la cantidad de nodos del grafo
		Iterator<Vertex> iter1 = lista1.iterator();
		Vertex temp1;

		while(iter1.hasNext()){
			temp1 = iter1.next();
			LinkedList <Vertex>  lista2 = grafoKNN.getVerticesList();
			ArrayList<Double> fila = new ArrayList<>(lista2.size()); // se crea una nueva referencia de fila cuyo tamaño será la cantidad de nodos
			Iterator<Vertex> iter2 = lista2.iterator();
			Vertex temp2;
			while(iter2.hasNext()){
				temp2 = iter2.next();
				fila.add(DijkstraShortestPath(temp1, temp2, (LinkedGraph) grafoKNN));
			}

			distancias.add(fila);

		}

		return distancias;
	}

	//Fin


	// Trabajo Con Ficheros

	public void crearFicheroMatrizDistancias() throws IOException {
		File file = new File("matriz de distancias.dat"); // se obtiene al fichero en esa ruta
		file.delete(); // se elimina todo el contendio el archivo para volver a escribir el nuevo contenido
		RandomAccessFile random = new RandomAccessFile(file, "rw");
		ArrayList<ArrayList<Double>> matriz = this.obtenerMatrizDistancias(); // se obtiene la matriz de distancias del grafo
		byte[] arregloBytes;
		int tamanio;

		// Escribir dimensiones de la matriz(una sola ya que es una matriz cuadrada)
		arregloBytes = Convert.toBytes(matriz.size());
		tamanio = arregloBytes.length;
		random.writeInt(tamanio);
		random.write(arregloBytes);

		// Escribir los valores de los elementos de la matriz
		for (ArrayList<Double> row : matriz) {
			for (double value : row) {
				arregloBytes = Convert.toBytes(value);
				tamanio = arregloBytes.length;
				random.writeInt(tamanio);
				random.write(arregloBytes);
			}
		}

		random.close();
	}

	public ArrayList<ArrayList<Double>> cargarFicheroMatriz(String fileName) throws IOException {
		ArrayList<ArrayList<Double>> matriz = new ArrayList<>();

		try (RandomAccessFile file = new RandomAccessFile(fileName, "r")) {
			// Leer la dimensión de la matriz
			int byteArraySize = file.readInt();
			byte[] intBytes = new byte[byteArraySize];
			file.read(intBytes);
			int dimension = (int) Convert.toObject(intBytes);


			// Leer los valores de los elementos de la matriz
			for (int i = 0; i < dimension; i++) {
				ArrayList<Double> row = new ArrayList<>();
				for (int j = 0; j < dimension; j++) {
					byteArraySize = file.readInt();
					byte[] doubleBytes = new byte[byteArraySize];
					file.read(doubleBytes);
					double value = (double) Convert.toObject(doubleBytes);
					row.add(value);
				}
				matriz.add(row);
			}

			return matriz;
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}}




}
