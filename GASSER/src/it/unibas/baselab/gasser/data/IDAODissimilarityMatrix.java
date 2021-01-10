package it.unibas.baselab.gasser.data;

import it.unibas.jcc.data.TestSuite;

public interface IDAODissimilarityMatrix {
	
	public void write(DissimilarityMatrix dissimilarityMatrix, String filePath);
	
	public DissimilarityMatrix read(String filePath, TestSuite testSuite);

}
