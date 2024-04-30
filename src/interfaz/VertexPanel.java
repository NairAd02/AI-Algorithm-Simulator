package interfaz;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.Controlador;
import logica.Elemento;
import utils.Par;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import javax.swing.border.MatteBorder;


public class VertexPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Vertex vertex;
	private int mouseX, mouseY;
	private JLabel lblInfoElemento;

	/**
	 * Create the panel.
	 */
	public VertexPanel(Vertex v) {
		setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(0, 0, 0)));
		setBackground(new Color (56, 121, 163));
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen()- FramePrincipal.getInstancie().getLienzo().getLocationOnScreen().x;
				int y = e.getYOnScreen()- FramePrincipal.getInstancie().getLienzo().getLocationOnScreen().y;
				((Elemento) vertex.getInfo()).setX(x - mouseX);
				((Elemento) vertex.getInfo()).setY(y-mouseY);
				setLocation(x - mouseX, y-mouseY);
				FramePrincipal.getInstancie().actualizarAristasLienzo();
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (FramePrincipal.getInstancie().isInserctionEdge()) { // si está habilitada la insercción de aristas
					if (Par.vertexInicial == null)
						Par.vertexInicial = vertex;
					else {
						Par.vertexFinal = vertex;
						try {
							Controlador.getInstancie().addArista(Par.vertexInicial, Par.vertexFinal);
							FramePrincipal.getInstancie().setInserctionEdge(false);
							setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
							FramePrincipal.getInstancie().actualizarAristasLienzo();
						} catch (Exception e1) {
							FrameAdvertencia frameAdvertencia = new FrameAdvertencia(FramePrincipal.getInstancie(), "No se puede trazar una arista hacia el mismo vertice");
							frameAdvertencia.setVisible(true);
							FramePrincipal.getInstancie().setEnabled(false); // se inhabilita el frame principal
						} // se añade la arista a la logica del grafo
						// se inhabilita la logica de insercción de arista
						Par.vertexInicial = null;
						Par.vertexFinal = null; 

					}

				}
				else if (FramePrincipal.getInstancie().isDeleteVertex()) { // se está habilitada la eliminación de un vértice
					FrameDecisor frameDecisor = new FrameDecisor(FramePrincipal.getInstancie(), "Seguro que desea eliminar el vertice " + ((Elemento) vertex.getInfo()).getId(), () -> {
						Controlador.getInstancie().eliminarVertice(vertex); // se elimina el vertice de la logica del negocio
						FramePrincipal.getInstancie().actualizarLienzo(); // se actualiza la información del lienzo
						FramePrincipal.getInstancie().setDeleteVertex(false); // se inhabilita la eliminación de un vértice
						FramePrincipal.lanzarNotificacion("El vertice " + vertex.getInfo() + " ha sido eliminado con exito"); // se notifica al usuario de la eliminación
					});
					frameDecisor.setVisible(true);
					FramePrincipal.getInstancie().setEnabled(false); // se inhabilita el frame principal
				}
				else {
					mouseX = e.getX();
					mouseY = e.getY();
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				setBackground(SystemColor.activeCaptionBorder);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				setBackground(new Color (56, 121, 163));
			}
		});
		//setBackground(SystemColor.textText);
		//setForeground(SystemColor.infoText);
		this.vertex = v;

		setLocation( (int)((Elemento) this.vertex.getInfo()).getX(), (int)((Elemento) this.vertex.getInfo()).getY()); // se establece una localizacion para el componente
		//setSize(50, 50);
		setLayout(new BorderLayout(0, 0));

		lblInfoElemento = new JLabel(((Elemento) this.vertex.getInfo()).getId());
		lblInfoElemento.setForeground(SystemColor.textHighlightText);
		lblInfoElemento.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoElemento.setFont(new Font("Dialog", Font.BOLD, 16));
		add(lblInfoElemento, BorderLayout.CENTER);


		setSize(getPreferredSize().width + 35, getPreferredSize().height + 35);

		// se actualizan las dimensiones del vertice
		((Elemento) this.vertex.getInfo()).setAncho(getWidth());
		((Elemento) this.vertex.getInfo()).setLargo(getHeight());
	}

	/*@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension arcs = new Dimension(20,20); //Border corners arcs {width,height}, change this to whatever you want
		int width = getWidth();
		int height = getHeight();
		Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//Draws the rounded panel with borders.
		graphics.setColor(getBackground());
		graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height); //paint background
		graphics.setColor(getForeground());
		graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height); //paint border
	}*/

}
