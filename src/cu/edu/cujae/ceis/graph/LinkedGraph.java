package cu.edu.cujae.ceis.graph;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import cu.edu.cujae.ceis.graph.edge.Edge;
import cu.edu.cujae.ceis.graph.edge.WeightedEdge;
import cu.edu.cujae.ceis.graph.interfaces.ILinkedDirectedGraph;
import cu.edu.cujae.ceis.graph.interfaces.ILinkedNotDirectedGraph;
import cu.edu.cujae.ceis.graph.interfaces.ILinkedWeightedEdgeDirectedGraph;
import cu.edu.cujae.ceis.graph.interfaces.ILinkedWeightedEdgeNotDirectedGraph;
import cu.edu.cujae.ceis.graph.interfaces.ILinkedWeightedVertexDirectedGraph;
import cu.edu.cujae.ceis.graph.interfaces.ILinkedWeightedVertexNotDirectedGraph;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import cu.edu.cujae.ceis.graph.vertex.WeightedVertex;

/**
 * <h1>Grafo enlazado</h1> 
 */
public class LinkedGraph extends Graph implements ILinkedDirectedGraph, ILinkedNotDirectedGraph,
							ILinkedWeightedEdgeDirectedGraph, ILinkedWeightedEdgeNotDirectedGraph,
							ILinkedWeightedVertexDirectedGraph, ILinkedWeightedVertexNotDirectedGraph, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Lista de v�rtices.
	 */
	private LinkedList<Vertex> verticesList;
	
	public LinkedGraph() {
		verticesList = new LinkedList<Vertex>();
	}
	
	/**
	 * Valida una posici�n en la lista 
	 * de v�rtices.
	 * @param pos posici�n.
	 * @return true si la posici�n es v�lida, false si no.
	 */
	private boolean posInRange(int pos) {
		return pos > -1 && pos < verticesList.size();
	}
	
	/**
	 * Elimina un v�rtice.
	 * @param vertex v�rtice.
	 */
	private void deleteVertex(Vertex vertex) {
		//se elimina el de la lista
		verticesList.remove(vertex);
		
		//y de la lista de adyacentes de todos los dem�s v�rtices
		Iterator<Vertex> iter = verticesList.iterator();
		while(iter.hasNext()) {
			iter.next().deleteEdge(vertex);
		}
	}
	
	/**
	 * Devuelve recursivamente un v�rtice junto con todos sus asociados
	 * directa e indirectamente en un grafo.
	 * @param vertex v�rtice.
	 * @param selected lista de v�rtices seleccionados.
	 */
	private void selectVerticesInCascadeRecurs(Vertex vertex, LinkedList<Vertex> selected) {
		if(!selected.contains(vertex)) {
			Vertex current;
			LinkedList<Edge> edges = vertex.getEdgeList();
			Iterator<Edge> iter = edges.iterator();					
			selected.add(vertex);
			
			while(iter.hasNext()) {
				current = iter.next().getVertex();			
				selectVerticesInCascadeRecurs(current, selected);
			}		
		}
	}
	
	/**
	 * Indica si existe al menos un ciclo comenzando por el v�rtice
	 * especificado en un grafo dirigido.
	 * 
	 * @param adjacents lista de adyacentes del v�rtice a verificar.
	 * @param vertex v�rtice a verificar.
	 * @param visited lista de v�rtices visitados.
	 * @return true si existe un ciclo con inicio en vertex.
	 */
	private boolean cycleInNodeDG(LinkedList<Edge> adjacents, Vertex vertex, LinkedList<Vertex> visited) {			
		Vertex current; 
		boolean cycle = false;
		Iterator<Edge> iter = adjacents.iterator();		
		
		while(!cycle && iter.hasNext()) {
			current = iter.next().getVertex();
			
			if(!current.equals(vertex)) {	
				if(!visited.contains(current)) {
					visited.add(current);
					cycle = cycleInNodeDG(current.getEdgeList(), vertex, visited);
				}
			} else
				cycle = true;
		}
		return cycle;
	}
	
	/**
	 * Indica si existe al menos un ciclo comenzando por el v�rtice
	 * especificado en un grafo no dirigido.
	 * 
	 * @param vertex vertex.
	 * @param visited visited.
	 * @return true si existe un ciclo con inicio en vertex o en uno
	 * 			de sus asociados directa o indirectamente.
	 */
	private boolean cycleInNodeND(Vertex origin, Vertex vertex, LinkedList<Vertex> visited) {
		boolean cycle = false;		
		
		if(!visited.contains(vertex)) {
			visited.add(vertex);
			Iterator<Edge> iter = vertex.getEdgeList().iterator();			
			
			while(!cycle && iter.hasNext()) {
				Vertex current = iter.next().getVertex();
				
				if(!current.equals(origin))
					cycle = cycleInNodeND(vertex, current, visited);
			}
		} else {
			cycle = true;
		}				
		return cycle;
	}
	
	/**
	 * Chequea recursivamente si existe un camino entre 
	 * dos v�rtices de longitud especificada.
	 * 
	 * @param v1 v�rtice 1.
	 * @param v2 v�rtice 2.
	 * @param length longitud del camino.
	 * @return true si el camino existe, false si no.
	 */
	private boolean path(Vertex v1, Vertex v2, int length) {
		boolean found = false;
		
		if(length == 1) {
			found = v1.isAdjacent(v2);
		} else {
			Iterator<Vertex> iter = verticesList.iterator();
			Vertex bridge;
			
			while(!found && iter.hasNext()) {
				bridge = iter.next();
				
				if(bridge.isAdjacent(v2))
					found = path(v1, bridge, length-1);
			}
		}
		
		return found;
	}
	
	/*-----------------------------------------*\
	|   Funciones comunes a todos los grafos    |
	\*-----------------------------------------*/
	
	/**
	 * Devuelve el �ndice de un v�rtice.
	 * @param vertex v�rtice
	 * @return �ndice del v�rtice en la lista de v�rtices
	 * 			o -1 si no existe.
	 */
	public int getVertexIndex(Vertex vertex) {
		int count = 0;
		int index = -1;
		Iterator<Vertex> iter = verticesList.iterator();
		
		while(index == -1 && iter.hasNext()) {
			if(iter.next().equals(vertex))
				index = count;
			count++;
		}
		
		return index;
	}
		
	/**
	 * Indica si el grafo est� vac�o.
	 * @return true si est� vac�o, false si no.
	 */
	@Override
	public boolean isEmpty() {
		return verticesList.isEmpty();
	}
	
	/**
	 * Indica si dos v�rtice son adyacentes.
	 * 
	 * @param posTail posici�n del v�rtice cola.
	 * @param posHead posici�n del v�rtice cabeza.
	 * @return true si son adyacentes, false si no.
	 */
	
	public boolean areAdjacents(int posTail, int posHead) {		
		boolean adjacents = false;
		
		if(posInRange(posHead) && posInRange(posTail)) 
			adjacents = verticesList.get(posTail).isAdjacent(verticesList.get(posHead));
		
		return adjacents;
	}			
	
	/**
	 * Inserta un nuevo v�rtice al grafo.
	 *
	 * @param info informaci�n del v�rtice.
	 * @return true si la operaci�n se complet� 
	 * 			satisfactoriamente, false si no.
	 */
	
	public boolean insertVertex(Object info) {		
		return verticesList.add(new Vertex(info));
	}

	/**
	 * Indica si existe un camino de 
	 * longitud determinada entre dos v�rtices.
	 * 
	 * @param posTail posici�n del v�rtice en la cola del camino.
	 * @param posHead posici�n del v�rtice en la cabeza del camino.
	 * @param length longitud del camino.
	 * @return true si el camino existe, false si no.
	 */
	
	public boolean pathWithLength(int posTail, int posHead, int length) {
		boolean found = false;
		
		if(posInRange(posTail) && posInRange(posHead))
			found = path(verticesList.get(posTail), verticesList.get(posHead), length);
		
		return found;
	}		
	
	/**
	 * Devuelve la lista de v�rtices.
	 * @return lista de v�rtices.
	 */
	
	public LinkedList<Vertex> getVerticesList() {
		return verticesList;
	}

	/**
	 * Devuelve la lista de adyacentes a
	 * un v�rtice.
	 * @param pos posici�n del v�rtice del que se quiere conocer los adyacentes.
	 * @return lista de v�rtices adyacentes.
	 */
	
	public LinkedList<Vertex> adjacentsG(int pos) {		
		LinkedList<Vertex> verts = new LinkedList<Vertex>();		
		
		if(posInRange(pos)) {
			LinkedList<Edge> edges = verticesList.get(pos).getEdgeList();
			Iterator<Edge> iter = edges.iterator();
			
			while(iter.hasNext())
				verts.add(iter.next().getVertex());
		}
		return verts;
	}
	
	/**
	 * Elimina un v�rtice del grafo.
	 * @param pos posici�n del v�rtice.
	 * @return retorna el v�rtice eliminado si la operaci�n es
	 * 		satisfactoria o null en caso contrario.
	 */
	
	public Vertex deleteVertex(int pos) {
		Vertex v = null;
		if(posInRange(pos)) {
			v = verticesList.get(pos);
			deleteVertex(v);			
		}
		return v;
	}
	
	/**
	 * Elimina en cascada un v�rtice junto con todos sus adyacentes. 
	 * Si existe al menos un camino desde el v�rtice hasta todos los 
	 * dem�s el grafo quedar� vac�o.
	 * 
	 * @return lista de v�rtices eliminados.
	 */
	
	public LinkedList<Vertex> deleteVertexCascade(int pos) {

		LinkedList<Vertex> deleted = new LinkedList<Vertex>();		
		
		if(posInRange(pos)) {
			selectVerticesInCascadeRecurs(verticesList.get(pos), deleted);			
			Iterator<Vertex> iter = deleted.iterator();
			
			while(iter.hasNext()) {				
				deleteVertex(getVertexIndex(iter.next()));
			}
		}
		
		return deleted;
	}	
	
	/*-----------------------------------------*\
	|      Funciones para grafos dirigidos      |
	\*-----------------------------------------*/
	
	/**
	 * Indica si existe al menos un ciclo en un grafo dirigido.
	 * @return true si existe al menos un cliclo, false si no.
	 */
	
	public boolean cyclicDG() {		
		Vertex current;		
		boolean cycle = false;
		Iterator<Vertex> iter = verticesList.iterator();		
		
		while(!cycle && iter.hasNext()) {
			current = iter.next();
			cycle = cycleInNodeDG(current.getEdgeList(), current, new LinkedList<Vertex>());
		}
		
		return cycle;
	}
	
	/**
	 * Indica el grado de un v�rtice
	 * en un grafo dirigido.
	 * 
	 * @param pos posici�n del v�rtice del que se
	 * 			desea saber el grado.
	 * @return el grado del v�rtice o -1 si ocurre
	 * 			un error.
	 */
	
	public int degreeDG(int pos) {
		int degree = -1;
		if(posInRange(pos)) {
			degree = inDegreeDG(pos) + outDegree(pos);
		}
		return degree;
	}
	
	/**
	 * Indica el grado de entrada de un v�rtice
	 * en un grafo dirigido.
	 * 
	 * @param pos posici�n del v�rtice del que se
	 * 			desea saber el grado de entrada.
	 * @return el grado del v�rtice o -1 si ocurre
	 * 			un error.
	 */
	
	public int inDegreeDG(int pos) {
		int degree = -1;
		
		if(posInRange(pos)) {
			degree = 0;
			Vertex vertex = verticesList.get(pos);
			Iterator<Vertex> iter = verticesList.iterator();
			
			while(iter.hasNext()) {
				if(iter.next().isAdjacent(vertex))
					degree++;
			}
		}
		return degree;
	}
	
	/**
	 * Indica el grado de salida de un v�rtice
	 * en un grafo dirigido.
	 * 
	 * @param pos posici�n del v�rtice del que se
	 * 			desea saber el grado de salida.
	 * @return el grado del v�rtice o -1 si ocurre	un error.
	 */
	
	public int outDegree(int pos) {
		int degree = -1;
		
		if(posInRange(pos))
			degree = verticesList.get(pos).getAdjacents().size();
		
		return degree;
	}	
	
	/**
	 * Elimina una arista entre dos v�rtices en
	 * un grafo dirigido.
	 * 
	 * @param posTail posici�n del v�rtice en la cola de la arista.
	 * @param posHead posici�n del v�rtice en la cabeza de la arista.
	 * @return true si la operaci�n se complet� satisfactoriamente, false si no.
	 */
	
	public boolean deleteEdgeD(int posTail, int posHead) {
		boolean success = false;
		
		if(posInRange(posTail) && posInRange(posHead)) {
			Vertex tail = verticesList.get(posTail);
			Vertex head = verticesList.get(posHead);			
			success = tail.deleteEdge(head);
		}
		return success;
	}
	
	/**
	 * Inserta una arista entre dos v�rtices en
	 * un grafo dirigido.
	 * 
	 * @param posTail posici�n del v�rtice en la cola de la arista.
	 * @param posHead posici�n del v�rtice en la cabeza de la arista.
	 * @return true si la operaci�n se complet� satisfactoriamente, false si no.
	 */
	
	public boolean insertEdgeDG(int posTail, int posHead) {
		boolean success = false;
		
		if(posInRange(posTail) && posInRange(posHead)) {
			Vertex tail = verticesList.get(posTail);
			Vertex head = verticesList.get(posHead);
			success = tail.getEdgeList().add(new Edge(head));
		}
		return success;
	}		

	/**
	 * <p>Elimina los v�rtices que no se encuentren asociados
	 * en un grafo dirigido.</p>
	 * <bold>Nota:</bold> Un v�rtice con un lazo a si mismo no
	 * se considera desconectado.
	 * @return lista de v�rtices eliminados.
	 */
	
	public LinkedList<Vertex> removeDisconnectVerticesDG() {
		LinkedList<Vertex> verts = new LinkedList<Vertex>();					
		
		for(int i=0; i<verticesList.size();) {
			if(degreeDG(i) == 0) {
				verts.add(verticesList.get(i));
				verticesList.remove(i);
			} else
				i++;
		}
		return verts;
	}
	
	/*-----------------------------------------*\
	|    Funciones para grafos no dirigidos     |
	\*-----------------------------------------*/
	
	/**
	 * Indica si existe al menos un ciclo en un grafo no dirigido.
	 * @return true si existe al menos un cliclo, false si no.
	 */
	
	public boolean cyclicND() {
		Vertex current;		
		boolean cycle = false;
		Iterator<Vertex> iter = verticesList.iterator();		
		
		while(!cycle && iter.hasNext()) {
			current = iter.next();
			cycle = cycleInNodeND(null, current, new LinkedList<Vertex>());
		}
		
		return cycle;
	}
	
	/**
	 * Devuelve el grado de un v�rtice en un grafo no dirigido.
	 * @param pos �ndice del v�rtice del que se quiere 
	 * 				conocer el grado.
	 */
	
	public int degreeND(int pos) {
		int degree = -1;
		
		if(posInRange(pos)) {
			degree = verticesList.get(pos).getAdjacents().size();
		}
		
		return degree;
	}

	/**
	 * Elimina una arista entre dos v�rtices en
	 * un grafo no dirigido.
	 * 
	 * @param posTail posici�n del v�rtice en el extremo 1.
	 * @param posHead posici�n del v�rtice en el extremo 2.
	 * @return true si la operaci�n se complet� satisfactoriamente, false si no.
	 */
	
	public boolean deleteEdgeND(int posTail, int posHead) {
		boolean success = false;
		
		if(posInRange(posTail) && posInRange(posHead)) {
			success  = deleteEdgeD(posTail, posHead);
			success &= deleteEdgeD(posHead, posTail);
		}
		return success;
	}

	/**
	 * Inserta una arista entre dos v�rtices en
	 * un grafo no dirigido.
	 * 
	 * @param posTail posici�n del v�rtice en el extremo 1.
	 * @param posHead posici�n del v�rtice en el extremo 2.
	 * @return true si la operaci�n se complet� satisfactoriamente, false si no.
	 */
	
	public boolean insertEdgeNDG(int posTail, int posHead) {
		boolean success = false;
		
		if(posInRange(posTail) && posInRange(posHead)) {
			success  = insertEdgeDG(posTail, posHead);
			
			//si es un lazo al mismo v�rtice se hace un solo enlace
			if(posTail != posHead)
				success &= insertEdgeDG(posHead, posTail);
		}
		return success;
	}

	/* 
	   ------------------------
	< I�m not a bug, I�m a cow  >
	  ------------------------
	       \ ,__,
	        \(oo)____
	         (__)    )\
	            ||--|| *
	 */
	
	/**
	 * <p>Elimina los v�rtices que no se encuentren asociados
	 * en un grafo no dirigido.</p>
	 * <bold>Nota:</bold> Un v�rtice con un lazo a si mismo no
	 * se considera desconectado.
	 * @return lista de v�rtices eliminados.
	 */
	
	public LinkedList<Vertex> removeDisconnectVerticesND() {
		LinkedList<Vertex> verts = new LinkedList<Vertex>();					
		
		for(int i=0; i<verticesList.size();) {
			if(degreeND(i) == 0) {
				verts.add(verticesList.get(i));
				verticesList.remove(i);
			} else
				i++;
		}
		return verts;
	}
	
	/*-------------------------------------------------------------*\
	|    Funciones para grafos dirigidos con aristas ponderadas     |
	\*-------------------------------------------------------------*/
	
	/**
	 * Inserta una arista ponderada entre dos v�rtices en
	 * un grafo dirigido.
	 * 
	 * @param posTail posici�n del v�rtice en el extremo 1.
	 * @param posHead posici�n del v�rtice en el extremo 2.
	 * @param weight peso.
	 * @return true si la operaci�n se complet� satisfactoriamente, false si no.
	 */
	
	public boolean insertWEdgeDG(int posTail, int posHead, Object weight) {
		boolean success = false;
		
		if(posInRange(posTail) && posInRange(posHead)) {
			Vertex tail = verticesList.get(posTail);
			Vertex head = verticesList.get(posHead);			
			success = tail.getEdgeList().add(new WeightedEdge(head, weight));
		}
		return success;
	}
		
	/*----------------------------------------------------------------*\
	|    Funciones para grafos no dirigidos con aristas ponderadas     |
	\*----------------------------------------------------------------*/
	
	/**
	 * Inserta una arista ponderada entre dos v�rtices en
	 * un grafo no dirigido.
	 * 
	 * @param posTail posici�n del v�rtice en el extremo 1.
	 * @param posHead posici�n del v�rtice en el extremo 2.
	 * @param weight peso.
	 * @return true si la operaci�n se complet� satisfactoriamente, false si no.
	 */
	
	public boolean insertWEdgeNDG(int posTail, int posHead, Object weight) {
		boolean success = false;
		
		if(posInRange(posTail) && posInRange(posHead)) {
			success  = insertWEdgeDG(posTail, posHead, weight);
			
			//si es un lazo al mismo v�rtice se hace un solo enlace
			if(posTail != posHead)
				success &= insertWEdgeDG(posHead, posTail, weight);
		}
		return success;
	}
		
	/*---------------------------------------------------*\
	|   Funciones para grafos con v�rtices ponderados     |
	\*---------------------------------------------------*/
	
	/**
	 * Inserta un v�rtice ponderado al grafo.
	 *
	 * @param info informaci�n del v�rtice.
	 * @param weight peso.
	 * @return true si la operaci�n se complet� 
	 * 			satisfactoriamente, false si no.
	 */
	
	public boolean insertWVertex(Object info, Object weight) {
		return verticesList.add(new WeightedVertex(info, weight));
	}	
}


