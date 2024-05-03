package interfaz;

import javax.swing.JPanel;

import logica.Clase;

import java.awt.Color;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.FlowLayout;

public class ParLeyenda extends JPanel {

	private static final long serialVersionUID = 1L;
	private Clase clase;
	private JLabel lblClase;
	private JPanel panelColor;
	/**
	 * Create the panel.
	 */
	public ParLeyenda(Clase c) {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		setBackground(SystemColor.textHighlightText);
		this.clase = c;

		panelColor = new JPanel();
		panelColor.setBackground(new Color(clase.getColor().getRed(), clase.getColor().getGreen(), clase.getColor().getBlue()));
		add(panelColor);
		lblClase = new JLabel(this.clase.getNombre());
		lblClase.setFont(new Font("Dialog", Font.BOLD, 18));
		add(lblClase);


	}

}
