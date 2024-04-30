package cu.edu.cujae.ceis.graph.edge;

import cu.edu.cujae.ceis.graph.vertex.Vertex;

/**
 *  <h1>Arista con peso</h1>
 */
public class WeightedEdge extends Edge {
	
	/**
	 * Objeto que representa el peso de la arista.
	 */
	private Object weight;

	/**
	 * Inicia la instancia con el v�rtice 
	 * asociado y el peso de la arista.
	 * 
	 * @param vertex v�rtice asociado.
	 * @param weight peso de la arista.
	 */
	public WeightedEdge(Vertex vertex, Object weight) {
		super(vertex);
		this.weight = weight;
	}

	/**
	 * Devuelve el peso de la arista.
	 * 
	 * @return peso.
	 */
	public Object getWeight() {
		return weight;
	}

	public void setWeight(Object weight) {
		this.weight = weight;
	}
	
	

}
