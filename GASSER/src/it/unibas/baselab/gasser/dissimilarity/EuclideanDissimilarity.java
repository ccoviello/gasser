package it.unibas.baselab.gasser.dissimilarity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unibas.jcc.data.JavaLine;
import it.unibas.jcc.data.TestCase;
import it.unibas.jcc.data.TestSuite;

public class EuclideanDissimilarity implements IDissimilarityStrategy {

	public EuclideanDissimilarity() {}
	
	public EuclideanDissimilarity(TestSuite testSuite) {
		Set<JavaLine> coveredLines = new HashSet<JavaLine>();
		for (TestCase t : testSuite.getTestCases()) {
			coveredLines.addAll(t.getCoveredJavaLines());
		}
		List<JavaLine> allCoveredLines = new ArrayList<JavaLine>(coveredLines);
		List<TestCase> testCases = testSuite.getTestCases();
		int[] binaryString;
		
		for (TestCase test : testCases) {
			if (test.getBinaryString() == null) {
				binaryString = new int[allCoveredLines.size()];
				for (int i = 0; i < binaryString.length; i++) {
						if (test.getCoveredJavaLines().contains(allCoveredLines.get(i))) {
							binaryString[i] = 1;
						} else {
							binaryString[i] = 0;
						}
				}
				test.setBinaryString(binaryString);
			}
		}

	}

	@Override
	public double computesDissimilarity(TestCase testCase1, TestCase testCase2) {

		int[] binaryStringt1 = testCase1.getBinaryString();
		int[] binaryStringt2 = testCase2.getBinaryString();
		Double sum = 0.0;
		for (int i = 0; i < binaryStringt1.length; i++) {
			sum = sum + (binaryStringt2[i] - binaryStringt1[i]) * (binaryStringt2[i] - binaryStringt1[i]);
		}
		return Math.sqrt(sum);
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		return;
	}

}
