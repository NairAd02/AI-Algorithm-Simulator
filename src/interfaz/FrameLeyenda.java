package interfaz;

import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JPanel;
import logica.Clase;
import logica.Controlador;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.SystemColor;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;

public class FrameLeyenda extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblNewLabel;
	private JPanel panelContenedor;
	private JLabel lblX;
	private int mouseX, mouseY;
	private JScrollPane scrollPane;
	private JPanel panelParClase;


	public FrameLeyenda() {
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
		
		lblNewLabel = new JLabel("Leyenda");
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
		
		panelParClase = new JPanel();
		scrollPane.setViewportView(panelParClase);
		panelParClase.setLayout(new BoxLayout(panelParClase, BoxLayout.Y_AXIS));
		this.actualizarLeyenda();
		
	}
	
	public void actualizarLeyenda () {
		Iterator<Clase> clases = Controlador.getInstancie().getAlgoritmoK().getClases().iterator(); // se obtiene el iterador de las clases
		
		while(clases.hasNext()) {
			this.panelParClase.add(new ParLeyenda(clases.next()));
		}
	}
	
	public void cerrarFrame () {
		FramePrincipal.getInstancie().setEnabled(true); // se habilita el frame principal
		dispose();
	}

}
