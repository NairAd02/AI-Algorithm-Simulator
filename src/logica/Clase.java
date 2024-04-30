package logica;

import java.awt.Color;
import utils.Colors;

public class Clase {
	private String nombre;
	private Color color; // atributo que representa el color de la clase

	public Clase (String nombre) {
		this.nombre = nombre;
		this.color = Colors.generarColorAleatorio(); // se genera un color aleatorio para la clase
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
