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
import logica.Knn;
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
	private boolean dragging;


	/**
	 * Create the panel.
	 */
	public VertexPanel(Vertex v) {
		this.dragging = false;
		setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(0, 0, 0)));

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				
				dragging = true;
				int x = e.getXOnScreen()- FramePrincipal.getInstancie().getLienzo().getLocationOnScreen().x;
				int y = e.getYOnScreen()- FramePrincipal.getInstancie().getLienzo().getLocationOnScreen().y;
				Controlador.getInstancie().getAlgoritmoK().actualizarPosicionesVertice(vertex, x - mouseX, y - mouseY); // se actualizan las posiciones del vértice
				if((x - mouseX)>-30&&(y-mouseY>-30)&&(x - mouseX)<2000-getWidth()&&
						(y - mouseY)<2000-getHeight()) {
					setLocation(x - mouseX, y-mouseY);
					FramePrincipal.getInstancie().actualizarAristasLienzo(); // se actualizan las aristas del lienzo
					FramePrincipal.getInstancie().actualizarColoresPanelesLienzo(); // se actualiza los colores de los paneles en el lienzo
				}

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
							Controlador.getInstancie().getAlgoritmoK().addArista(Par.vertexInicial, Par.vertexFinal);
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
						Controlador.getInstancie().getAlgoritmoK().eliminarVertice(vertex); // se elimina el vertice de la logica del negocio
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
				if (!dragging) { // si no se está moviendo el cursor
					FramePrincipal.getInstancie().setVertexSeleccionado(vertex); // se marca como vértice seleccionado
					FramePrincipal.getInstancie().actualizarAristasLienzo(); // se actualiza las aristas del lienzo
					

				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!dragging) { // si no se está moviendo el cursor
					FramePrincipal.getInstancie().setVertexSeleccionado(null); // se indica que ya no está seleccionado el vértice
					FramePrincipal.getInstancie().actualizarAristasLienzo(); // se actualiza las aristas del lienzo
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				dragging = false; // Se ha soltado el ratón
			}

		});
		//setBackground(SystemColor.textText);
		//setForeground(SystemColor.infoText);
		this.vertex = v;

		setLocation( (int)((Elemento) this.vertex.getInfo()).getX(), (int)((Elemento) this.vertex.getInfo()).getY()); // se establece una localizacion para el componente
		//setSize(50, 50);
		setLayout(new BorderLayout(0, 0));
		this.actualizarColorPanel(); // se actualiza el color del panel
		lblInfoElemento = new JLabel(((Elemento) this.vertex.getInfo()).getId());
	

		lblInfoElemento.setForeground(SystemColor.textHighlightText);
		lblInfoElemento.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoElemento.setFont(new Font("Dialog", Font.BOLD, 16));
		add(lblInfoElemento, BorderLayout.CENTER);


		setSize(getPreferredSize().width + 35, getPreferredSize().height + 35);

		// se actualizan las dimensiones del vertice
		((Elemento) this.vertex.getInfo()).setAncho(getWidth());
		((Elemento) this.vertex.getInfo()).setLargo(getHeight());
		// solo se aplica el procedimiento si fue seleccionado el algoritmo KNN
		if (Controlador.getInstancie().getAlgoritmoK() instanceof Knn) {
			if (((Elemento) this.vertex.getInfo()).isClasificado()) { // si es un vértice clasificado

				JLabel lblClasificado = new JLabel("Class"); // se marca como un clasificado
				lblClasificado.setForeground(SystemColor.WHITE);
				lblClasificado.setFont(new Font("Dialog", Font.BOLD, 11));
				add(lblClasificado, BorderLayout.NORTH);
			}
		}

	}



	public void actualizarColorPanel () {
		Elemento elemento = (Elemento) this.vertex.getInfo();
		if ( elemento.getClase() == null) // se trata de un vertice no clasificado
			setBackground(new Color (56, 121, 163));
		else
			setBackground(new Color(elemento.getClase().getColor().getRed(), elemento.getClase().getColor().getGreen(), 
					elemento.getClase().getColor().getBlue()));
	}


}
