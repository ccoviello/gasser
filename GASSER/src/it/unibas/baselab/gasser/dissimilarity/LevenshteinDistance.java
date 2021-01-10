package it.unibas.baselab.gasser.dissimilarity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibas.jcc.data.JavaLine;
import it.unibas.jcc.data.TestCase;

public class LevenshteinDistance implements IDissimilarityStrategy{

	private Map<TestCase, List<JavaLine>> coveredLineCache = new HashMap<TestCase, List<JavaLine>>();

	@Override
	public double computesDissimilarity(TestCase testCase1, TestCase testCase2) {
		List<JavaLine> coveredLines1 = coveredLineCache.get(testCase1);
		List<JavaLine> coveredLines2 = coveredLineCache.get(testCase2);
				
		if (coveredLines1 == null) {
        	coveredLines1 = testCase1.getCoveredJavaLines();
        	Collections.sort(coveredLines1, new JavaLineComparator());
        	coveredLineCache.put(testCase1, coveredLines1);
		}
		
		if (coveredLines2 == null) {
        	coveredLines2 = testCase2.getCoveredJavaLines();
        	Collections.sort(coveredLines2, new JavaLineComparator());
        	coveredLineCache.put(testCase2, coveredLines2);
		}
		
		Encoder encoder = new Encoder();
		String encoding1 = getEncodingOf(coveredLines1, encoder);
		String encoding2 = getEncodingOf(coveredLines2, encoder);
		Double result = Levenshtein.getIstance().distance(encoding1, encoding2);
		return result;
	}

	
	private String getEncodingOf(List<JavaLine> javaLines, Encoder encoder){
		StringBuilder buffer = new StringBuilder();
		for (JavaLine javaLine : javaLines) {
			buffer.append(encoder.getEncodingOf(javaLine));
		}		
		return buffer.toString();
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
