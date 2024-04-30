package main;

import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import interfaz.FramePrincipal;
import logica.Controlador;


public class Main {
	public static void main(String[] args){
		Controlador controlador = Controlador.getInstancie(3);
		//System.out.print(controlador.grafoKNN.getVerticesList());

		/*Elemento elemento = new Elemento("IdL", "InfoL", 6, 0, 100, 100);
		Elemento elemento2 = new Elemento("IdM", "InfoM", 4, 5, 100, 100);
		Elemento elemento3 = new Elemento("IdN", "InfoN", 5, 6, 100, 100);

		//controlador.DijkstraShortestPath(grafoKNN.getVerticesList().getLast(), grafoKNN.getVerticesList().getFirst(), (LinkedGraph) grafoKNN);

		System.out.println("matriz de distancias: "+controlador.obtenerMatrizDistancias());
		controlador.algoritmo(elemento);
		controlador.algoritmo(elemento2);
		controlador.algoritmo(elemento3);*/
		
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
