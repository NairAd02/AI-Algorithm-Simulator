package utils;

import cu.edu.cujae.ceis.graph.vertex.Vertex;

public class ParVertex {
	private Vertex vertexInicial;
	private Vertex vertexFinal;
	
	public ParVertex(Vertex vertexInicial, Vertex vertexFinal) {
		super();
		this.vertexInicial = vertexInicial;
		this.vertexFinal = vertexFinal;
	}
	public Vertex getVertexInicial() {
		return vertexInicial;
	}
	public void setVertexInicial(Vertex vertexInicial) {
		this.vertexInicial = vertexInicial;
	}
	public Vertex getVertexFinal() {
		return vertexFinal;
	}
	public void setVertexFinal(Vertex vertexFinal) {
		this.vertexFinal = vertexFinal;
	}
	
}
