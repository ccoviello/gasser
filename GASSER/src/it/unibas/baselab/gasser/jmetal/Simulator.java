package it.unibas.baselab.gasser.jmetal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.unibas.baselab.gasser.HandlerFiles;
import it.unibas.baselab.gasser.data.DissimilarityMatrix;
import it.unibas.baselab.gasser.dissimilarity.IDissimilarityStrategy;
import it.unibas.jcc.data.TestCase;


public class Simulator {

	private Set<MySolution> computedSolution = new HashSet<>();
	
	private boolean computeDissimilarity;
	
	private IDissimilarityStrategy dissimilarityMeasureStrategy = null;
	private DissimilarityMatrix dissimilarityMatrix = null;
	
	
	public Simulator(boolean computeDissimilarity) {
		this.computeDissimilarity = computeDissimilarity;
	}
	
	public void setStrategy(IDissimilarityStrategy strategy) {
		this.dissimilarityMeasureStrategy = strategy;
	}
	
	public void setDissimilarityMatrix(DissimilarityMatrix dissimilarityMatrix) {
		this.dissimilarityMatrix = dissimilarityMatrix;
	}
	
	public MySolution simulate(MySolution mySolution){
		if(computeDissimilarity) {
			return simulateWithoutDissimilarityMatrix(mySolution);
		} else {
			return simulateWithDissimilarityMatrix(mySolution);
		}
	}
	
 	public MySolution simulateWithDissimilarityMatrix(MySolution mySolution) {
		List<TestCase> lstExecutedTestCase = new ArrayList<>();
		double codeCoverage = 0.0;
		double testCaseNumber = 0.0;
		double distance = 0.0;
		for (Map.Entry<String, Integer> entry : mySolution.getMapTestCase().entrySet()) {	
			if (entry.getValue() == 1) {
				TestCase testCaseIesimo = HandlerFiles.getIstance().getLstTestCase().get(Integer.parseInt(entry.getKey())-1);
				lstExecutedTestCase.add(testCaseIesimo);
				codeCoverage += (testCaseIesimo.getCoveredJavaLines().size() * (-1));
				testCaseNumber ++;
			}
		}
		if (lstExecutedTestCase.size() > 1) {
			for (int i=0; i <= lstExecutedTestCase.size()-1;i++) {
				for (int j=i+1; j < lstExecutedTestCase.size() ;j++)
					distance += dissimilarityMatrix.getEntry(i, j)*(-1);
				}
		}
		mySolution.setCodeCoverage((int) codeCoverage);
		mySolution.setTestCaseNumber((int) testCaseNumber);
		mySolution.setDistance(distance);
		this.computedSolution.add(mySolution);
		return mySolution;
	}
	
 	public MySolution simulateWithoutDissimilarityMatrix(MySolution mySolution) {
		List<TestCase> lstExecutedTestCase = new ArrayList<>();
		double codeCoverage = 0.0;
		double testCaseNumber = 0.0;
		double distance = 0.0;
		for (Map.Entry<String, Integer> entry : mySolution.getMapTestCase().entrySet()) {	
			if (entry.getValue() == 1) {
				TestCase testCaseIesimo = HandlerFiles.getIstance().getLstTestCase().get(Integer.parseInt(entry.getKey())-1);
				lstExecutedTestCase.add(testCaseIesimo);
				codeCoverage += (testCaseIesimo.getCoveredJavaLines().size() * (-1));
				testCaseNumber ++;
			}
		}
		if (lstExecutedTestCase.size() > 1) {
			for (int i=0; i <= lstExecutedTestCase.size()-1;i++) {
				for (int j=i+1; j < lstExecutedTestCase.size() ;j++)
				distance += (dissimilarityMeasureStrategy.computesDissimilarity(lstExecutedTestCase.get(i), lstExecutedTestCase.get(j))*(-1));
			}
		
		}
		mySolution.setCodeCoverage((int) codeCoverage);
		mySolution.setTestCaseNumber((int) testCaseNumber);
		mySolution.setDistance(distance);
		this.computedSolution.add(mySolution);
		return mySolution;
	}
	
	public Set<MySolution> getComputedSolution() {
		return this.computedSolution;
	}
}
