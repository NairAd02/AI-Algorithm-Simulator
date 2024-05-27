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
import logica.AlgoritmoK;
import logica.Controlador;
import logica.Dbscan;
import logica.Knn;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;

public class FrameCrearGrafo extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblX;
	private JLabel lblConfirm;
	private int mouseX, mouseY;
	private JLabel lblNewLabel;
	private JSpinner spinnerValorK;
	private JComboBox<String> comboBoxTipoAlgorimo;
	private JLabel lblSeleccioneElRadio;
	private JSpinner spinnerValorRadio;
	private JLabel lblSeleccioneElMinimo;
	private JSpinner spinnerValorMinimoNumero;
	private JLabel lblValorK;




	public FrameCrearGrafo() {

		setUndecorated(true);

		setSize(516, 501);
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
		panel.setBounds(0, 0, 516, 501);
		contentPane.add(panel);

		lblConfirm = new JLabel("Confirmar");
		lblConfirm.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		lblConfirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblConfirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// En dependencia de la opcion elegida se decide que tipo de algoritmo se va a crear
				AlgoritmoK algoritmoK = null;
				if (comboBoxTipoAlgorimo.getSelectedIndex() == 0) // se seleccionó la opción de KNN
					algoritmoK = new Knn((int) spinnerValorK.getValue());
				else // se seleccionó la opción de DBSCAN
					algoritmoK = new Dbscan((float) spinnerValorRadio.getValue(), (int) spinnerValorMinimoNumero.getValue());
				
				Controlador.getInstancie().setAlgoritmoK(algoritmoK); // se crea el proyecto con el algoritmo seleccionado
				FramePrincipal.getInstancie().actualizarEstadoBotones(); // se actualiza el estado de los botones del frame principal
				FramePrincipal.getInstancie().actualizarLienzo(); // se actualiza la info del lienzo
				FramePrincipal.getInstancie().actualizarMenuOpciones(); // se actualiza el menu de las opciones
				FramePrincipal.lanzarNotificacion("Se ha creado con exito el grafo");
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
		lblConfirm.setBounds(189, 455, 138, 35);
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
		lblX.setBounds(478, 0, 38, 38);
		panel.add(lblX);

		lblNewLabel = new JLabel("Crear nuevo Proyecto:");
		lblNewLabel.setBackground(Color.BLACK);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setForeground(SystemColor.textHighlightText);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 26));
		lblNewLabel.setBounds(10, 40, 303, 28);
		panel.add(lblNewLabel);

		lblValorK = new JLabel("Seleccione Valor de K:");
		lblValorK.setHorizontalAlignment(SwingConstants.LEFT);
		lblValorK.setForeground(SystemColor.textHighlightText);
		lblValorK.setFont(new Font("Dialog", Font.BOLD, 20));
		lblValorK.setBackground(Color.BLACK);
		lblValorK.setBounds(144, 184, 227, 28);
		panel.add(lblValorK);

		spinnerValorK = new JSpinner();
		spinnerValorK.setModel(new SpinnerNumberModel(1, 1, 21, 2));
		spinnerValorK.setFont(new Font("Dialog", Font.PLAIN, 20));
		spinnerValorK.setBounds(214, 223, 88, 28);

		panel.add(spinnerValorK);

		JLabel lblSeleccioneElAlgoritmo = new JLabel("Seleccione el algoritmo a utilizar:");
		lblSeleccioneElAlgoritmo.setHorizontalAlignment(SwingConstants.LEFT);
		lblSeleccioneElAlgoritmo.setForeground(SystemColor.textHighlightText);
		lblSeleccioneElAlgoritmo.setFont(new Font("Dialog", Font.BOLD, 20));
		lblSeleccioneElAlgoritmo.setBackground(Color.BLACK);
		lblSeleccioneElAlgoritmo.setBounds(98, 79, 319, 28);
		panel.add(lblSeleccioneElAlgoritmo);

		comboBoxTipoAlgorimo = new JComboBox<String>();
		comboBoxTipoAlgorimo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarCampos(); // se actualizan los campos a llenar por el usuario
			}
		});
		comboBoxTipoAlgorimo.setFont(new Font("Dialog", Font.PLAIN, 20));
		comboBoxTipoAlgorimo.setBounds(98, 118, 319, 28);
		panel.add(comboBoxTipoAlgorimo);

		lblSeleccioneElRadio = new JLabel("Seleccione el Radio de la Circunferencia:");
		lblSeleccioneElRadio.setHorizontalAlignment(SwingConstants.LEFT);
		lblSeleccioneElRadio.setForeground(SystemColor.textHighlightText);
		lblSeleccioneElRadio.setFont(new Font("Dialog", Font.BOLD, 20));
		lblSeleccioneElRadio.setBackground(Color.BLACK);
		lblSeleccioneElRadio.setBounds(57, 262, 402, 28);
		panel.add(lblSeleccioneElRadio);

		spinnerValorRadio = new JSpinner();
		spinnerValorRadio.setModel(new SpinnerNumberModel(Float.valueOf(1), Float.valueOf(1), null, Float.valueOf(1)));
		spinnerValorRadio.setFont(new Font("Dialog", Font.PLAIN, 20));
		spinnerValorRadio.setBounds(214, 301, 88, 28);
		panel.add(spinnerValorRadio);

		lblSeleccioneElMinimo = new JLabel("Seleccione el minimo numeros de puntos:");
		lblSeleccioneElMinimo.setHorizontalAlignment(SwingConstants.LEFT);
		lblSeleccioneElMinimo.setForeground(SystemColor.textHighlightText);
		lblSeleccioneElMinimo.setFont(new Font("Dialog", Font.BOLD, 20));
		lblSeleccioneElMinimo.setBackground(Color.BLACK);
		lblSeleccioneElMinimo.setBounds(57, 342, 402, 28);
		panel.add(lblSeleccioneElMinimo);

		spinnerValorMinimoNumero = new JSpinner();
		spinnerValorMinimoNumero.setModel(new SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
		spinnerValorMinimoNumero.setFont(new Font("Dialog", Font.PLAIN, 20));
		spinnerValorMinimoNumero.setBounds(214, 381, 88, 28);
		panel.add(spinnerValorMinimoNumero);

		this.llenarComboBoxAlgoritmos();

	}

	private void actualizarCampos () {
		if (this.comboBoxTipoAlgorimo.getSelectedIndex() == 0) {  // se seleccionó el algoritmo KNN
			this.lblValorK.setVisible(true);
			this.spinnerValorK.setVisible(true);
			this.lblSeleccioneElMinimo.setVisible(false);
			this.lblSeleccioneElRadio.setVisible(false);
			this.spinnerValorMinimoNumero.setVisible(false);
			this.spinnerValorRadio.setVisible(false);
		}
		else { // se seleccionó DBSCAN
			this.lblValorK.setVisible(false);
			this.spinnerValorK.setVisible(false);
			this.lblSeleccioneElMinimo.setVisible(true);
			this.lblSeleccioneElRadio.setVisible(true);
			this.spinnerValorMinimoNumero.setVisible(true);
			this.spinnerValorRadio.setVisible(true);
		}
	}

	private void llenarComboBoxAlgoritmos () {
		this.comboBoxTipoAlgorimo.addItem("KNN");
		this.comboBoxTipoAlgorimo.addItem("DBSCAN");
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
