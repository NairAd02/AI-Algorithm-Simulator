package interfaz;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import logica.Controlador;
import logica.Dbscan;
import logica.Knn;

import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class FrameAddVertice extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblX;
	private JLabel lblConfirm;
	private int mouseX, mouseY;
	private JLabel lblNewLabel;
	private JTextField textFieldValorNode;
	private JTextField textFieldClase;
	private JLabel lblClase;
	private JCheckBox chckbxClasificado;




	public FrameAddVertice() {

		setUndecorated(true);

		setSize(442, 317);
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
		panel.setBounds(0, 0, 442, 317);
		contentPane.add(panel);

		lblConfirm = new JLabel("Confirmar");
		lblConfirm.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		lblConfirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblConfirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!textFieldValorNode.getText().equalsIgnoreCase("")) { // se fue seleccionado un id para la clase
					FramePrincipal.getInstancie().setNombreCiudad(textFieldValorNode.getText()); // se almacena el nombre que se le va a dar a la ciudad
					if (chckbxClasificado.isSelected() && !textFieldClase.getText().equalsIgnoreCase("")) { // si está habilitado el check de clasificación y fue seleccionada una clase
						FramePrincipal.getInstancie().setClase(textFieldClase.getText());
						FramePrincipal.getInstancie().setInserctionVertex(true); // se habilita la isercción de vertices
						cerrarFrame();
					}
					else if (!chckbxClasificado.isSelected()) {
						FramePrincipal.getInstancie().setInserctionVertex(true); // se habilita la isercción de vertices
						cerrarFrame();
					}
				}
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
		lblConfirm.setBounds(150, 271, 138, 35);
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

		lblNewLabel = new JLabel("Introduzca los Datos:");
		lblNewLabel.setBackground(Color.BLACK);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setForeground(SystemColor.textHighlightText);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 26));
		lblNewLabel.setBounds(10, 23, 312, 28);
		panel.add(lblNewLabel);

		textFieldValorNode = new JTextField();
		textFieldValorNode.setFont(new Font("Dialog", Font.PLAIN, 14));
		textFieldValorNode.setBounds(81, 102, 279, 35);
		panel.add(textFieldValorNode);
		textFieldValorNode.setColumns(10);

		JLabel lblIdentificador = new JLabel("Identificador:");
		lblIdentificador.setHorizontalAlignment(SwingConstants.LEFT);
		lblIdentificador.setForeground(SystemColor.textHighlightText);
		lblIdentificador.setFont(new Font("Dialog", Font.BOLD, 20));
		lblIdentificador.setBackground(Color.BLACK);
		lblIdentificador.setBounds(10, 62, 138, 28);
		panel.add(lblIdentificador);

		textFieldClase = new JTextField();
		textFieldClase.setFont(new Font("Dialog", Font.PLAIN, 14));
		textFieldClase.setColumns(10);
		textFieldClase.setBounds(81, 207, 279, 35);
		panel.add(textFieldClase);

		lblClase = new JLabel("Clase:");
		lblClase.setHorizontalAlignment(SwingConstants.LEFT);
		lblClase.setForeground(SystemColor.textHighlightText);
		lblClase.setFont(new Font("Dialog", Font.BOLD, 20));
		lblClase.setBackground(Color.BLACK);
		lblClase.setBounds(10, 175, 71, 28);
		panel.add(lblClase);

		chckbxClasificado = new JCheckBox("Vertice Clasificado");
		chckbxClasificado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarEstadoSeccionClase(); // se actualiza el estado de los labels e inputs que formen parte de la seccion de seleccion de estado
			}
		});
		chckbxClasificado.setForeground(SystemColor.textHighlightText);
		chckbxClasificado.setBackground(new Color(35, 47, 59));

		chckbxClasificado.setFont(new Font("Dialog", Font.BOLD, 16));
		chckbxClasificado.setBounds(122, 155, 184, 23);
		panel.add(chckbxClasificado);

		this.actualizarOpciones();

	}

	private void actualizarOpciones () {
		// si el algoritmo seleccionado fue Dbscan
		if (Controlador.getInstancie().getAlgoritmoK() instanceof Dbscan) {
			// No se mostrarán las opciones de clasificación al usuario
			this.lblClase.setVisible(false);
			this.textFieldClase.setVisible(false);
			this.chckbxClasificado.setVisible(false);
		}
		else
			this.actualizarEstadoSeccionClase(); // se actualiza el estado de los labels e inputs que formen parte de la seccion de seleccion de estado
	}

	private void actualizarEstadoSeccionClase () {
		if (this.chckbxClasificado.isSelected()) { // si el check está seleccionado
			// se habilita la seccion de la selección de clase
			this.lblClase.setEnabled(true);
			this.textFieldClase.setEnabled(true);
		}
		else { // si no está seleccionado el check
			// se ihabilita la sección de la selección de clase
			this.lblClase.setEnabled(false);
			this.textFieldClase.setEnabled(false);
		}
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
