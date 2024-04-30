package cu.edu.cujae.ceis.graph.interfaces;

/**
 * <h1>Interface para grafos dirigidos con aristas ponderadas</h1>
 */
public interface ILinkedWeightedEdgeDirectedGraph extends ILinkedDirectedGraph {
	public boolean insertWEdgeDG(int posTail, int posHead, Object weight);
}
