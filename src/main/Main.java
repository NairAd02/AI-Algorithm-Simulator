package main;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import interfaz.FramePrincipal;
import logica.Clase;
import logica.Controlador;
import logica.Elemento;


public class Main {
	public static void main(String[] args){
		// Implementar Carga con ficheros CSSV reales
		/*Controlador controlador = Controlador.getInstancie();
		// Se crea el fichero csv de los vertices no clasificados
		ArrayList<Elemento> elementosNoClasificados = new ArrayList<Elemento>();
		elementosNoClasificados.add(new Elemento("Elemento de Prueba 1", 100, 100));
		elementosNoClasificados.add(new Elemento("Elemento de Prueba 2", 200, 200));
		try {
			controlador.crearFicheroCsvElementos(elementosNoClasificados, "datos no clasificados.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Se crea el fichero csv de los vertices clasificados
		ArrayList<Elemento> elementosClasificados = new ArrayList<Elemento>();
		elementosClasificados.add(new Elemento("Elemento Class de Prueba 1", 500, 100, new Clase("Cuadrado")));
		elementosClasificados.add(new Elemento("Elemento Class de Prueba 2", 500, 400, new Clase("Rombo")));
		try {
			controlador.crearFicheroCsvElementos(elementosClasificados, "datos clasificados.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		// Habilitar lookAndFeel
		JFrame.setDefaultLookAndFeelDecorated(true);

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e1) {

			e1.printStackTrace();
		} catch (InstantiationException e1) {

			e1.printStackTrace();
		} catch (IllegalAccessException e1) {  

			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {

			e1.printStackTrace();
		}



		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FramePrincipal frame = FramePrincipal.getInstancie();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});


	}
}
