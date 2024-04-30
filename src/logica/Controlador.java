package logica;
import java.awt.geom.Line2D;
import java.util.*;



import cu.edu.cujae.ceis.graph.LinkedGraph;
import cu.edu.cujae.ceis.graph.edge.Edge;
import cu.edu.cujae.ceis.graph.edge.WeightedEdge;
import cu.edu.cujae.ceis.graph.interfaces.ILinkedWeightedEdgeDirectedGraph;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import utils.ParVertex;

public class Controlador {
	private ILinkedWeightedEdgeDirectedGraph grafoKNN;
	private int k;
	private static Controlador contralador;
	//private ArrayList<ArrayList<Double>> distancias;

	private Controlador(int k){
		grafoKNN = new LinkedGraph();
		this.k = k;

		//init();
	}

	public ILinkedWeightedEdgeDirectedGraph getGrafoKNN() {
		return grafoKNN;
	}

	public void setGrafoKNN(ILinkedWeightedEdgeDirectedGraph grafoKNN) {
		this.grafoKNN = grafoKNN;
	}

	public static Controlador getInstancie (int k) {
		if (contralador == null)
			contralador = new Controlador(k);

		return contralador;
	}

	public static Controlador getInstancie () {
		return contralador;
	}


	// Operaciones
	// Metodo para insertar un elemento en el grafo
	public void addElemento (String id, Object info, float x, float y, double largo, double ancho) {
		this.grafoKNN.insertVertex(new Elemento(id, info, x, y, largo, ancho));
		// cada vez que se inserta un vertice es necesario trazar aristas de distancias
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

	public void addElemento (String id, Object info, float x, float y) throws Exception {
		if (this.buscarVerticeByElementoId(id) == null) // si no existe un vértice con un elemento repetido
			this.grafoKNN.insertVertex(new Elemento(id, info, x, y)); 
		else
			throw new Exception("No se permiten vertices con elementos duplicados");
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
		if (posVertex != -1) // si fue encontrado el vertice a eliminar
			this.grafoKNN.deleteVertex(posVertex); // se elimina el vertice del grafo
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

	public void algoritmo(Elemento e1){
		Iterator<Vertex> iterGrafo = grafoKNN.getVerticesList().iterator();
		LinkedList<WeightedEdge> menoresDistancias = new LinkedList<WeightedEdge>();
		Vertex vertice = null;
		float distancia = 0;
		float x1 = e1.getX();
		float y1 = e1.getY();
		float x2 = 0;
		float y2 = 0;
		Elemento e2 = null;
		WeightedEdge mayorDistancia = null;

		while(iterGrafo.hasNext()){
			vertice = iterGrafo.next();
			e2 = (Elemento)vertice.getInfo();
			x2 = e2.getX();
			y2 = e2.getY();

			distancia = distanciaEuclidiana(x1, y1, x2, y2);

			WeightedEdge WeightedEdge = new WeightedEdge(vertice, distancia);

			if(menoresDistancias.isEmpty()){
				menoresDistancias.addFirst(WeightedEdge);
				mayorDistancia = WeightedEdge;
			}
			else if(menoresDistancias.size() < k){
				menoresDistancias.addFirst(WeightedEdge);
				if((Float)WeightedEdge.getWeight() > (Float)mayorDistancia.getWeight())
					mayorDistancia = WeightedEdge;
			}	
			else if((Float)WeightedEdge.getWeight() < (Float)mayorDistancia.getWeight()){
				menoresDistancias.remove(mayorDistancia);

				mayorDistancia = WeightedEdge;

				for(WeightedEdge a: menoresDistancias){
					if((Float)a.getWeight() > (Float)mayorDistancia.getWeight())
						mayorDistancia = a;
				}

				menoresDistancias.add(WeightedEdge);		
			}
		}

		Vertex nuevo = new Vertex(e1);

		for(WeightedEdge a: menoresDistancias){
			nuevo.getEdgeList().add(a);
		}

		grafoKNN.getVerticesList().add(nuevo);

		Iterator<Vertex> iterAdjacentes = nuevo.getAdjacents().iterator();
		Vertex adjacente = null;
		ArrayList<String> clases = new ArrayList<String>();
		ArrayList<Integer> votos = new ArrayList<Integer>();

		while(iterAdjacentes.hasNext()){
			adjacente = iterAdjacentes.next();
			Elemento elemento = (Elemento)adjacente.getInfo();
			String clase = elemento.getClase();

			if(!clases.contains(clase)){
				clases.add(clase);
				votos.add(0);
			}	

			votos.set(clases.indexOf(clase), votos.get(clases.indexOf(clase)) + 1);	
		}

		int mayor = 0;
		Iterator<Integer> iterVotos = votos.iterator();

		while(iterVotos.hasNext()){
			int temporal = iterVotos.next();
			if(temporal > mayor)
				mayor = temporal; 
		}

		e1.setClase(clases.get(votos.indexOf(mayor)));

		//PRUEBA

		System.out.println();
		System.out.print(e1.getClase());
		System.out.println();

		//FIN PRUEBA

		Iterator<Vertex> iterAdjacentes2 = nuevo.getAdjacents().iterator();
		Iterator<Edge> iterAristasNuevo = nuevo.getEdgeList().iterator();
		Vertex adjacente2 = null;
		WeightedEdge aristaAlVerticeMasLejano = null;
		WeightedEdge aristaAlVerticeAAnalizar = null;

		while(iterAdjacentes2.hasNext()){
			adjacente2 = iterAdjacentes2.next();
			Iterator<Edge> iterAristas = adjacente2.getEdgeList().iterator();

			aristaAlVerticeMasLejano = (WeightedEdge)iterAristas.next();
			float distanciaDeAristaAlVerticeMasLejano = 
					(Float)aristaAlVerticeMasLejano.getWeight();

			while(iterAristas.hasNext()){
				aristaAlVerticeAAnalizar = (WeightedEdge)iterAristas.next();
				float distanciaDeAristaAlVerticeAAnalizar = 
						(Float)aristaAlVerticeAAnalizar.getWeight();
				if(distanciaDeAristaAlVerticeAAnalizar > distanciaDeAristaAlVerticeMasLejano){
					aristaAlVerticeMasLejano = aristaAlVerticeAAnalizar;
					distanciaDeAristaAlVerticeMasLejano = distanciaDeAristaAlVerticeAAnalizar;
				}
			}

			WeightedEdge arista = (WeightedEdge)iterAristasNuevo.next();
			float distanciaArista = (Float)arista.getWeight();

			if(distanciaArista < distanciaDeAristaAlVerticeMasLejano){
				adjacente2.getEdgeList().remove(aristaAlVerticeMasLejano);

				WeightedEdge aristaAAgregar = new WeightedEdge(nuevo, distanciaArista);

				adjacente2.getEdgeList().add(aristaAAgregar);
			}	
		}
	}

	public void init(){
		Elemento elementoA = new Elemento("IdA", "InfoA", 0.5f, 6, 100, 100);
		Elemento elementoB = new Elemento("IdB", "InfoB", 1.5f, 5.2f, 100, 100);
		Elemento elementoC = new Elemento("IdC", "InfoC", 0.6f, 4, 100, 100);
		Elemento elementoD = new Elemento("IdD", "InfoD", 2.5f, 4.7f, 100, 100);
		Elemento elementoE = new Elemento("IdE", "InfoE", 2.2f, 3.5f, 100, 100);
		Elemento elementoF = new Elemento("IdF", "InfoF", 6, 4.5f, 100, 100);
		Elemento elementoG = new Elemento("IdG", "InfoG", 7.2f, 5.1f, 100, 100);
		Elemento elementoH = new Elemento("IdH", "InfoH", 7.5f, 4.2f, 100, 100);
		Elemento elementoI = new Elemento("IdI", "InfoI", 6.3f, 1.9f, 100, 100);
		Elemento elementoJ = new Elemento("IdJ", "InfoJ", 4.3f, 1.8f, 100, 100);
		Elemento elementoK = new Elemento("IdK", "InfoK", 5.3f, 0.7f, 100, 100);

		elementoA.setClase("Circulo");
		elementoB.setClase("Circulo");
		elementoC.setClase("Circulo");
		elementoD.setClase("Circulo");
		elementoE.setClase("Circulo");
		elementoF.setClase("Rombo");
		elementoG.setClase("Rombo");
		elementoH.setClase("Rombo");
		elementoI.setClase("Cuadrado");
		elementoJ.setClase("Cuadrado");
		elementoK.setClase("Cuadrado");

		Vertex vertexA = new Vertex(elementoA);
		Vertex vertexB = new Vertex(elementoB);
		Vertex vertexC = new Vertex(elementoC);
		Vertex vertexD = new Vertex(elementoD);
		Vertex vertexE = new Vertex(elementoE);
		Vertex vertexF = new Vertex(elementoF);
		Vertex vertexG = new Vertex(elementoG);
		Vertex vertexH = new Vertex(elementoH);
		Vertex vertexI = new Vertex(elementoI);
		Vertex vertexJ = new Vertex(elementoJ);
		Vertex vertexK = new Vertex(elementoK);

		WeightedEdge edgeAB = new WeightedEdge(vertexB, distanciaEuclidiana(elementoA.getX(), 
				elementoA.getY(), elementoB.getX(), elementoB.getY()));
		System.out.println("edgeAB: "+ edgeAB.getWeight().toString());
		vertexA.getEdgeList().add(edgeAB);
		WeightedEdge edgeAC = new WeightedEdge(vertexC, distanciaEuclidiana(elementoA.getX(), 
				elementoA.getY(), elementoC.getX(), elementoC.getY()));
		System.out.println("edgeAC: "+ edgeAC.getWeight().toString());
		vertexA.getEdgeList().add(edgeAC);
		WeightedEdge edgeAD = new WeightedEdge(vertexD, distanciaEuclidiana(elementoA.getX(), 
				elementoA.getY(), elementoD.getX(), elementoD.getY()));
		System.out.println("edgeAD: "+ edgeAD.getWeight().toString());
		vertexA.getEdgeList().add(edgeAD);

		WeightedEdge edgeBA = new WeightedEdge(vertexA, distanciaEuclidiana(elementoB.getX(), 
				elementoB.getY(), elementoA.getX(), elementoA.getY()));
		System.out.println("edgeBA: "+ edgeBA.getWeight().toString());
		vertexB.getEdgeList().add(edgeBA);
		WeightedEdge edgeBC = new WeightedEdge(vertexC, distanciaEuclidiana(elementoB.getX(), 
				elementoB.getY(), elementoC.getX(), elementoC.getY()));
		System.out.println("edgeBC: "+ edgeBC.getWeight().toString());
		vertexB.getEdgeList().add(edgeBC);
		WeightedEdge edgeBD = new WeightedEdge(vertexD, distanciaEuclidiana(elementoB.getX(), 
				elementoB.getY(), elementoD.getX(), elementoD.getY()));
		System.out.println("edgeBD: "+ edgeBD.getWeight().toString());
		vertexB.getEdgeList().add(edgeBD);

		WeightedEdge edgeCA = new WeightedEdge(vertexA, distanciaEuclidiana(elementoC.getX(), 
				elementoC.getY(), elementoA.getX(), elementoA.getY()));
		System.out.println("edgeCA: "+ edgeCA.getWeight().toString());
		vertexC.getEdgeList().add(edgeCA);
		WeightedEdge edgeCB = new WeightedEdge(vertexB, distanciaEuclidiana(elementoC.getX(), 
				elementoC.getY(), elementoB.getX(), elementoB.getY()));
		System.out.println("edgeCB: "+ edgeCB.getWeight().toString());
		vertexC.getEdgeList().add(edgeCB);
		WeightedEdge edgeCE = new WeightedEdge(vertexE, distanciaEuclidiana(elementoC.getX(), 
				elementoC.getY(), elementoE.getX(), elementoE.getY()));
		System.out.println("edgeCE: "+ edgeCE.getWeight().toString());
		vertexC.getEdgeList().add(edgeCE);

		WeightedEdge edgeDA = new WeightedEdge(vertexA, distanciaEuclidiana(elementoD.getX(), 
				elementoD.getY(), elementoA.getX(), elementoA.getY()));
		System.out.println("edgeDA: "+ edgeDA.getWeight().toString());
		vertexD.getEdgeList().add(edgeDA);
		WeightedEdge edgeDB = new WeightedEdge(vertexB, distanciaEuclidiana(elementoD.getX(), 
				elementoD.getY(), elementoB.getX(), elementoB.getY()));
		System.out.println("edgeDB: "+ edgeDB.getWeight().toString());
		vertexD.getEdgeList().add(edgeDB);
		WeightedEdge edgeDE = new WeightedEdge(vertexE, distanciaEuclidiana(elementoD.getX(), 
				elementoD.getY(), elementoE.getX(), elementoE.getY()));
		System.out.println("edgeDE: "+ edgeDE.getWeight().toString());
		vertexD.getEdgeList().add(edgeDE);

		WeightedEdge edgeEB = new WeightedEdge(vertexB, distanciaEuclidiana(elementoE.getX(), 
				elementoE.getY(), elementoB.getX(), elementoB.getY()));
		System.out.println("edgeEB: "+ edgeEB.getWeight().toString());
		vertexE.getEdgeList().add(edgeEB);
		WeightedEdge edgeEC = new WeightedEdge(vertexC, distanciaEuclidiana(elementoE.getX(), 
				elementoE.getY(), elementoC.getX(), elementoC.getY()));
		System.out.println("edgeEC: "+ edgeEC.getWeight().toString());
		vertexE.getEdgeList().add(edgeEC);
		WeightedEdge edgeED = new WeightedEdge(vertexD, distanciaEuclidiana(elementoE.getX(), 
				elementoE.getY(), elementoD.getX(), elementoD.getY()));
		System.out.println("edgeED: "+ edgeED.getWeight().toString());
		vertexE.getEdgeList().add(edgeED);

		WeightedEdge edgeFG = new WeightedEdge(vertexG, distanciaEuclidiana(elementoF.getX(), 
				elementoF.getY(), elementoG.getX(), elementoG.getY()));
		System.out.println("edgeFG: "+ edgeFG.getWeight().toString());
		vertexF.getEdgeList().add(edgeFG);
		WeightedEdge edgeFH = new WeightedEdge(vertexH, distanciaEuclidiana(elementoF.getX(), 
				elementoF.getY(), elementoH.getX(), elementoH.getY()));
		System.out.println("edgeFH: "+ edgeFH.getWeight().toString());
		vertexF.getEdgeList().add(edgeFH);
		WeightedEdge edgeFI = new WeightedEdge(vertexI, distanciaEuclidiana(elementoF.getX(), 
				elementoF.getY(), elementoI.getX(), elementoI.getY()));
		System.out.println("edgeFI: "+ edgeFI.getWeight().toString());
		vertexF.getEdgeList().add(edgeFI);

		WeightedEdge edgeGF = new WeightedEdge(vertexF, distanciaEuclidiana(elementoG.getX(), 
				elementoG.getY(), elementoF.getX(), elementoF.getY()));
		System.out.println("edgeGF: "+ edgeGF.getWeight().toString());
		vertexG.getEdgeList().add(edgeGF);
		WeightedEdge edgeGH = new WeightedEdge(vertexH, distanciaEuclidiana(elementoG.getX(), 
				elementoG.getY(), elementoH.getX(), elementoH.getY()));
		System.out.println("edgeGH: "+ edgeGH.getWeight().toString());
		vertexG.getEdgeList().add(edgeGH);
		WeightedEdge edgeGI = new WeightedEdge(vertexI, distanciaEuclidiana(elementoG.getX(), 
				elementoG.getY(), elementoI.getX(), elementoI.getY()));
		System.out.println("edgeGI: "+ edgeGI.getWeight().toString());
		vertexG.getEdgeList().add(edgeGI);

		WeightedEdge edgeHF = new WeightedEdge(vertexF, distanciaEuclidiana(elementoH.getX(), 
				elementoH.getY(), elementoF.getX(), elementoF.getY()));
		System.out.println("edgeHF: "+ edgeHF.getWeight().toString());
		vertexH.getEdgeList().add(edgeHF);
		WeightedEdge edgeHG = new WeightedEdge(vertexG, distanciaEuclidiana(elementoH.getX(), 
				elementoH.getY(), elementoG.getX(), elementoG.getY()));
		System.out.println("edgeHG: "+ edgeHG.getWeight().toString());
		vertexH.getEdgeList().add(edgeHG);
		WeightedEdge edgeHI = new WeightedEdge(vertexI, distanciaEuclidiana(elementoH.getX(), 
				elementoH.getY(), elementoI.getX(), elementoI.getY()));
		System.out.println("edgeHI: "+ edgeHI.getWeight().toString());
		vertexH.getEdgeList().add(edgeHI);

		WeightedEdge edgeIH = new WeightedEdge(vertexH, distanciaEuclidiana(elementoI.getX(), 
				elementoI.getY(), elementoH.getX(), elementoH.getY()));
		System.out.println("edgeIH: "+ edgeIH.getWeight().toString());
		vertexI.getEdgeList().add(edgeIH);
		WeightedEdge edgeIJ = new WeightedEdge(vertexJ, distanciaEuclidiana(elementoI.getX(), 
				elementoI.getY(), elementoJ.getX(), elementoJ.getY()));
		System.out.println("edgeIJ: "+ edgeIJ.getWeight().toString());
		vertexI.getEdgeList().add(edgeIJ);
		WeightedEdge edgeIK = new WeightedEdge(vertexK, distanciaEuclidiana(elementoI.getX(), 
				elementoI.getY(), elementoK.getX(), elementoK.getY()));
		System.out.println("edgeIK: "+ edgeIK.getWeight().toString());
		vertexI.getEdgeList().add(edgeIK);

		WeightedEdge edgeJE = new WeightedEdge(vertexE, distanciaEuclidiana(elementoJ.getX(), 
				elementoJ.getY(), elementoE.getX(), elementoE.getY()));
		System.out.println("edgeJE: "+ edgeJE.getWeight().toString());
		vertexJ.getEdgeList().add(edgeJE);
		WeightedEdge edgeJI = new WeightedEdge(vertexI, distanciaEuclidiana(elementoJ.getX(), 
				elementoJ.getY(), elementoI.getX(), elementoI.getY()));
		System.out.println("edgeJI: "+ edgeJI.getWeight().toString());
		vertexJ.getEdgeList().add(edgeJI);
		WeightedEdge edgeJK = new WeightedEdge(vertexK, distanciaEuclidiana(elementoJ.getX(), 
				elementoJ.getY(), elementoK.getX(), elementoK.getY()));
		System.out.println("edgeJK: "+ edgeJK.getWeight().toString());
		vertexJ.getEdgeList().add(edgeJK);

		WeightedEdge edgeKF = new WeightedEdge(vertexF, distanciaEuclidiana(elementoK.getX(), 
				elementoK.getY(), elementoF.getX(), elementoF.getY()));
		System.out.println("edgeKF: "+ edgeKF.getWeight().toString());
		vertexK.getEdgeList().add(edgeKF);
		WeightedEdge edgeKI = new WeightedEdge(vertexI, distanciaEuclidiana(elementoK.getX(), 
				elementoK.getY(), elementoI.getX(), elementoI.getY()));
		System.out.println("edgeKI: "+ edgeKI.getWeight().toString());
		vertexK.getEdgeList().add(edgeKI);
		WeightedEdge edgeKJ = new WeightedEdge(vertexJ, distanciaEuclidiana(elementoK.getX(), 
				elementoK.getY(), elementoJ.getX(), elementoJ.getY()));
		System.out.println("edgeKJ: "+ edgeKJ.getWeight().toString());
		vertexK.getEdgeList().add(edgeKJ);

		grafoKNN.getVerticesList().add(vertexA);
		grafoKNN.getVerticesList().add(vertexB);
		grafoKNN.getVerticesList().add(vertexC);
		grafoKNN.getVerticesList().add(vertexD);
		grafoKNN.getVerticesList().add(vertexE);
		grafoKNN.getVerticesList().add(vertexF);
		grafoKNN.getVerticesList().add(vertexG);
		grafoKNN.getVerticesList().add(vertexH);
		grafoKNN.getVerticesList().add(vertexI);
		grafoKNN.getVerticesList().add(vertexJ);
		grafoKNN.getVerticesList().add(vertexK);

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


}
