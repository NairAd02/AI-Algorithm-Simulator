package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Iterator;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import logica.Clase;
import logica.Controlador;
import logica.Dbscan;
import logica.Knn;

public class FrameInfoProyecto extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblNewLabel;
	private JPanel panelContenedor;
	private JLabel lblX;
	private int mouseX, mouseY;
	private JScrollPane scrollPane;
	private JPanel panelInfoProyectoKNN;
	private JLabel lblAlgoritmo;
	private JLabel lblAlgoritmoDinamico;
	private JLabel lblAlgoritmo_1;
	private JLabel lblKDinamica;
	private JPanel panelInfoDbscan;
	private JLabel lblAlgoritmo_2;
	private JLabel lblAlgoritmo_3;
	private JLabel lblAlgoritmo_4;
	private JLabel lblRadioDinamico;
	private JLabel lblAlgoritmo_5;
	private JLabel lblMinimoPuntosDinamico;


	public FrameInfoProyecto() {
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(0, 0, 0)));
		// se ubica el modal en pantalla
		setLocationRelativeTo(FramePrincipal.getInstancie());
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panelSuperior = new JPanel();
		panelSuperior.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {

				int x= e.getXOnScreen();
				int y= e.getYOnScreen();

				setLocation(x - mouseX , y - mouseY );

			}
		});
		panelSuperior.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		panelSuperior.setBackground(new Color(35, 47, 59));

		contentPane.add(panelSuperior, BorderLayout.NORTH);
		panelSuperior.setLayout(new BorderLayout(0, 0));

		lblNewLabel = new JLabel("Información Proyecto");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(SystemColor.textHighlightText);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 24));
		panelSuperior.add(lblNewLabel);

		lblX = new JLabel("X  ");
		lblX.setForeground(SystemColor.textHighlightText);
		lblX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cerrarFrame(); // se cierra el frame actual
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblX.setForeground(SystemColor.RED);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblX.setForeground(SystemColor.textHighlightText);
			}
		});
		lblX.setFont(new Font("Dialog", Font.PLAIN, 24));
		panelSuperior.add(lblX, BorderLayout.EAST);

		panelContenedor = new JPanel();
		panelContenedor.setBackground(SystemColor.textHighlightText);
		contentPane.add(panelContenedor, BorderLayout.CENTER);
		panelContenedor.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		panelContenedor.add(scrollPane, BorderLayout.CENTER);

		if (Controlador.getInstancie().getAlgoritmoK() instanceof Knn) // si se seleccionó KNN
			this.addPanelKNN(); // se añade el panel con el info del Knn
		else // si se seleccionó Dbscan
			this.addPanelDbscan(); // se añade el panel con la info del Dbscan

	}

	private void addPanelKNN () {
		panelInfoProyectoKNN = new JPanel();
		scrollPane.setViewportView(panelInfoProyectoKNN);
		panelInfoProyectoKNN.setLayout(null);

		lblAlgoritmo = new JLabel("Algoritmo:");
		lblAlgoritmo.setHorizontalAlignment(SwingConstants.LEFT);
		lblAlgoritmo.setForeground(SystemColor.infoText);
		lblAlgoritmo.setFont(new Font("Dialog", Font.BOLD, 24));
		lblAlgoritmo.setBounds(10, 11, 128, 32);
		panelInfoProyectoKNN.add(lblAlgoritmo);

		lblAlgoritmoDinamico = new JLabel("KNN");
		lblAlgoritmoDinamico.setHorizontalAlignment(SwingConstants.LEFT);
		lblAlgoritmoDinamico.setForeground(SystemColor.infoText);
		lblAlgoritmoDinamico.setFont(new Font("Dialog", Font.PLAIN, 24));
		lblAlgoritmoDinamico.setBounds(148, 11, 272, 32);
		panelInfoProyectoKNN.add(lblAlgoritmoDinamico);

		lblAlgoritmo_1 = new JLabel("K:");
		lblAlgoritmo_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblAlgoritmo_1.setForeground(SystemColor.infoText);
		lblAlgoritmo_1.setFont(new Font("Dialog", Font.BOLD, 24));
		lblAlgoritmo_1.setBounds(10, 66, 36, 32);
		panelInfoProyectoKNN.add(lblAlgoritmo_1);

		lblKDinamica = new JLabel(String.valueOf(((Knn) Controlador.getInstancie().getAlgoritmoK()).getK()));
		lblKDinamica.setHorizontalAlignment(SwingConstants.LEFT);
		lblKDinamica.setForeground(SystemColor.infoText);
		lblKDinamica.setFont(new Font("Dialog", Font.PLAIN, 24));
		lblKDinamica.setBounds(51, 66, 36, 32);
		panelInfoProyectoKNN.add(lblKDinamica);
	}

	private void addPanelDbscan () {
		panelInfoDbscan = new JPanel();
		scrollPane.setViewportView(panelInfoDbscan);
		panelInfoDbscan.setLayout(null);

		lblAlgoritmo_2 = new JLabel("Algoritmo:");
		lblAlgoritmo_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblAlgoritmo_2.setForeground(SystemColor.textText);
		lblAlgoritmo_2.setFont(new Font("Dialog", Font.BOLD, 24));
		lblAlgoritmo_2.setBounds(10, 11, 132, 32);
		panelInfoDbscan.add(lblAlgoritmo_2);

		lblAlgoritmo_3 = new JLabel("DBSCAN");
		lblAlgoritmo_3.setHorizontalAlignment(SwingConstants.LEFT);
		lblAlgoritmo_3.setForeground(SystemColor.textText);
		lblAlgoritmo_3.setFont(new Font("Dialog", Font.PLAIN, 24));
		lblAlgoritmo_3.setBounds(146, 11, 132, 32);
		panelInfoDbscan.add(lblAlgoritmo_3);

		lblAlgoritmo_4 = new JLabel("Radio:");
		lblAlgoritmo_4.setHorizontalAlignment(SwingConstants.LEFT);
		lblAlgoritmo_4.setForeground(SystemColor.textText);
		lblAlgoritmo_4.setFont(new Font("Dialog", Font.BOLD, 24));
		lblAlgoritmo_4.setBounds(10, 54, 82, 32);
		panelInfoDbscan.add(lblAlgoritmo_4);

		lblRadioDinamico = new JLabel(String.valueOf(((Dbscan) Controlador.getInstancie().getAlgoritmoK()).getRadio()));
		lblRadioDinamico.setHorizontalAlignment(SwingConstants.LEFT);
		lblRadioDinamico.setForeground(SystemColor.textText);
		lblRadioDinamico.setFont(new Font("Dialog", Font.PLAIN, 24));
		lblRadioDinamico.setBounds(102, 54, 330, 32);
		panelInfoDbscan.add(lblRadioDinamico);

		lblAlgoritmo_5 = new JLabel("Mínimo de Puntos:");
		lblAlgoritmo_5.setHorizontalAlignment(SwingConstants.LEFT);
		lblAlgoritmo_5.setForeground(SystemColor.textText);
		lblAlgoritmo_5.setFont(new Font("Dialog", Font.BOLD, 24));
		lblAlgoritmo_5.setBounds(10, 94, 223, 32);
		panelInfoDbscan.add(lblAlgoritmo_5);

		lblMinimoPuntosDinamico = new JLabel(String.valueOf(((Dbscan) Controlador.getInstancie().getAlgoritmoK()).getNumeroMinPuntos()));
		lblMinimoPuntosDinamico.setHorizontalAlignment(SwingConstants.LEFT);
		lblMinimoPuntosDinamico.setForeground(SystemColor.textText);
		lblMinimoPuntosDinamico.setFont(new Font("Dialog", Font.PLAIN, 24));
		lblMinimoPuntosDinamico.setBounds(238, 94, 194, 32);
		panelInfoDbscan.add(lblMinimoPuntosDinamico);
	}


	public void cerrarFrame () {
		FramePrincipal.getInstancie().setEnabled(true); // se habilita el frame principal
		dispose();
	}
}
