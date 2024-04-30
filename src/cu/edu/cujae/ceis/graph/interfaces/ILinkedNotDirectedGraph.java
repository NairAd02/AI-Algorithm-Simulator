package cu.edu.cujae.ceis.graph.interfaces;

import java.util.LinkedList;

import cu.edu.cujae.ceis.graph.vertex.Vertex;

/**
 * <h1>Interface para grafos no dirigidos</h1>
 */
public interface ILinkedNotDirectedGraph {
	
	public boolean areAdjacents(int posTail, int posHead);		
	public boolean insertVertex(Object info);	
	public boolean isEmpty();
	public boolean pathWithLength (int posTail, int posHead, int length);
	public LinkedList<Vertex> getVerticesList();	
	public LinkedList<Vertex> adjacentsG(int pos);
	public LinkedList<Vertex> deleteVertexCascade(int pos);
	public Vertex deleteVertex(int pos);
	
	public int degreeND(int pos);
	public boolean cyclicND();
	public boolean deleteEdgeND(int posTail, int posHead);		
	public boolean insertEdgeNDG(int posTail, int posHead);	
	public LinkedList<Vertex> removeDisconnectVerticesND();
			
}
