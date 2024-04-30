package utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

public class ManejoImagen {
	public static void generarImagen (JPanel panel, String ruta) {
		// Se crea un buffer para almacenar la imagen
		BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = image.createGraphics();

		panel.paint(g2d); // se dibuja la imagen apartir del contenido del panel

		g2d.dispose();

		try {
			ManejoDirectorios.guardarImagen(image, ruta); // se guarda la imagen
		} catch (IOException e1) {

			e1.printStackTrace();
		}  	
	}
}
