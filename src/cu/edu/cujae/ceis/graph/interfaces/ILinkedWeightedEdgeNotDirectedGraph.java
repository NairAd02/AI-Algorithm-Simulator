package cu.edu.cujae.ceis.graph.interfaces;

/**
 * <h1>Interface para grafos no dirigidos con aristas ponderadas</h1>
 */
public interface ILinkedWeightedEdgeNotDirectedGraph extends
		ILinkedNotDirectedGraph {
	public boolean insertWEdgeNDG(int posTail, int posHead, Object weight);
}
