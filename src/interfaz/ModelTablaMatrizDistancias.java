package interfaz;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.table.DefaultTableModel;

import cu.edu.cujae.ceis.graph.edge.WeightedEdge;
import cu.edu.cujae.ceis.graph.vertex.Vertex;
import logica.Elemento;


public class ModelTablaMatrizDistancias extends DefaultTableModel {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public ModelTablaMatrizDistancias(LinkedList<Vertex> vertices){ // se recibe como parametro los vertices del grafo
	
		String[] columnNames = new String[vertices.size() + 1]; 
		// se defiden las columnas de la tabla
		columnNames[0] = "Vertices"; // se añade una columna para los vertices
		int cant = 1;
		for (Vertex vertex : vertices) {
			Elemento elemento = (Elemento) vertex.getInfo();
			columnNames[cant++] = elemento.getId(); 
		}


		this.setColumnIdentifiers(columnNames);
		this.isCellEditable(getRowCount(), getColumnCount());


	}


	public void addElement(Elemento elementoFila, ArrayList<Double> filaMatrizDistancias) {
		Object[] newRow =  new Object[filaMatrizDistancias.size() + 1];
		newRow[0] = elementoFila.getId(); // se añade el encabezado de la fila
		int cant = 1;
		for (Double i : filaMatrizDistancias) {
			newRow[cant++] = String.format("%.2f", i);
		}
		addRow(newRow);

	}
	
	public boolean isCellEditable(int row, int column){
		boolean x=false;
		
		
		return x;
			
		
	}
}
