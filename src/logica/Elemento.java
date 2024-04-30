package logica;

import java.io.Serializable;

public class Elemento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Object info;
	private float x;
	private float y;
	// dimensiones del nodo (son necesarias para trazar las aristas justo en el centro de los nodos)
	private double largo;
	private double ancho;
	private String clase;

	public Elemento(String id, Object info, float x, float y, double largo, double ancho){
		this.id = id;
		this.info = info;
		this.x = x;
		this.y = y;
		this.largo = largo;
		this.ancho = ancho;
		clase = null;
	}
	
	public Elemento(String id, Object info, float x, float y){
		this.id = id;
		this.info = info;
		this.x = x;
		this.y = y;
		clase = null;
	}

	
	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}
	
	public double getLargo() {
		return largo;
	}

	public void setLargo(double largo) {
		this.largo = largo;
	}

	public double getAncho() {
		return ancho;
	}

	public void setAncho(double ancho) {
		this.ancho = ancho;
	}

	public void setInfo(Object info){
		this.info = info;
	}

	public Object getInfo(){
		return info;
	}

	public void setX(float x){
		this.x = x;
	}

	public float getX(){
		return this.x;
	}

	public void setY(float y){
		this.y = y;
	}

	public float getY(){
		return this.y;
	}

	public void setClase(String clase){
		this.clase = clase;
	}

	public String getClase(){
		return clase;
	}

	public String toString() {
		return this.id;
	}
}
