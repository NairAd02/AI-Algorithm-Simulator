package utils;



import java.util.Random;

import logica.Color;

public class Colors {

	public static Color generarColorAleatorio () {
		Color color = null;
		Random rand = new Random(); // se crea una instancia de random
		// Genera un número aleatorio entre 1 y 3 (ambos inclusive)
		int numeroAleatorio = rand.nextInt(3) + 1;

		if (numeroAleatorio == 1) // se genera un color azulado
			color =	generarColorAzulado();

		else if (numeroAleatorio == 2) // se genera un color verdoso
			color =	generarColorVerdoso();
		else // se genera un color amarillento

			color = generarColorAmarillento();

		return color;
	}

	private static Color generarColorAzulado () {
		// La clase 'Color' en Java toma 3 valores de punto flotante, de 0 a 1.
		Random rand = new Random(); // se crea una instancia de random
		float r = rand.nextFloat() * 0.4f; // Rojo bajo
		float g = rand.nextFloat() * 0.4f; // Verde bajo
		float b = rand.nextFloat() * 0.8f + 0.2f; // Azul medio-alto


		// Crea el color final pasando los componentes a su constructor:
		Color randomColor = new Color(r, g, b);

		return randomColor;

	}

	private static Color generarColorVerdoso () {
		// La clase 'Color' en Java toma 3 valores de punto flotante, de 0 a 1.
		Random rand = new Random(); // se crea una instancia de random
		float r = rand.nextFloat() * 0.2f; // Rojo bajo
		float g = rand.nextFloat() * 0.8f + 0.2f; // Verde medio-alto
		float b = rand.nextFloat() * 0.2f; // Azul bajo


		// Crea el color final pasando los componentes a su constructor:
		Color randomColor = new Color(r, g, b);

		return randomColor;
	}

	private static Color generarColorAmarillento () {
		// La clase 'Color' en Java toma 3 valores de punto flotante, de 0 a 1.
		Random rand = new Random(); // se crea una instancia de random
		float r = rand.nextFloat() * 0.8f + 0.2f; // Rojo medio-alto
		float g = rand.nextFloat() * 0.8f + 0.2f; // Verde medio-alto
		float b = rand.nextFloat() * 0.2f; // Azul bajo


		// Crea el color final pasando los componentes a su constructor:
		Color randomColor = new Color(r, g, b);

		return randomColor;
	}
}
