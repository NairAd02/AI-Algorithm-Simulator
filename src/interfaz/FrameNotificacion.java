package interfaz;


import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.border.MatteBorder;
import java.awt.SystemColor;

public class FrameNotificacion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	boolean tiempoTranscurrido = false;
	Timer timer;
	private JTextPane textPane;

	public FrameNotificacion(String mensaje) {
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(mensaje);
		setBounds(100, 100, 408, 90);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(35, 47, 59));
		contentPane.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		/*JPanel panel = new JPanel();
		panel.setBackground(getForeground());*/

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(FrameNotificacion.class.getResource("/img/Chat Message.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 79, 68);
		contentPane.add(lblNewLabel);
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setForeground(SystemColor.textHighlightText);
		textPane.setFont(new Font("Dialog", Font.PLAIN, 14));
		textPane.setText(mensaje);
		textPane.setBackground(new Color(35, 47, 59));
		textPane.setBounds(99, 22, 299, 46);
		contentPane.add(textPane);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle bounds = ge.getMaximumWindowBounds();
		setLocation(bounds.width - getWidth() - 10, (bounds.height - getHeight())- 10);

		setOpacity(1);


		timer = new Timer(100, new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {


				float opacity = getOpacity();
				if(opacity > 0){
					opacity -= 0.1;
					if(opacity < 0){
						opacity = 0;
					}
					setOpacity(opacity);
				} else{
					dispose();
					((Timer)e.getSource()).stop();
				}	
			}
		});
		timer.setInitialDelay(3000);
		timer.start();
	}
}
