package cu.edu.cujae.ceis.graph.interfaces;

import java.util.LinkedList;

import cu.edu.cujae.ceis.graph.vertex.Vertex;

/**
 * <h1>Interface para grafos dirigidos</h1>
 */
public interface ILinkedDirectedGraph {
		
	public boolean areAdjacents(int posTail, int posHead);		
	public boolean insertVertex(Object info);	
	public boolean isEmpty();	
	public boolean pathWithLength (int posTail, int posHead, int length);	
	public LinkedList<Vertex> getVerticesList();
	public LinkedList<Vertex> adjacentsG(int pos);
	public LinkedList<Vertex> deleteVertexCascade(int pos);
	public Vertex deleteVertex(int pos);
		
	public int degreeDG(int pos);
	public int inDegreeDG(int pos);
	public int outDegree(int pos);
	public boolean cyclicDG();
	public boolean deleteEdgeD(int posTail, int posHead);	
	public boolean insertEdgeDG(int posTail, int posHead);		
	public LinkedList<Vertex> removeDisconnectVerticesDG();			
}
