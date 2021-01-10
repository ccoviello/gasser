package it.unibas.baselab.gasser;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import it.unibas.jcc.data.TestCase;
import it.unibas.jcc.data.TestSuite;
import it.unibas.baselab.gasser.jmetal.MySolution;
import it.unibas.jcc.JCCException;
import it.unibas.jcc.persistence.DAOTestSuite;

public class HandlerFiles {

	private static HandlerFiles singleton = new HandlerFiles();
	private static Logger logger = Logger.getLogger(HandlerFiles.class.getSimpleName());
    private static List<TestCase> lstTestCase = new ArrayList<>();
    private static TestSuite testSuite;
    
	private HandlerFiles() {}
	
	public static HandlerFiles getIstance() {
		return singleton;
	}
	
	public TestSuite loadTestSuiteComplete(String fileName) {
		testSuite = null;
		try {
			testSuite = DAOTestSuite.load(fileName);
			lstTestCase = testSuite.getTestCases();
		} catch (JCCException e) {
			e.printStackTrace();
		}
		return testSuite;
	}

	public List<TestCase> getLstTestCase(){
		return lstTestCase;
	}

	public TestSuite getTestSuite() {
		return this.testSuite;
	}
	
    /**
     * To save the file with the results
     **/
	public void saveResults(String fileName, List<MySolution> lstSolution) {
		File file = new File(fileName);
		try {
			FileWriter fileWriter =  new FileWriter(file);
			for(MySolution solution : lstSolution) {
				fileWriter.write(Math.abs(solution.getCodeCoverage()) + " " + solution.getTestCaseNumber()+ " " + Math.abs(solution.getDistance())+ "\n");				
			}
			fileWriter.flush();
			fileWriter.close();
		  } catch (IOException e) {
			  System.out.println("Exception: " + e.getMessage());
		  }
	}
	
	
	public void saveResultsComputed(String fileName, Set<MySolution> lstSolution) {
		File file = new File(fileName);
		try {
			FileWriter fileWriter =  new FileWriter(file);
			for(MySolution solution : lstSolution) {
				fileWriter.write(solution.getCodeCoverage() + " " + solution.getTestCaseNumber()+ " " + solution.getDistance()+ "\n");
				}
			fileWriter.flush();
			fileWriter.close();
		  } catch (IOException e) {
		   System.out.println("Exception: " + e.getMessage());
		  }
	}

	public void saveMap(String fileName, Set<MySolution> lstSolution) {
		File file = new File(fileName);
		try {
			FileWriter fileWriter =  new FileWriter(file);
			for(MySolution solution : lstSolution) {
				logger.info(solution.getMapTestCase().toString() + "\n");
			}
			fileWriter.flush();
			fileWriter.close();
		  } catch (IOException e) {
		   System.out.println("Exception: " + e.getMessage());
		  }
	}

	
	public TestCase getTestCaseFromKey(int key) {
		return lstTestCase.get(key);
	}


	public void saveCompleteResult(String fileName, List<MySolution> lstSolution) {
		File file = new File(fileName);
		 try {
			FileWriter fileWriter =  new FileWriter(file);
			for(MySolution solution : lstSolution) {
				fileWriter.write("CODE COVERAGE = " + Math.abs(solution.getCodeCoverage()) 
						+ " TEST CASE NUMBER = " + solution.getTestCaseNumber()
						+ " DISTANCE = " + Math.abs(solution.getDistance())+ "\n");
				for (Map.Entry<String, Integer> entry : solution.getMapTestCase().entrySet()) {
					if (entry.getValue() == 1) {
						TestCase testCaseiesimo = getTestCaseFromKey(Integer.parseInt(entry.getKey())-1);
						fileWriter.write(testCaseToString(testCaseiesimo));			
					}
				}
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
		   System.out.println("Exception: " + e.getMessage());
		  }
	}
	
	private String testCaseToString(TestCase test){
		return test.getId() + ";" + test.getFullName() + ";" + test.getCoveredJavaLines().size()+ "\n";
	}

	public void saveFirstInformation(String fileName, MySolution solutionFirst) {
		File file = new File(fileName);
		 try {
			FileWriter fileWriter =  new FileWriter(file);
				fileWriter.write("CODE COVERAGE = " + Math.abs(solutionFirst.getCodeCoverage()) 
						+ " TEST CASE NUMBER = " + solutionFirst.getTestCaseNumber()
						+ " DISTANCE = " + Math.abs(solutionFirst.getDistance())+ "\n");
				for (Map.Entry<String, Integer> entry : solutionFirst.getMapTestCase().entrySet()) {
					if (entry.getValue() == 1) {
						TestCase testCaseiesimo = getTestCaseFromKey(Integer.parseInt(entry.getKey())-1);
						fileWriter.write(testCaseToString(testCaseiesimo));			
					}
				}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
		   System.out.println("Exception: " + e.getMessage());
		  }
	}
	
}
