package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableColumn;

import logica.Clase;
import logica.Controlador;
import logica.Elemento;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class FrameMatrizDistancias extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblNewLabel;
	private JPanel panelCentral;
	private JLabel lblX;
	private int mouseX, mouseY;
	private JLabel label;
	private JPanel panelMatrizDistancias;
	private JScrollPane scrollPane;
	private JTable tableMatrizDistancias;


	public FrameMatrizDistancias() {
		setUndecorated(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1010, 637);
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
		
		lblNewLabel = new JLabel("Matriz de Distancias");
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
		
		panelCentral = new JPanel();
		panelCentral.setBackground(SystemColor.textHighlightText);
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new BorderLayout(0, 0));
		
		panelMatrizDistancias = new JPanel();
		panelCentral.add(panelMatrizDistancias, BorderLayout.CENTER);
		panelMatrizDistancias.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		panelMatrizDistancias.add(scrollPane, BorderLayout.CENTER);
		
		tableMatrizDistancias = new JTable();
		tableMatrizDistancias.setModel(new ModelTablaMatrizDistancias(Controlador.getInstancie().getGrafo().getVerticesList())); // se le añade el modelo a la tabla
		tableMatrizDistancias.setRowHeight(30);
		tableMatrizDistancias.setFont(new Font("Tahoma", Font.PLAIN, 18));
		tableMatrizDistancias.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
		tableMatrizDistancias.getTableHeader().setForeground(Color.black);
		tableMatrizDistancias.getTableHeader().setBackground(SystemColor.black);
		Font newFont = new Font("Arial", Font.BOLD, 18);
		TableColumn column = tableMatrizDistancias.getColumnModel().getColumn(0); 
		column.setCellRenderer(new CustomFontRenderer(newFont));
		scrollPane.setViewportView(tableMatrizDistancias);
		
	this.actualizarMatrizDistancias(); // se actualiza la info de la matriz de distancias del grafo
		
		
	}
	
	public void actualizarMatrizDistancias () {
	     ArrayList<ArrayList<Double>> matrizDistancias = Controlador.getInstancie().getGrafo().obtenerMatrizDistancias(); // se obtiene la matriz de distancias del grafo
	     int count = 0;
	     for (ArrayList<Double> fila : matrizDistancias) {
	    	 Elemento elementoFila = (Elemento) Controlador.getInstancie().getGrafo().getVertexPos(count++).getInfo();
			((ModelTablaMatrizDistancias)this.tableMatrizDistancias.getModel()).addElement(elementoFila, fila);
		}
	}
	
	public void cerrarFrame () {
		FramePrincipal.getInstancie().setEnabled(true); // se habilita el frame principal
		dispose();
	}
}
