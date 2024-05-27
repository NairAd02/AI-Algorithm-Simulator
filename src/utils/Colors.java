package utils;




import java.util.Random;
import logica.Color;
import logica.Controlador;

public class Colors {
	private static Colors colors;

	private Colors () {
		
	}

	public static Colors getInstancie () {
		if (colors == null)
			colors = new Colors();

		return colors;
	}

	public  Color generarColorAleatorio () {

		Color color = null;
		Random rand = new Random(); // se crea una instancia de random
		do {
			// Genera un número aleatorio entre 1 y 3 (ambos inclusive)
			int numeroAleatorio = rand.nextInt(3) + 1;

			if (numeroAleatorio == 1) // se genera un color azulado
				color =	generarColorAzulado();

			else if (numeroAleatorio == 2) // se genera un color verdoso
				color =	generarColorVerdoso();
			else // se genera un color amarillento

				color = generarColorAmarillento();
			System.out.println(color);
		}while (Controlador.getInstancie().getAlgoritmoK().isSimilarColorClase(color));


		return color;
	}


	private  Color generarColorAzulado () {
		// La clase 'Color' en Java toma 3 valores de punto flotante, de 0 a 1.
		Random rand = new Random(); // se crea una instancia de random
		float r = rand.nextFloat() * 0.4f; // Rojo bajo
		float g = rand.nextFloat() * 0.4f; // Verde bajo
		float b = rand.nextFloat() * 0.8f + 0.2f; // Azul medio-alto


		// Crea el color final pasando los componentes a su constructor:
		Color randomColor = new Color(r, g, b);

		return randomColor;

	}

	private  Color generarColorVerdoso () {
		// La clase 'Color' en Java toma 3 valores de punto flotante, de 0 a 1.
		Random rand = new Random(); // se crea una instancia de random
		float r = rand.nextFloat() * 0.2f; // Rojo bajo
		float g = rand.nextFloat() * 0.8f + 0.2f; // Verde medio-alto
		float b = rand.nextFloat() * 0.2f; // Azul bajo


		// Crea el color final pasando los componentes a su constructor:
		Color randomColor = new Color(r, g, b);

		return randomColor;
	}

	private  Color generarColorAmarillento () {
		// La clase 'Color' en Java toma 3 valores de punto flotante, de 0 a 1.
		Random rand = new Random(); // se crea una instancia de random
		float r = rand.nextFloat() * 0.8f + 0.2f; // Rojo medio-alto
		float g = rand.nextFloat() * 0.8f + 0.2f; // Verde medio-alto
		float b = rand.nextFloat() * 0.2f; // Azul bajo


		// Crea el color final pasando los componentes a su constructor:
		Color randomColor = new Color(r, g, b);

		return randomColor;
	}
	
	public boolean isSimilarColor(Color color1, Color color2) {
	    // Establece el umbral. Si la distancia es menor que este valor, los colores son similares.
	    double threshold = 0.10;

	    float red1 = color1.getRed();
	    float green1 = color1.getGreen();
	    float blue1 = color1.getBlue();

	    float red2 = color2.getRed();
	    float green2 = color2.getGreen();
	    float blue2 = color2.getBlue();

	    // Calcula la distancia euclidiana en el espacio de color RGB.
	    double distance = Math.sqrt(Math.pow(red2 - red1, 2) + Math.pow(green2 - green1, 2) + Math.pow(blue2 - blue1, 2));

	    // Si la distancia es menor que el umbral, los colores son similares.
	    return distance < threshold;
	}
}
