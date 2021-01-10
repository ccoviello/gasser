package it.unibas.baselab.gasser.dissimilarity;

import it.unibas.jcc.data.TestCase;

public interface IDissimilarityStrategy {
	
	/**
	 * Returns a dissimilarity value between two test cases. 
	 * @param testCase1 First test case
	 * @param testCase2 Second test case
	 * @return The dissimilarity value
	 */
	public double computesDissimilarity(TestCase testCase1, TestCase testCase2);
	
	public String getName();

	public void close();


}
