package cu.edu.cujae.ceis.graph.interfaces;

/**
 * <h1>Interface para grafos dirigidos con vértices ponderados</h1>
 */
public interface ILinkedWeightedVertexDirectedGraph extends
		ILinkedDirectedGraph {
	public boolean insertWVertex(Object info, Object weight);
}
