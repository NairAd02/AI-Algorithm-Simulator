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



public class FrameAddVertice extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblX;
	private JLabel lblConfirm;
	private int mouseX, mouseY;
	private JLabel lblNewLabel;
	private JTextField textFieldValorNode;




	public FrameAddVertice() {

		setUndecorated(true);

		setSize(442, 245);
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
		panel.setBounds(0, 0, 442, 245);
		contentPane.add(panel);

		lblConfirm = new JLabel("Confirmar");
		lblConfirm.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		lblConfirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblConfirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				FramePrincipal.getInstancie().setnombreCiudad(textFieldValorNode.getText()); // se almacena el nombre que se le va a dar a la ciudad
				FramePrincipal.getInstancie().setInserctionVertex(true); // se habilita la isercción de vertices
				cerrarFrame();
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
		lblConfirm.setBounds(152, 199, 138, 35);
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

		lblNewLabel = new JLabel("Introduzca Nombre del Vertice:");
		lblNewLabel.setBackground(Color.BLACK);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(SystemColor.textHighlightText);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 26));
		lblNewLabel.setBounds(21, 68, 400, 28);
		panel.add(lblNewLabel);

		textFieldValorNode = new JTextField();
		textFieldValorNode.setFont(new Font("Dialog", Font.PLAIN, 16));
		textFieldValorNode.setBounds(28, 119, 382, 35);
		panel.add(textFieldValorNode);
		textFieldValorNode.setColumns(10);

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
