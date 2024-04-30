package cu.edu.cujae.ceis.graph.edge;

import java.io.Serializable;

import cu.edu.cujae.ceis.graph.vertex.Vertex;

/**
 * <h1>Arista elemental</h1>
 */
public class Edge implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * V�rtice asociado a la arista.
	 */
	protected Vertex vertex;

	/**
	 * Inicia la instancia con el v�rtice
	 * asociado a la arista.
	 * 
	 * @param vertex v�rtice.
	 */
	public Edge(Vertex vertex) {
		this.vertex = vertex;
	}
	
	/**
	 * Devuelve el v�rtice asociado
	 * a esta arista.
	 * 
	 * @return v�rtice.
	 */
	public Vertex getVertex() {
		return vertex;
	}
	
	/**
	 * Indica el v�rtice asociado
	 * con esta arista.
	 * 
	 * @param vertex v�rtice.
	 */
	public void setVertex(Vertex vertex) {
		this.vertex = vertex;
	}
}
