package interfaz;

import java.awt.Color;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.LinkedList;

import javax.swing.JPanel;


import cu.edu.cujae.ceis.graph.edge.Edge;
import cu.edu.cujae.ceis.graph.edge.WeightedEdge;
import cu.edu.cujae.ceis.graph.vertex.Vertex;

import logica.Controlador;
import logica.Elemento;
import utils.Par;
import utils.ParVertex;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.SystemColor;
import java.awt.event.MouseMotionAdapter;

public class GraphPanel extends JPanel {
	private int mouseX, mouseY;


	public GraphPanel () {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (FramePrincipal.getInstancie().isInserctionVertex()) // si está habilitada la insercción de vértice
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				mouseX = e.getX();
				mouseY = e.getY();
				// se repinta el lienzo
				repintarse();
			}
		});
		setBackground(SystemColor.inactiveCaption);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (FramePrincipal.getInstancie().isInserctionVertex()) { // si está habilitada la insercción de vertice
					// se inserta el elemento como vertice del grafo
					try {
						Controlador.getInstancie().addElemento(FramePrincipal.getInstancie().getnombreCiudad(), "Info", e.getX(), e.getY());
						actualizarInfoLienzo();
					} catch (Exception e1) {
						FrameAdvertencia frameAdvertencia = new FrameAdvertencia(FramePrincipal.getInstancie(), "No se permiten vertices con id duplicados");
						frameAdvertencia.setVisible(true);
						FramePrincipal.getInstancie().setEnabled(false); // se inhabilita el frame principal
					}
					FramePrincipal.getInstancie().setInserctionVertex(false); // se inhabilita la inserccion de vertices
					FramePrincipal.getInstancie().setnombreCiudad(""); // se pone nombre por defecto (se libera memoria)
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
				else if (FramePrincipal.getInstancie().isDeleteArista()) { // Si está habilitada la insercción de aristas
					ParVertex parVertex = Controlador.getInstancie().buscarAristaCercana(e.getX(), e.getY()); // se obtienen los pares de vertices de la arista mas cercana al punto
					if (parVertex != null) { // se se seleccionó una arista
						FrameDecisor frameDecisor = new FrameDecisor(FramePrincipal.getInstancie(), "Seguro que desea eliminar la arista seleccionada?", () -> {
							Controlador.getInstancie().eliminarArista(parVertex.getVertexInicial(), parVertex.getVertexFinal()); // se elimina la arista que tiene como inicio y fin a esos vertices
							FramePrincipal.getInstancie().setDeleteArista(false); // se inhabilita la eliminación de arista
						});
						frameDecisor.setVisible(true);
						FramePrincipal.getInstancie().setEnabled(false); // se inhabilita el frame principal
					}

				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		setLayout(null);
		actualizarInfoLienzo();
		setBackground(SystemColor.inactiveCaptionBorder);
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//actualizarVerticesLienzo();

		actualizarAristasLienzo(g);
		if (Par.vertexInicial != null) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Para suavizar los bordes
			g2.setColor(Color.BLACK);
			Elemento elementoInicial = (Elemento) Par.vertexInicial.getInfo();
			int x1 = (int) (elementoInicial.getX() + elementoInicial.getAncho() / 2);
			int y1 = (int) (elementoInicial.getY() + elementoInicial.getLargo() / 2);
			// Calcula el ángulo de la línea entre los centros

			g2.drawLine(x1, y1, mouseX, mouseY); // se traza la recta en direccion a al mouse
			
		}



	}

	private void actualizarVerticesLienzo() {
		LinkedList<Vertex> verticesGrafo = Controlador.getInstancie().getGrafoKNN().getVerticesList(); // se obtiene la lista de vertices del grafo
		removeAll(); // se remueven todos los componentes
		// Se recorre la lista de vertices para pintar los vertices del grafo
		for (Vertex vertex : verticesGrafo) {
			add(new VertexPanel(vertex)); // se crea un componente vertexPanel con la información del vértice
		}
	}

	private void actualizarAristasLienzo(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Para suavizar los bordes

		LinkedList<Vertex> verticesGrafo = Controlador.getInstancie().getGrafoKNN().getVerticesList(); // se obtiene la lista de vertices del grafo

		for (Vertex vertex : verticesGrafo) {
			Elemento elementoInicial = (Elemento) vertex.getInfo(); // se obtiene al elemento inicial
			for (Edge edge : vertex.getEdgeList()) {
				Elemento elementoFinal = (Elemento) edge.getVertex().getInfo(); // se obtiene al elemento final
				// Variables que representan las posiciones donde se van a trazar las rectas
				int x1 = (int) (elementoInicial.getX() + elementoInicial.getAncho() / 2);
				int y1 = (int) (elementoInicial.getY() + elementoInicial.getLargo() / 2);
				int x2 = (int) (elementoFinal.getX() + elementoFinal.getAncho() / 2);
				int y2 = (int) (elementoFinal.getY() + elementoFinal.getLargo() / 2);

				// Calcula el ángulo de la línea entre los centros
				double angulo = Math.atan2(y2 - y1, x2 - x1);

				// Calcula las nuevas coordenadas de inicio y fin de la línea, teniendo en cuenta el tamaño de los elementos
				int nuevoX1 = (int) (x1 + (elementoInicial.getAncho() / 2) * Math.cos(angulo));
				int nuevoY1 = (int) (y1 + (elementoInicial.getLargo() / 2) * Math.sin(angulo));
				int nuevoX2 = (int) (x2 - (elementoFinal.getAncho() / 2) * Math.cos(angulo));
				int nuevoY2 = (int) (y2 - (elementoFinal.getLargo() / 2) * Math.sin(angulo));

				// Definir el color
				if (Line2D.ptSegDist(nuevoX1, nuevoY1, nuevoX2, nuevoY2, mouseX, mouseY) < 5.0) {
					g2.setColor(Color.RED);							
				}
				else {
					g2.setColor(Color.BLACK);
				}

				// Dibuja la línea
				g2.drawLine(nuevoX1, nuevoY1, nuevoX2, nuevoY2);

				// Dibuja la flecha al final de la línea
				int size = 10; // Tamaño de la flecha
				int dx = nuevoX2 - nuevoX1, dy = nuevoY2 - nuevoY1;
				double D = Math.sqrt(dx*dx + dy*dy);
				double xm = D - size, xn = xm, ym = size, yn = -size, x;
				double sin = dy / D, cos = dx / D;

				x = xm*cos - ym*sin + nuevoX1;
				ym = xm*sin + ym*cos + nuevoY1;
				xm = x;

				x = xn*cos - yn*sin + nuevoX1;
				yn = xn*sin + yn*cos + nuevoY1;
				xn = x;

				int[] xpoints = {nuevoX2, (int) xm, (int) xn};
				int[] ypoints = {nuevoY2, (int) ym, (int) yn};


				g2.fillPolygon(xpoints, ypoints, 3);

				// Dibujar la ponderación de la arista
				int xCenter = (x1 + x2) / 2;
				int yCenter = (y1 + y2) / 2;
				// se actualiza la ponderacion de la arista
				Controlador.getInstancie().actualizarPonderacionArista((WeightedEdge) edge, vertex, edge.getVertex());
				String weight = String.valueOf(((WeightedEdge)edge).getWeight()); // Asume que Edge tiene un método getWeight que devuelve la ponderación de la arista
				g2.drawString(weight, xCenter, yCenter);
			}
		}
	}



	public void actualizarInfoLienzo () {
		this.actualizarVerticesLienzo();
		this.repintarse();
	}

	public void repintarse() {
		repaint();
		revalidate();
	}


}