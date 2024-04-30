package interfaz;

import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.border.MatteBorder;

import cu.edu.cujae.ceis.graph.interfaces.ILinkedWeightedEdgeDirectedGraph;
import logica.Controlador;
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
	private String nombreCiudad;
	private JLabel lblAddEdge;
	private JLabel lblAddVertex;
	private GraphPanel lienzo;
	private JPanel panelLateral;
	private JPanel panelOpcionesLateral;
	private JLabel lblRelleno;
	private JLabel lblEliminarVertice;
	private JLabel lblEliminarArista;
	private JPanel panelSuperior;
	private JPanel panelMenuSuperior;
	private JLabel lblSalir;



	// Metodo para lanzar una notificación
	public static void lanzarNotificacion (String mensaje) {
		FrameNotificacion notificacion = new FrameNotificacion(mensaje);
		notificacion.setVisible(true);
	}


	public String getnombreCiudad() {
		return nombreCiudad;
	}

	public void setnombreCiudad(String nombreGrafo) {
		this.nombreCiudad = nombreGrafo;
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
		this.lienzo = new GraphPanel();

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panelCentral = new JPanel();
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

		lblAddVertex = new JLabel("Insertar Vertice");
		lblAddVertex.setOpaque(true);
		lblAddVertex.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblAddVertex.setForeground(SystemColor.textHighlightText);
		lblAddVertex.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddVertex.setBounds(10, 44, 177, 32);
		lblAddVertex.setBackground(new Color(35, 47, 59));
		lblAddVertex.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
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
			@Override
			public void mouseEntered(MouseEvent e) {
				lblAddVertex.setBackground(SystemColor.activeCaptionBorder);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblAddVertex.setBackground(new Color(35, 47, 59));
			}
		});
		panelOpcionesLateral.setLayout(null);

		lblAddVertex.setBorder(new MatteBorder(2, 2, 2, 2, (Color) SystemColor.textHighlightText));
		lblAddVertex.setFont(new Font("Dialog", Font.PLAIN, 21));
		panelOpcionesLateral.add(lblAddVertex);

		lblAddEdge = new JLabel("Insertar Arista");
		lblAddEdge.setOpaque(true);
		lblAddEdge.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblAddEdge.setForeground(SystemColor.textHighlightText);
		lblAddEdge.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddEdge.setBounds(10, 196, 177, 32);
		lblAddEdge.setBackground(new Color(35, 47, 59));
		lblAddEdge.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (Controlador.getInstancie().cantVertices() > 1) { // si la cantdad de vertices es mayor que 1
					// se muestra el frame advertencia para informar al usuario de lo que debe de hacer
					FrameAdvertencia frameAdvertencia = new FrameAdvertencia(FramePrincipal.this, "Seleccione dos vertices para trazar la arista");
					frameAdvertencia.setVisible(true);
					setEnabled(false); // se inhabilita el frame
					isInserctionEdge = true; // se habilita la logica de inserccion de aristas
					// se inhabilitan las demás opciones
					isDeleteArista = false;
					isInserctionVertex = false;
					isDeleteVertex = false;
				}
				else {
					// se muestra el frame advertencia para advertir del error
					FrameAdvertencia frameAdvertencia = new FrameAdvertencia(FramePrincipal.this, "Al menos deben de existir dos vertices para poder trazar aristas");
					frameAdvertencia.setVisible(true);
					setEnabled(false); // se inhabilita el frame
				}

			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblAddEdge.setBackground(SystemColor.activeCaptionBorder);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblAddEdge.setBackground(new Color(35, 47, 59));
			}
		});
		lblAddEdge.setFont(new Font("Dialog", Font.PLAIN, 21));
		lblAddEdge.setBorder(new MatteBorder(2, 2, 2, 2, (Color) SystemColor.textHighlightText));
		panelOpcionesLateral.add(lblAddEdge);

		lblEliminarVertice = new JLabel("Eliminar Vertice");
		lblEliminarVertice.setOpaque(true);
		lblEliminarVertice.setBackground(new Color(35, 47, 59));
		lblEliminarVertice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblEliminarVertice.setBackground(SystemColor.activeCaptionBorder);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblEliminarVertice.setBackground(new Color(35, 47, 59));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (Controlador.getInstancie().cantVertices() != 0) { // se existe al menos un vertice
					FrameAdvertencia frameAdvertencia = new FrameAdvertencia(FramePrincipal.this, "Seleccione el vertice que desea eliminar");
					frameAdvertencia.setVisible(true);
					setEnabled(false); // se inhabilita el frame principal
					isDeleteVertex = true; // se habilita la eliminación de un vértice
					// se inhabilitan las demás opciones
					isDeleteArista = false;
					isInserctionVertex = false;
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
		});
		lblEliminarVertice.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblEliminarVertice.setForeground(SystemColor.textHighlightText);
		lblEliminarVertice.setHorizontalAlignment(SwingConstants.CENTER);
		lblEliminarVertice.setFont(new Font("Dialog", Font.PLAIN, 21));
		lblEliminarVertice.setBorder(new MatteBorder(2, 2, 2, 2, (Color) SystemColor.textHighlightText));
		lblEliminarVertice.setBounds(10, 120, 177, 32);
		panelOpcionesLateral.add(lblEliminarVertice);

		lblEliminarArista = new JLabel("Eliminar Arista");
		lblEliminarArista.setOpaque(true);
		lblEliminarArista.setBackground(new Color(35, 47, 59));
		lblEliminarArista.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblEliminarArista.setBackground(SystemColor.activeCaptionBorder);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblEliminarArista.setBackground(new Color(35, 47, 59));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				if (Controlador.getInstancie().cantAristas() != 0) { // si existe al menos una arista
					FrameAdvertencia frameAdvertencia = new FrameAdvertencia(FramePrincipal.this, "Seleccione la arista que desea eliminar");
					frameAdvertencia.setVisible(true);
					setEnabled(false); // se inhabilita el frame principal
					isDeleteArista = true; // se habilita la eliminacion de arista
					// se inhabilitan las demás opciones
					isInserctionEdge = false;
					// se reincia además del valor de los par de vertices como parte de la cancelación de insercción de arista
					Par.vertexInicial = null;
					Par.vertexFinal = null;
					isInserctionVertex = false;
					isDeleteVertex = false;
				}
				else {
					FrameAdvertencia frameAdvertencia = new FrameAdvertencia(FramePrincipal.this, "Debe de haber al menos una arista para realizar una eliminacion");
					frameAdvertencia.setVisible(true);
					setEnabled(false); // se inhabilita el frame principal
				}
			}
		});
		lblEliminarArista.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblEliminarArista.setForeground(SystemColor.textHighlightText);
		lblEliminarArista.setHorizontalAlignment(SwingConstants.CENTER);
		lblEliminarArista.setFont(new Font("Dialog", Font.PLAIN, 21));
		lblEliminarArista.setBorder(new MatteBorder(2, 2, 2, 2, (Color) SystemColor.textHighlightText));
		lblEliminarArista.setBounds(10, 272, 177, 32);
		panelOpcionesLateral.add(lblEliminarArista);

		lblRelleno = new JLabel();
		lblRelleno.setHorizontalAlignment(SwingConstants.LEFT);
		lblRelleno.setIcon(new ImageIcon(FramePrincipal.class.getResource("/img/logo2.jpg")));
		lblRelleno.setBackground(SystemColor.info);
		panelLateral.add(lblRelleno, BorderLayout.NORTH);

		panelCentral.add(lienzo, BorderLayout.CENTER);

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
		mnNewMenu.setIcon(new ImageIcon(FramePrincipal.class.getResource("/img/Home Page.png")));
		mnNewMenu.setFont(new Font("Dialog", Font.PLAIN, 24));
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("Nuevo Grafo");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Guardar Grafo");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Se crea un sistema de exploracion

				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {// si se seleccion� un archivo
					File selectedFile = fileChooser.getSelectedFile(); // se obtiene el archivo selecionado
					// se guarda el grafo en memoria externa
					try {
						ManejoDirectorios.guardarArchivo(Controlador.getInstancie().getGrafoKNN(), selectedFile.getAbsolutePath());
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
		mnNewMenu.add(mntmNewMenuItem_2);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Cargar Grafo");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {// si se seleccion� un archivo
					File selectedFile = fileChooser.getSelectedFile(); // se obtiene el archivo selecionado
					// Se carga el grafo de memoria externa
					try {
						Controlador.getInstancie().setGrafoKNN((ILinkedWeightedEdgeDirectedGraph)ManejoDirectorios.recuperarArchivo(selectedFile.getAbsolutePath()));
						actualizarLienzo(); // se actualiza el lienzo para mostrar la información del grafo cargado
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
		mnNewMenu.add(mntmNewMenuItem_1);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Importar PNG");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Se crea un sistema de exploracion

				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {// si se seleccion� un archivo
					File selectedFile = fileChooser.getSelectedFile();
					ManejoImagen.generarImagen(lienzo, selectedFile.getAbsolutePath());

				}
				/*
				FrameProyectosAbrir frameProyectosAbrir = new FrameProyectosAbrir(Principal.this);
				frameProyectosAbrir.setVisible(true);
				setEnabled(false)*/
			}
		});

		mnNewMenu.add(mntmNewMenuItem_3);

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
		lienzo.repintarse(); // se repinta el lienzo
	}

	public static FramePrincipal getInstancie () {
		if (framePrincipal == null)
			framePrincipal = new FramePrincipal();

		return framePrincipal;
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
