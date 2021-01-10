package it.unibas.baselab.gasser.data;

import java.util.Arrays;
import java.util.List;

import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import it.unibas.jcc.data.TestCase;

@Root
public class DissimilarityMatrix {
	
	@ElementList
	private List<TestCase> header;
	
	@ElementArray
	private double[][] matrix;
		
	public DissimilarityMatrix() {
	}
	
	public DissimilarityMatrix(List<TestCase> header) {
		this.header = header;
		this.matrix = new double[header.size()][header.size()];
	}

	/**
	 * Returns an entry having the desired indexes.
	 * @param i The i-th index
	 * @param j The j-th index
	 * @return The entry
	 */
	public double getEntry(int i, int j) {
		return matrix[i][j]; 
	}
	
	/**
	 * Set the value of an entry having the desired indexes.
	 * @param i The i-th index
	 * @param j The j-th index
	 * @param value The value
	 */
	public void setEntry (int i, int j, double value) {
		matrix[i][j] = value; 
	}
	
	public double[][] getEntries() {
		return matrix; 
	}

	public int size() {
		return matrix[0].length;
	}
	
	public List<TestCase> getHeader() {
		return header;
	}

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder(header.toString());
		for (int i = 0; i < size(); i++) {
			builder.append(Arrays.toString(matrix[i]));
		}
		return builder.toString();
	}
	
}
