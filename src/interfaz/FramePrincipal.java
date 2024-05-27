package interfaz;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.border.MatteBorder;
import cu.edu.cujae.ceis.graph.interfaces.ILinkedWeightedEdgeDirectedGraph;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.AlgoritmoK;
import logica.Controlador;
import logica.Knn;
import utils.ManejoDirectorios;
import utils.ManejoImagen;
import utils.Par;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import javax.swing.JMenuBar;
import java.awt.FlowLayout;
import javax.swing.JMenu;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Cursor;

public class FramePrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static FramePrincipal framePrincipal;
	private boolean isInserctionVertex, isInserctionEdge, isDeleteVertex, isDeleteArista;
	private String nombreCiudad; // atributo que representa el nombre de la ciudad seleccionado
	private String clase; // atributo que representa el nombre de la clase seleccionado 
	private JLabel lblAddVertex;
	private GraphPanel lienzo;
	private JPanel panelLateral;
	private JPanel panelOpcionesLateral;
	private JLabel lblRelleno;
	private JLabel lblEliminarVertice;
	private JPanel panelSuperior;
	private JPanel panelMenuSuperior;
	private JLabel lblSalir;
	private JMenuBar menuBarOpciones;
	private JMenu mnOpciones;
	private JMenuItem mntmVerLeyenda;
	private JMenuItem mntmNewMenuItem_5;
	private JPanel panelCentral;
	private JMenuItem mntmGuardarGrafo;
	private JMenuItem mntmNuevoGrafo;
	private JMenuItem mntmCargarGrafo;
	private JMenuItem mntmImportarImagen;
	private Vertex vertexSeleccionado; // representa el vértice seleccionado (no mezcla logica con interfaz ******)
	private JMenuItem mntmClasificadosCsv;
	private JMenuItem mntmNewMenuItem_1;
	private JMenuItem mntmCargarNoClasificados;
	private JMenuItem mntmNewMenuItem;




	// Metodo para lanzar una notificación
	public static void lanzarNotificacion (String mensaje) {
		FrameNotificacion notificacion = new FrameNotificacion(mensaje);
		notificacion.setVisible(true);
	}




	public String getNombreCiudad() {
		return nombreCiudad;
	}

	public void setNombreCiudad(String nombreGrafo) {
		this.nombreCiudad = nombreGrafo;
	}

	public String getClase() {
		return clase;
	}


	public void setClase(String clase) {
		this.clase = clase;
	}


	public boolean isInserctionVertex() {
		return isInserctionVertex;
	}

	public void setInserctionVertex(boolean isTouchScreen) {
		this.isInserctionVertex = isTouchScreen;
	}


	public boolean isInserctionEdge() {
		return isInserctionEdge;
	}

	public void setInserctionEdge(boolean isInserctionEdge) {
		this.isInserctionEdge = isInserctionEdge;
	}



	public Vertex getVertexSeleccionado() {
		return vertexSeleccionado;
	}


	public void setVertexSeleccionado(Vertex vertexSeleccionado) {
		this.vertexSeleccionado = vertexSeleccionado;
	}


	public boolean isDeleteVertex() {
		return isDeleteVertex;
	}

	public void setDeleteVertex(boolean isDeleteVertex) {
		this.isDeleteVertex = isDeleteVertex;
	}

	public boolean isDeleteArista() {
		return isDeleteArista;
	}

	public void setDeleteArista(boolean isDeleteArista) {
		this.isDeleteArista = isDeleteArista;
	}

	private FramePrincipal() {
		setUndecorated(true);
		setResizable(false);
		this.nombreCiudad = "";
		this.clase = "";


		// se marcan dan valores iniciales a los boolean
		this.isInserctionVertex = false;
		this.isInserctionEdge = false;
		this.isDeleteArista = false;
		this.isDeleteVertex = false;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH); // se abre en pantalla completa al frame
		setBounds(100, 100, 450, 597);
		contentPane = new JPanel();
		contentPane.setBorder(new MatteBorder(4, 4, 4, 4, (Color) new Color(0, 0, 0)));


		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		panelCentral = new JPanel();
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new BorderLayout(0, 0));

		panelLateral = new JPanel();
		panelLateral.setBorder(new MatteBorder(0, 0, 0, 3, (Color) new Color(0, 0, 0)));
		panelLateral.setBackground(new Color(35, 47, 59));
		contentPane.add(panelLateral, BorderLayout.WEST);
		panelLateral.setLayout(new BorderLayout(0, 0));

		panelOpcionesLateral = new JPanel();
		panelOpcionesLateral.setBackground(new Color(35, 47, 59));
		panelLateral.add(panelOpcionesLateral, BorderLayout.CENTER);

		lblAddVertex = new JLabel("Insertar Elemento");
		lblAddVertex.setOpaque(true);
		lblAddVertex.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblAddVertex.setForeground(SystemColor.textHighlightText);
		lblAddVertex.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddVertex.setBounds(10, 44, 177, 32);
		lblAddVertex.setBackground(new Color(35, 47, 59));
		lblAddVertex.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (lblAddVertex.isEnabled()) { // si el boton se encuentra habilitado
					FrameAddVertice frameAddVertice = new FrameAddVertice();
					frameAddVertice.setVisible(true);
					setEnabled(false); // se inhabilita del frame principal
					// se inhabilitan las demas opciones
					isDeleteArista = false;
					isDeleteVertex = false;
					isInserctionEdge = false;
					// se reincia además del valor de los par de vertices como parte de la cancelación de insercción de arista
					Par.vertexInicial = null;
					Par.vertexFinal = null;
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if (lblAddVertex.isEnabled())
					lblAddVertex.setBackground(SystemColor.activeCaptionBorder);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (lblAddVertex.isEnabled())
					lblAddVertex.setBackground(new Color(35, 47, 59));
			}
		});
		panelOpcionesLateral.setLayout(null);

		lblAddVertex.setBorder(new MatteBorder(2, 2, 2, 2, (Color) SystemColor.textHighlightText));
		lblAddVertex.setFont(new Font("Dialog", Font.PLAIN, 21));
		panelOpcionesLateral.add(lblAddVertex);

		lblEliminarVertice = new JLabel("Eliminar Elemento");
		lblEliminarVertice.setOpaque(true);
		lblEliminarVertice.setBackground(new Color(35, 47, 59));
		lblEliminarVertice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (lblEliminarVertice.isEnabled())
					lblEliminarVertice.setBackground(SystemColor.activeCaptionBorder);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (lblEliminarVertice.isEnabled())
					lblEliminarVertice.setBackground(new Color(35, 47, 59));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (lblEliminarVertice.isEnabled()) { // si el boton se encuentra habilitado
					if (Controlador.getInstancie().getAlgoritmoK().cantVertices() != 0) { // se existe al menos un vertice
						FrameAdvertencia frameAdvertencia = new FrameAdvertencia(FramePrincipal.this, "Seleccione el vertice que desea eliminar");
						frameAdvertencia.setVisible(true);
						setEnabled(false); // se inhabilita el frame principal
						isDeleteVertex = true; // se habilita la eliminación de un vértice
						// se inhabilitan las demás opciones
						isDeleteArista = false;
						isInserctionVertex = false;
						// ademas se libera la memoria posiblemente ocupada de las variables que intervienen en la insercción del vértice
						nombreCiudad = "";
						clase = "";
						isInserctionEdge = false;
						// se reincia además del valor de los par de vertices como parte de la cancelación de insercción de arista
						Par.vertexInicial = null;
						Par.vertexFinal = null;
					}
					else {
						FrameAdvertencia frameAdvertencia = new FrameAdvertencia(FramePrincipal.this, "Debe de existir al menos un vertice para poder realizar una eliminacion");
						frameAdvertencia.setVisible(true);
						setEnabled(false); // se inhabilita el frame principal
					}
				}
			}
		});
		lblEliminarVertice.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblEliminarVertice.setForeground(SystemColor.textHighlightText);
		lblEliminarVertice.setHorizontalAlignment(SwingConstants.CENTER);
		lblEliminarVertice.setFont(new Font("Dialog", Font.PLAIN, 21));
		lblEliminarVertice.setBorder(new MatteBorder(2, 2, 2, 2, (Color) SystemColor.textHighlightText));
		lblEliminarVertice.setBounds(10, 120, 177, 32);
		panelOpcionesLateral.add(lblEliminarVertice);

		lblRelleno = new JLabel();
		lblRelleno.setHorizontalAlignment(SwingConstants.LEFT);
		lblRelleno.setIcon(new ImageIcon(FramePrincipal.class.getResource("/img/logo2.jpg")));
		lblRelleno.setBackground(SystemColor.info);
		panelLateral.add(lblRelleno, BorderLayout.NORTH);



		panelSuperior = new JPanel();
		panelSuperior.setBorder(new MatteBorder(0, 0, 3, 0, (Color) new Color(0, 0, 0)));
		panelCentral.add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new BorderLayout(0, 0));

		panelMenuSuperior = new JPanel();
		panelSuperior.setBackground(new Color(35, 47, 59));
		FlowLayout flowLayout = (FlowLayout) panelMenuSuperior.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panelSuperior.add(panelMenuSuperior, BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		panelMenuSuperior.add(menuBar);
		panelMenuSuperior.setBackground(new Color(35, 47, 59));
		JMenu mnNewMenu = new JMenu("Archivo");
		mnNewMenu.setBorder(new MatteBorder(2, 2, 2, 2, (Color) SystemColor.textHighlightText));
		mnNewMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mnNewMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mnNewMenu.setBackground(SystemColor.activeCaptionBorder);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				mnNewMenu.setBackground(new Color(35, 47, 59));
			}
		});
		mnNewMenu.setForeground(SystemColor.textHighlightText);
		mnNewMenu.setBackground(new Color(35, 47, 59));
		mnNewMenu.setOpaque(true);
		mnNewMenu.setHorizontalAlignment(SwingConstants.CENTER);
		mnNewMenu.setIcon(new ImageIcon(FramePrincipal.class.getResource("/img/Home.png")));
		mnNewMenu.setFont(new Font("Dialog", Font.PLAIN, 24));
		menuBar.add(mnNewMenu);

		mntmNuevoGrafo = new JMenuItem("Nuevo Grafo");
		mntmNuevoGrafo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Se abre el frame para crear un nuevo grafo
				FrameCrearGrafo frameCrearGrafo = new FrameCrearGrafo();
				frameCrearGrafo.setVisible(true);
				setEnabled(false); // se inhabilita el frame principal
			}
		});
		mnNewMenu.add(mntmNuevoGrafo);

		mntmGuardarGrafo = new JMenuItem("Guardar Grafo");
		mntmGuardarGrafo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Se crea un sistema de exploracion

				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {// si se seleccion� un archivo
					File selectedFile = fileChooser.getSelectedFile(); // se obtiene el archivo selecionado
					// se guarda el grafo en memoria externa
					try {
						ManejoDirectorios.guardarArchivo(Controlador.getInstancie().getAlgoritmoK(), selectedFile.getAbsolutePath());
						FramePrincipal.lanzarNotificacion("Se ha guardado con exito el grafo");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		mnNewMenu.add(mntmGuardarGrafo);

		mntmCargarGrafo = new JMenuItem("Cargar Grafo");
		mntmCargarGrafo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {// si se seleccion� un archivo
					File selectedFile = fileChooser.getSelectedFile(); // se obtiene el archivo selecionado
					// Se carga el grafo de memoria externa
					try {
						Controlador.getInstancie().setAlgoritmoK((AlgoritmoK) ManejoDirectorios.recuperarArchivo(selectedFile.getAbsolutePath()));
						actualizarEstadoBotones(); // se actualiza el estado de los botones
						actualizarLienzo(); //se actualiza la información del lienzo para mostrar el diagrama cargado
						FramePrincipal.lanzarNotificacion("Se ha cargado el grafo seleccionado correctamente");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		mnNewMenu.add(mntmCargarGrafo);

		mntmImportarImagen = new JMenuItem("Importar PNG");
		mntmImportarImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Se crea un sistema de exploracion

				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {// si se seleccion� un archivo
					File selectedFile = fileChooser.getSelectedFile();
					ManejoImagen.generarImagen(lienzo, selectedFile.getAbsolutePath());
					FramePrincipal.lanzarNotificacion("La imagen del grafo ha sido correctamente generada");
				}
				/*
				FrameProyectosAbrir frameProyectosAbrir = new FrameProyectosAbrir(Principal.this);
				frameProyectosAbrir.setVisible(true);
				setEnabled(false)*/
			}
		});

		mnNewMenu.add(mntmImportarImagen);

		menuBarOpciones = new JMenuBar();
		panelMenuSuperior.add(menuBarOpciones);

		mnOpciones = new JMenu("Opciones");
		mnOpciones.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (mnOpciones.isEnabled()) // se está habilitado el botón
					mnOpciones.setBackground(SystemColor.activeCaptionBorder);

			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (mnOpciones.isEnabled()) // se está habilitado el botón
					mnOpciones.setBackground(new Color(35, 47, 59));
			}
		});
		mnOpciones.setIcon(new ImageIcon(FramePrincipal.class.getResource("/img/question1.png")));
		mnOpciones.setOpaque(true);
		mnOpciones.setHorizontalAlignment(SwingConstants.CENTER);
		mnOpciones.setForeground(SystemColor.textHighlightText);
		mnOpciones.setFont(new Font("Dialog", Font.PLAIN, 24));
		mnOpciones.setBorder(new MatteBorder(2, 2, 2, 2, (Color) SystemColor.textHighlightText));
		mnOpciones.setBackground(new Color(35, 47, 59));
		menuBarOpciones.add(mnOpciones);

		mntmVerLeyenda = new JMenuItem("Ver Leyenda");
		mntmVerLeyenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrameLeyenda frameLeyenda = new FrameLeyenda();
				frameLeyenda.setVisible(true);
				setEnabled(false); // se inhabilita el frame actual
			}
		});
		mnOpciones.add(mntmVerLeyenda);

		mntmNewMenuItem_5 = new JMenuItem("Ver Matriz de Distancias");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrameMatrizDistancias frameMatrizDistancias = new FrameMatrizDistancias();
				frameMatrizDistancias.setVisible(true);
				setEnabled(false); // se inhabilita el frame principal
			}
		});
		mnOpciones.add(mntmNewMenuItem_5);


		mntmClasificadosCsv = new JMenuItem("Cargar Clasificados.csv");
		mntmClasificadosCsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// se cargan y se añaden al grafo los datos clasificados
				try {
					Controlador.getInstancie().getAlgoritmoK().addElementos(Controlador.getInstancie().cargarFicheroCsvElementos("datos clasificados.csv"));
					actualizarLienzo(); // se actualiza la información del lienzo
					FramePrincipal.lanzarNotificacion("Los datos clasificados del fichero han sido cargados con exito");
				} catch (Exception e1) {

					FrameAdvertencia frameAdvertencia = new FrameAdvertencia(FramePrincipal.this, "Ya los datos de este fichero ya fueron cargados");
					frameAdvertencia.setVisible(true);
					setEnabled(false); // se inhabilita el frame actual
				}
			}
		});
		mnOpciones.add(mntmClasificadosCsv);



		mntmCargarNoClasificados = new JMenuItem("Cargar No Clasificados.csv");
		mntmCargarNoClasificados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// se cargan y se añaden al grafo los datos no clasificados
				try {
					Controlador.getInstancie().getAlgoritmoK().addElementos(Controlador.getInstancie().cargarFicheroCsvElementos("datos no clasificados.csv"));
					actualizarLienzo(); // se actualiza la información del lienzo
					FramePrincipal.lanzarNotificacion("Los datos no clasificados del fichero han sido cargados con exito");
				} catch (Exception e1) {

					FrameAdvertencia frameAdvertencia = new FrameAdvertencia(FramePrincipal.this, "Ya los datos de este fichero ya fueron cargados");
					frameAdvertencia.setVisible(true);
					setEnabled(false); // se inhabilita el frame actual
				}
			}
		});
		mnOpciones.add(mntmCargarNoClasificados);


		mntmNewMenuItem_1 = new JMenuItem("Exportar Matriz de Distancia");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Controlador.getInstancie().getAlgoritmoK().crearFicheroMatrizDistancias(); // se crea en memoria externa un fichero con la info de la matriz de distancias del grafo
					FramePrincipal.lanzarNotificacion("La actual Matriz de Distancias del Grafo ha sido exportada correctamente");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnOpciones.add(mntmNewMenuItem_1);

		mntmNewMenuItem = new JMenuItem("Ver Información Proyecto");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrameInfoProyecto frameInfoProyecto = new FrameInfoProyecto();
				frameInfoProyecto.setVisible(true);
				setEnabled(false); // se inhabilita del frame principal
			}
		});
		mnOpciones.add(mntmNewMenuItem);

		lblSalir = new JLabel("X ");
		lblSalir.setHorizontalAlignment(SwingConstants.CENTER);
		lblSalir.setForeground(SystemColor.textHighlightText);
		lblSalir.setFont(new Font("Dialog", Font.BOLD, 48));
		lblSalir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblSalir.setForeground(SystemColor.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblSalir.setForeground(SystemColor.WHITE);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				FrameDecisor frameDecisor = new FrameDecisor(FramePrincipal.this, "Seguro que desea salir del programa?", () -> {
					System.exit(0); // se cierra el programa
				});
				frameDecisor.setVisible(true);
				setEnabled(false); // se inhabilita el frame principal
			}
		});
		panelSuperior.add(lblSalir, BorderLayout.EAST);
		this.actualizarEstadoBotones(); // se actualiza el estado de los botones
		// se prepara un lienzo para pintar la información del grafo
		this.lienzo = new GraphPanel();
		panelCentral.add(lienzo, BorderLayout.CENTER); // se añade el lienzo 
		lienzo.repintarse(); // se repinta el lienzo



	}

	public void actualizarMenuOpciones () {
		// si fue seleccionado KNN
		if (Controlador.getInstancie().getAlgoritmoK() instanceof Knn) {
			this.mntmClasificadosCsv.setVisible(true);
			this.mntmCargarNoClasificados.setVisible(true);
		}
		else { // si fue seleccionado Dbscan
			this.mntmClasificadosCsv.setVisible(false);
			this.mntmCargarNoClasificados.setVisible(false);
		}
	}

	public void actualizarEstadoBotones () {
		if (Controlador.getInstancie().getAlgoritmoK() == null) { // si no ha sido cargado ningún grafo
			this.lblAddVertex.setEnabled(false);
			this.lblEliminarVertice.setEnabled(false);
			this.mnOpciones.setEnabled(false);
			this.mntmGuardarGrafo.setEnabled(false);
			this.mntmImportarImagen.setEnabled(false);
		}
		else {
			// se habilitan los botones para la manipulación de los datos del grafo
			this.lblAddVertex.setEnabled(true);
			this.lblEliminarVertice.setEnabled(true);
			this.mnOpciones.setEnabled(true);
			this.mntmGuardarGrafo.setEnabled(true);
			this.mntmImportarImagen.setEnabled(true);
		}
	}

	public static FramePrincipal getInstancie () {
		if (framePrincipal == null)
			framePrincipal = new FramePrincipal();

		return framePrincipal;
	}


	public void actualizarColoresPanelesLienzo () {
		this.lienzo.actualizarColoresPaneles();
	}

	public void actualizarAristasLienzo () {
		this.lienzo.repintarse();
	}

	public void actualizarLienzo() {
		this.lienzo.actualizarInfoLienzo();
	}

	public GraphPanel getLienzo() {
		return lienzo;
	}

	public void setLienzo(GraphPanel lienzo) {
		this.lienzo = lienzo;
	}
}
