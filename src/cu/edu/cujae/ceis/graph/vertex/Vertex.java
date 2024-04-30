package cu.edu.cujae.ceis.graph.vertex;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import cu.edu.cujae.ceis.graph.edge.Edge;


/**
 * <h1>V�rtice elemental</h1>
 */
public class Vertex implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Objeto que representa la informaci�n del v�rtice.
	 */
	private Object info;
	
	/**
	 * Lista de aristas.
	 */
	private LinkedList<Edge> edgeList;	

	/**
	 * Inicia la instancia con la
	 * informaci�n del v�rtice.
	 * 
	 * @param info Informaci�n.
	 */
	public Vertex(Object info) {
		this.info = info;
		edgeList  = new LinkedList<Edge>();
	}
	
	/**
	 * Elimina la arista hacia un v�rtice dado.
	 * 
	 * @param vertex V�rtice.
	 * @return True si se realiza la operaci�n. False si no 
	 * 			se encuentra una arista hacia el v�rtice
	 * 			dado.
	 */
	public boolean deleteEdge(Vertex vertex) {
		boolean success = false;
		Iterator<Edge> iter = edgeList.iterator();
        
		while(!success && iter.hasNext()) {
			if(iter.next().getVertex().equals(vertex)) {
				iter.remove();
				success = true;
			}
		}
		return success;		
	}
	
	/**
	 * Permite acceder a la lista de aristas.
	 * @return Lista de aristas.
	 */
	public LinkedList<Edge> getEdgeList() {
		return edgeList;
	}
	
	/**
	 * Obtiene la informaci�n del v�rtice.
	 * @return Informaci�n.
	 */
	public Object getInfo() {
		return info;
	}
	
	/**
	 * Define la informaci�n para este nodo.
	 * @param info Informaci�n.
	 */
	public void setInfo(Object info) {
		this.info = info;
	}
	
	/**
	 * Permite acceder a la lista de aristas.
	 * @return Lista de aristas.
	 */
	public LinkedList<Vertex> getAdjacents() {
		LinkedList<Vertex> vertices = new LinkedList<Vertex>();		
		Iterator<Edge> iter = edgeList.iterator();
		
		while(iter.hasNext()) {
			vertices.add(iter.next().getVertex());
		}
		
		return vertices;
	}	
	
	/**
	 * Permite saber si este v�rtice es adyacente
	 * con un v�rtice dado.
	 * 
	 * @param vertex V�rtice.
	 * @return True si son adyacente, false si no.
	 */
	public boolean isAdjacent(Vertex vertex) {
		boolean adjacent = false;
		Iterator<Edge> iter = edgeList.iterator();
		
		while(!adjacent && iter.hasNext()) {
			if(iter.next().getVertex().equals(vertex)) {
				adjacent = true;
			}
		}		
		return adjacent;
	}
	
	/**
	 * Esta funci�n ser� �til para la depuraci�n.
	 * @return Informaci�n.
	 */
	@Override
	public String toString() {
		return info.toString();
	}
}
