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
import utils.Colors;
import utils.Convert;
import utils.ParVertex;

public abstract class AlgoritmoK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected LinkedGraph grafo;
	protected LinkedList<Clase> clases; // atributo que representa las clases creadas

	//private ArrayList<ArrayList<Double>> distancias;

	public AlgoritmoK(){
		this.grafo = new LinkedGraph();
		this.clases = new LinkedList<Clase>();
	}

	public LinkedGraph getGrafo() {

		return this.getGrafo();
	}


	public void setGrafo(LinkedGraph grafo) {
		this.grafo = grafo;	
	}

	public LinkedList<Vertex> getVerticesList() {

		return this.grafo.getVerticesList();
	}



	public abstract void addElementos (List<Elemento> elementos) throws Exception;

	// Metodo para trazar las aristas de distancia del vertice insertado a los demas vertices del grafo
	protected abstract void trazarAristasDistancias (Vertex vertex);

	// Metodo para elminar un vertice del grafo
	public abstract void eliminarVertice (Vertex vertex);

	public Vertex getVertexPos (int pos) {
		Vertex vertex = null;
		if (pos >= 0 && pos < this.grafo.getVerticesList().size())
			vertex = this.grafo.getVerticesList().get(pos);

		return vertex;
	}

	// Método para verificar la existencia de un color dentro de las clases generadas

	public boolean isSimilarColorClase (Color color /*Representa el color generado*/) {
		boolean isSimilar = false;
		Iterator<Clase> iter = this.clases.iterator();

		while (iter.hasNext() && !isSimilar) {
			Clase claseAux = iter.next();
			if (Colors.getInstancie().isSimilarColor(claseAux.getColor(), color))
				isSimilar = true;          
		}

		return isSimilar;
	}

	// Metodo para insertar un elemento en el grafo
	public void addElemento (String id, float x, float y) throws Exception {
		if (this.buscarVerticeByElementoId(id) == null) { // si no existe un vértice con un elemento repetido
			this.grafo.insertVertex(new Elemento(id, x, y));
			// se trazan las aristas de distancias con cada nodo del grafo
			this.trazarAristasDistancias(this.grafo.getVerticesList().getLast()); // trazan las aristas de distancia del vertice insertado hacia los demás vértices del grafo
			this.ejecutarAlgoritmo(); // se aplica el algoritmo KNN despues de la insercción del vertice
		}
		else
			throw new Exception("No se permiten vertices con elementos duplicados");
	}



	public LinkedList<Clase> getClases() {
		return clases;
	}

	public void setClases(LinkedList<Clase> clases) {
		this.clases = clases;
	}



	// Manipulación de clases
	protected boolean buscarClaseByNombre (String nombreClase) {
		boolean find = false;
		Iterator<Clase> iter = this.clases.iterator();

		while (iter.hasNext() && !find) {
			if (iter.next().getNombre().equalsIgnoreCase(nombreClase))
				find = true;
		}

		return find;

	}


	protected void addClase (Clase clase) throws Exception {
		if (!buscarClaseByNombre(clase.getNombre())) // si la clase no se encuentra en la lista de clases
			this.clases.add(clase);
		else
			throw new Exception("No se permiten clases duplicadas");
	}

	// Metodo para buscar un vertice que contenga un elemento con un id en específico
	public Vertex buscarVerticeByElementoId (String idElemento) {
		Vertex vertex = null;
		Iterator<Vertex> iter = this.grafo.getVerticesList().iterator();
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
			this.grafo.insertWEdgeDG(this.grafo.getVerticesList().indexOf(vertexInicial), this.grafo.getVerticesList().indexOf(vertexFinal), 
					this.distanciaEuclidiana(((Elemento)vertexInicial.getInfo()).getX(), 
							((Elemento)vertexInicial.getInfo()).getY(), ((Elemento)vertexFinal.getInfo()).getX(), 
							((Elemento)vertexFinal.getInfo()).getY()));

		}
		else
			throw new Exception("No se puede trazar una arista hacia un mismo vertice");	

	}



	// Metodo para eliminar una arista del grafo

	public void eliminarArista (Vertex vertexInicial, Vertex vertexFinal) {
		// Se elimina la arista del grafo
		this.grafo.deleteEdgeD(this.grafo.getVerticesList().indexOf(vertexInicial), this.grafo.getVerticesList().indexOf(vertexFinal));

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

		for (Vertex vertex : this.grafo.getVerticesList()) {
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
		this.ejecutarAlgoritmo(); // se ejecuta el algoritmo KNN para actualizar las clases de acuerdo a la cercanias de los vecinos dado los nuevos valores de distancias
	}


	// Metodo para obtener la cantidad de vertices del grafo
	public int cantVertices () {
		return this.grafo.getVerticesList().size();
	}

	// Metodo para obtener la cantidad de aristas del grafo

	public int cantAristas () {
		int cantAristas = 0;

		for (Vertex vertex : this.grafo.getVerticesList()) {
			cantAristas += vertex.getEdgeList().size();
		}

		return cantAristas;
	}


	// Método que representa el algoritmo (implementación en dependencia de la instancia de la clase)
	public abstract void ejecutarAlgoritmo ();


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

		LinkedList <Vertex> lista1 = grafo.getVerticesList();
		ArrayList<ArrayList<Double>> distancias = new ArrayList<ArrayList<Double>> (lista1.size()); // se crea la matriz de distancia cuyo tamaño será la cantidad de nodos del grafo
		Iterator<Vertex> iter1 = lista1.iterator();
		Vertex temp1;

		while(iter1.hasNext()){
			temp1 = iter1.next();
			LinkedList <Vertex>  lista2 = grafo.getVerticesList();
			ArrayList<Double> fila = new ArrayList<>(lista2.size()); // se crea una nueva referencia de fila cuyo tamaño será la cantidad de nodos
			Iterator<Vertex> iter2 = lista2.iterator();
			Vertex temp2;
			while(iter2.hasNext()){
				temp2 = iter2.next();
				fila.add(DijkstraShortestPath(temp1, temp2, (LinkedGraph) grafo));
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
