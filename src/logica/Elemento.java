package logica;

import java.io.Serializable;

public class Elemento implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private float x;
	private float y;
	// dimensiones del nodo (son necesarias para trazar las aristas justo en el centro de los nodos)
	private double largo;
	private double ancho;
	private Clase clase;
	private boolean isClasificado; // atributo que indica si un vertice fue creado como clasificado o no (solo a este vertice es posible cambiarle la clasificación)


	public Elemento(String id, float x, float y){
		this.id = id;
		this.x = x;
		this.y = y;
		this.clase = null;
		this.isClasificado = false;
	}

	public Elemento(String id, float x, float y, Clase clase){
		this.id = id;
		this.x = x;
		this.y = y;
		this.clase = clase;
		this.isClasificado = true; // se marca como clasificado
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

	public void setClase(Clase clase){
		this.clase = clase;
	}

	public Clase getClase(){
		return clase;
	}

	public String toString() {
		return this.id;
	}

	public boolean isClasificado() {
		return isClasificado;
	}

	public void setClasificado(boolean isClasificado) {
		this.isClasificado = isClasificado;
	}
	
}
