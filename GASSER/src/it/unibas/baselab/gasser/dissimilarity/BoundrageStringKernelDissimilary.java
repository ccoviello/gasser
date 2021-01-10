package it.unibas.baselab.gasser.dissimilarity;

import java.io.Closeable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

import it.unibas.jcc.data.JavaLine;
import it.unibas.jcc.data.TestCase;

public class BoundrageStringKernelDissimilary implements IDissimilarityStrategy, Closeable{
	
	private Rengine rengine;	
	private Map<TestCase, List<JavaLine>> coveredLineCache; 
	
	public BoundrageStringKernelDissimilary() {}
	
	public BoundrageStringKernelDissimilary(int length, double lambda) {
		
		
		rengine = new Rengine(new String[] { "--vanilla" }, false, null);
	  //  System.out.println("Rengine created, waiting for R");

        // The engine creates R is a new thread, so we should wait until it's ready
        if (!rengine.waitForR()) {
            throw new RuntimeException("Cannot load R");
        }
        rengine.eval("library(kernlab)");
        
        
        coveredLineCache = new HashMap<TestCase, List<JavaLine>>(); 
		rengine.eval("sk <- stringdot(type=\"boundrange\", length=" + length + ", lambda=" + lambda + ", normalized=TRUE)");
	}
	
	
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
		//logger.debug("String: " + encoding1);
		//logger.debug("String: "+encoding2);
		
		REXP result = rengine.eval( "sk('" + encoding1 + "','" + encoding2 + "')");
		//logger.debug("STRING KERNELS: " + result.asDouble());
		return 1 - result.asDouble();
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
		return BoundrageStringKernelDissimilary.class.getSimpleName();
	}

	@Override
	public void close() {
		rengine.end();
	}

}
