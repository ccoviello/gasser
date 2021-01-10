package it.unibas.baselab.gasser.dissimilarity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unibas.jcc.data.JavaLine;
import it.unibas.jcc.data.TestCase;
import it.unibas.jcc.data.TestSuite;
import org.apache.commons.collections.CollectionUtils;;

public class JaccardDistanceDissimilarity implements IDissimilarityStrategy {
	
	public JaccardDistanceDissimilarity() {}
	
	public JaccardDistanceDissimilarity(TestSuite testSuite) {
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

	@SuppressWarnings("unchecked")
	@Override
	public double computesDissimilarity(TestCase testCase1, TestCase testCase2) {

		Collection<JavaLine> inter = CollectionUtils.intersection(testCase1.getCoveredJavaLines(), testCase2.getCoveredJavaLines());
		Collection<JavaLine> union = CollectionUtils.union(testCase1.getCoveredJavaLines(), testCase2.getCoveredJavaLines());
		double jaccardIndex = (double) inter.size()/union.size();
		return 1 - jaccardIndex;

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
