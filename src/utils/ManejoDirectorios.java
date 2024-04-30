package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;



public class ManejoDirectorios {

	public static void guardarArchivo(Object objeto, String ruta) throws FileNotFoundException, IOException{
		
		ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(ruta));
		stream.writeObject(objeto);
		stream.close();
				
	}

	public static Object recuperarArchivo(String ruta) throws FileNotFoundException, IOException, ClassNotFoundException{
		Object objetoArecuperar = null;
		
		ObjectInputStream stream = new ObjectInputStream(new FileInputStream(ruta));
		objetoArecuperar = stream.readObject();
		stream.close();

		return objetoArecuperar;
	}
	
	
	
	public static boolean isSameName(String name){
	       File rutaSalvas = new File("Salvas Diagrama");
	       boolean issameName = false;
			
			String[] list = rutaSalvas.list();
			
			for (String s : list) {
				if(s.equals(name))
					issameName = true;
			}
			
			return issameName;
		}
	
	public static String[] obtenerNombreProyectos(){
	       File rutaSalvas = new File("Salvas Diagrama");
	       
			
			String[] list = rutaSalvas.list();
					
			return list;
		}
	

public static void guardarImagen (BufferedImage imagen, String ruta) throws IOException {
		
		ImageIO.write(imagen, "png",new File(ruta + ".png"));
	}
}
