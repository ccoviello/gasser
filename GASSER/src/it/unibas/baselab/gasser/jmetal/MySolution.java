package it.unibas.baselab.gasser.jmetal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.uma.jmetal.solution.Solution;

import it.unibas.baselab.gasser.HandlerFiles;

public class MySolution implements Solution<Double> {
	
	private Map<String, Integer> mapTestCase = new HashMap<String, Integer>();
	
	
	private double objectives[] = new double[3];
	private double variables[] = new double[3];
	private double codeCoverage = 0.0;
	private double testCaseNumber = 0.0;
	private double distanceAmongLines = 0.0;
	protected Map<Object, Object> attributes = new HashMap<Object, Object>();


	public MySolution() {}
	
	public MySolution(MySolution solution) {
		for (int i = 0; i <solution.getNumberOfVariables(); i++) {
			this.variables[i] = solution.getVariableValue(i) ;
		}
		for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
			objectives[i] = solution.getObjective(i) ;
		}
		attributes = new HashMap<Object, Object>(solution.attributes);
	}
	

	
	public MySolution(it.unibas.jcc.data.TestSuite testSuite, int size) {
		int value = 0;
		for (int i = 0; i < size; i++) {
				value = ThreadLocalRandom.current().nextInt(0, 2);
				if (value == 1) {
				this.mapTestCase.put(Integer.toString(i+1), 1);
					this.codeCoverage += (testSuite.getTestCases().get(i).getCoveredJavaLines().size()*(-1));	
				}
				else {
					this.mapTestCase.put(Integer.toString(i+1), 0);	
				}
			}
		}
	
	
	/**
	 * Computes the codeCoverage(CC) and the number of test cases (NTC) executed by the n solution
	 *
	 * **/
	public void calculateCCNTC() {
		this.codeCoverage = 0;
		this.testCaseNumber = 0;
		for (Map.Entry<String, Integer> entry : this.getMapTestCase().entrySet()) {
			if (entry.getValue() == 1){
				codeCoverage += (HandlerFiles.getIstance().getLstTestCase().get(Integer.parseInt(entry.getKey())-1).getCoveredJavaLines().size() * (-1));
				this.testCaseNumber ++;
			}
		}
	}

	/**
	 * Prints the solutions details
	 * 
	 * 
	 **/
	public String toShortString() {
		calculateCCNTC();
		StringBuilder sb = new StringBuilder();
		sb.append("RIF." + this.toString());
		sb.append(" {");
		for (Map.Entry<String, Integer> entry : this.mapTestCase.entrySet()) {
		    sb.append(entry.getKey() + "=" + entry.getValue()+ " ");
		}
		sb.append("}");
		sb.append("- CC:" + this.codeCoverage + " - NTC: " + this.testCaseNumber + "- DISTANCE:" + this.distanceAmongLines);
		return sb.toString();
	}
	
	public Map<String, Integer> getMapTestCase(){
		return this.mapTestCase;
	}
	
	public int getSizeMappa() {
		return this.mapTestCase.size();
	}
	
	public int getValueMapFromKey(String key) {
		return mapTestCase.get(key);
	}
	
	public void putValueMapFromKey(String key, int value) {
		this.mapTestCase.put(key, value);
	}
	
	@Override
	public Solution<Double> copy() {
		return new MySolution(this);
	}

	@Override
	public void setAttribute(Object id, Object value) {
		attributes.put(id, value) ;
	}

	@Override
	public Object getAttribute(Object id) {
		return attributes.get(id) ;
	}

	@Override
	public int getNumberOfObjectives() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getNumberOfVariables() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void setObjective(int index, double value) {
		this.objectives[index] = value;
	}
	@Override
	public double getObjective(int index) {
		return this.objectives[index];
	}
	@Override
	public double[] getObjectives() {
		return objectives;
	}

	@Override
	public String getVariableValueString(int index) {
		// TODO Auto-generated method stub
		return getVariableValue(index).toString();
	}


	public double getCodeCoverage() {
		return codeCoverage;
	}
	

	public double getDistance() {
		return distanceAmongLines;
	}
	
	public void setDistance(double distance) {
		this.distanceAmongLines = distance;
	}
	
	/**
	 * @param costFunction the costFunction to set
	 */
	public void setCodeCoverage(double codeCoverage) {
		this.codeCoverage = codeCoverage;
	}
	/**
	 * @return the timeFunction
	 */
	public double getTestCaseNumber() {
		return testCaseNumber;
	}
	/**
	 * @param timeFunction the timeFunction to set
	 */
	public void setTestCaseNumber(int testCaseNumber) {
		this.testCaseNumber = testCaseNumber;
	}

	@Override
	public Double getVariableValue(int index) {
		return variables[index];
	}

	@Override
	public void setVariableValue(int index, Double value) {
		variables[index] = value;
		
	}

}
