package it.unibas.baselab.gasser.dissimilarity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import it.unibas.jcc.data.JavaLine;
import it.unibas.jcc.data.TestCase;
import it.unibas.jcc.data.TestSuite;

public class KIndexDissimilarity implements IDissimilarityStrategy {

	private List<TestCase> testCases = new ArrayList<TestCase>();
	private static double pExpected = 0.0;
	
	public KIndexDissimilarity() {}
	
	public KIndexDissimilarity(TestSuite testSuite) {
		Set<JavaLine> coveredLines = new HashSet<JavaLine>();
		for (TestCase t : testSuite.getTestCases()) {
			coveredLines.addAll(t.getCoveredJavaLines());
		}
		List<JavaLine> allCoveredLines = new ArrayList<JavaLine>(coveredLines);
		testCases = testSuite.getTestCases();
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
		computePExpected();
	}

	private void computePExpected() {
		int[] totalString = testCases.get(0).getBinaryString();
		for(int i = 1; i < testCases.size(); i++) {
			int[] both = ArrayUtils.addAll(totalString, testCases.get(i).getBinaryString());
			totalString = both;
		}
		int ones = 0;
		int zeros = 0;
		for(int i = 0; i < totalString.length; i++) {
			if(totalString[i] == 0) {
				zeros++;
			} else {
				ones++;
			}
		}
		
		double pOne = (double)ones/totalString.length;
		double pZero = (double)zeros/totalString.length;
		pExpected = Math.pow(pOne, 2) + Math.pow(pZero, 2);
	}

	@Override
	public double computesDissimilarity(TestCase testCase1, TestCase testCase2) {

		int[] binaryStringt1 = testCase1.getBinaryString();
		int[] binaryStringt2 = testCase2.getBinaryString();

		return computesKIndex(binaryStringt1, binaryStringt2);

	}

	private double computesKIndex(int[] firstVec, int[] secondVec) {
		double kIndex = 0.0;
		double sum = 0.0;
		for (int i = 0; i < firstVec.length; i++) {
			if (firstVec[i] == (secondVec[i]))
				sum++;
		}
		
		double pObserved = sum/firstVec.length;
		
		kIndex = (pObserved - pExpected)/(1-pExpected);
		return 1- kIndex;
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

