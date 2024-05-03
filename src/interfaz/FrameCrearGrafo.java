package interfaz;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import logica.Controlador;
import logica.Grafo;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class FrameCrearGrafo extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblX;
	private JLabel lblConfirm;
	private int mouseX, mouseY;
	private JLabel lblNewLabel;
	private JSpinner spinnerValorK;




	public FrameCrearGrafo() {

		setUndecorated(true);

		setSize(442, 270);
		contentPane = new JPanel();
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {

				int x= e.getXOnScreen();
				int y= e.getYOnScreen();

				setLocation(x - mouseX , y - mouseY );

			}
		});
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		try {
			setLocationRelativeTo(FramePrincipal.getInstancie());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(4, 4, 4, 4, (Color) new Color(0, 0, 0)));
		panel.setLayout(null);
		panel.setBackground(new Color(35, 47, 59));
		panel.setBounds(0, 0, 442, 270);
		contentPane.add(panel);

		lblConfirm = new JLabel("Confirmar");
		lblConfirm.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		lblConfirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblConfirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Controlador.getInstancie().setGrafo(new Grafo((int) spinnerValorK.getValue())); // se crea el grafo
				FramePrincipal.getInstancie().actualizarEstadoBotones(); // se actualiza el estado de los botones del frame principal
				FramePrincipal.getInstancie().actualizarLienzo(); // se actualiza la info del lienzo
				FramePrincipal.lanzarNotificacion("Se a creado con exito el grafo");
				cerrarFrame(); // se cierra el frame actual
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblConfirm.setBackground(SystemColor.activeCaptionBorder);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblConfirm.setBackground(Color.WHITE);
			}
		});
		lblConfirm.setOpaque(true);
		lblConfirm.setHorizontalAlignment(SwingConstants.CENTER);
		lblConfirm.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lblConfirm.setBackground(Color.WHITE);
		lblConfirm.setBounds(150, 208, 138, 35);
		panel.add(lblConfirm);

		lblX = new JLabel("X");
		lblX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cerrarFrame(); // se cierra el frame actual
			}	
			@Override
			public void mouseEntered(MouseEvent e) {
				lblX.setForeground(SystemColor.red);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblX.setForeground(SystemColor.textHighlightText);
			}
		});
		lblX.setHorizontalAlignment(SwingConstants.CENTER);
		lblX.setForeground(SystemColor.textHighlightText);
		lblX.setFont(new Font("Arial Black", Font.PLAIN, 20));
		lblX.setBackground(Color.BLACK);
		lblX.setBounds(404, 0, 38, 38);
		panel.add(lblX);

		lblNewLabel = new JLabel("Crear nuevo Grafo:");
		lblNewLabel.setBackground(Color.BLACK);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setForeground(SystemColor.textHighlightText);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 26));
		lblNewLabel.setBounds(10, 40, 243, 28);
		panel.add(lblNewLabel);

		JLabel lblIdentificador = new JLabel("Seleccione Valor de K:");
		lblIdentificador.setHorizontalAlignment(SwingConstants.LEFT);
		lblIdentificador.setForeground(SystemColor.textHighlightText);
		lblIdentificador.setFont(new Font("Dialog", Font.BOLD, 20));
		lblIdentificador.setBackground(Color.BLACK);
		lblIdentificador.setBounds(107, 89, 227, 28);
		panel.add(lblIdentificador);

		spinnerValorK = new JSpinner();
		spinnerValorK.setModel(new SpinnerNumberModel(1, 1, 21, 2));
		spinnerValorK.setFont(new Font("Dialog", Font.PLAIN, 20));
		spinnerValorK.setBounds(177, 128, 88, 28);
		
		panel.add(spinnerValorK);

	}

	

	private void cerrarFrame () {
		try {
			FramePrincipal.getInstancie().setEnabled(true);
		} catch (Exception e) {

			e.printStackTrace();
		}
		dispose();
	}
}
