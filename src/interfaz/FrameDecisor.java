package interfaz;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import utils.FuncionalInterface;


public class FrameDecisor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFrame frame;
	private JLabel lblX;
	private JLabel lblYes;
	private JTextPane textPaneMensaje;
	private int mouseX, mouseY;
	private FuncionalInterface funcion; // respresenta la funcion que debe de ejecutarse si se confirma la operación
	private JLabel lblNo;

	public FrameDecisor(JFrame f,String mensaje, FuncionalInterface fi) {
		this.frame = f;
		this.funcion = fi;
		setUndecorated(true);

		setSize(422, 201);
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
		setLocationRelativeTo(frame);
		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(4, 4, 4, 4, (Color) new Color(0, 0, 0)));
		panel.setLayout(null);
		panel.setBackground(new Color(35, 47, 59));
		panel.setBounds(0, 0, 422, 201);
		contentPane.add(panel);

		lblYes = new JLabel("Si");
		lblYes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblYes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// se ejecuta la funcion programada
				funcion.ejecutar();
				cerrarFrame(); // se cierra el frame actual
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblYes.setBackground(SystemColor.activeCaptionBorder);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblYes.setBackground(SystemColor.WHITE);
			}
		});
		lblYes.setOpaque(true);
		lblYes.setHorizontalAlignment(SwingConstants.CENTER);
		lblYes.setFont(new Font("Arial Black", Font.PLAIN, 18));
		lblYes.setBackground(SystemColor.WHITE);
		lblYes.setBounds(27, 155, 138, 35);
		panel.add(lblYes);

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
		lblX.setBackground(SystemColor.menu);
		lblX.setBounds(384, 0, 38, 38);
		panel.add(lblX);

		textPaneMensaje = new JTextPane();
		textPaneMensaje.setText(mensaje);
		textPaneMensaje.setForeground(SystemColor.textHighlightText);
		textPaneMensaje.setFont(new Font("Dialog", Font.PLAIN, 21));
		textPaneMensaje.setEditable(false);
		textPaneMensaje.setBackground(new Color(35, 47, 59));
		textPaneMensaje.setBounds(56, 33, 324, 102);
		panel.add(textPaneMensaje);
		
		lblNo = new JLabel("No");
		lblNo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblNo.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				cerrarFrame(); // se cierra el frame actual
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblNo.setBackground(SystemColor.activeCaptionBorder);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblNo.setBackground(SystemColor.WHITE);
			}
		});
		lblNo.setOpaque(true);
		lblNo.setHorizontalAlignment(SwingConstants.CENTER);
		lblNo.setFont(new Font("Arial Black", Font.PLAIN, 18));
		lblNo.setBackground(SystemColor.WHITE);
		lblNo.setBounds(261, 155, 138, 35);
		panel.add(lblNo);
	}

	private void cerrarFrame () {
		frame.setEnabled(true);
		dispose();
	}
}
