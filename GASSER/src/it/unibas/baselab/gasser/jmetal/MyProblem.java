package it.unibas.baselab.gasser.jmetal;

import org.uma.jmetal.problem.Problem;

import it.unibas.baselab.gasser.HandlerFiles;
import it.unibas.jcc.data.TestSuite;

@SuppressWarnings("serial")
public class MyProblem implements Problem<MySolution>{

    private MySolution solutionFirst;
	private Simulator simulator;
	private TestSuite initialTestSuite;
	public MyProblem() {}
		
		
	public MyProblem(Simulator simulator, String fileName) {
		this.simulator = simulator;
		setTestSuiteIniziale(HandlerFiles.getIstance().loadTestSuiteComplete(fileName));
		solutionFirst = new MySolution(getInitialTestSuite(), getInitialTestSuite().getTestCases().size());
	}
	
	@Override
	public int getNumberOfVariables() {
		return 3;
	}
	
	@Override
	public int getNumberOfObjectives() {
		return 3;
	}
	
	@Override
	public int getNumberOfConstraints() {
		return 0;
	}

	@Override
	public String getName() {
		return "MyProblem";
	}

	@Override
	public void evaluate(MySolution mySolution) {
		MySolution solution = simulator.simulate(mySolution);
		mySolution.setVariableValue(0, solution.getCodeCoverage());
		mySolution.setVariableValue(1, solution.getTestCaseNumber());
		mySolution.setVariableValue(2, solution.getDistance());
		mySolution.setObjective(0, solution.getCodeCoverage());
		mySolution.setObjective(1, solution.getTestCaseNumber());
		mySolution.setObjective(2, solution.getDistance());
	}
	
	@Override
	public MySolution createSolution() {
		return this.solutionFirst;
	}
	
	public MySolution getSolutionFirst() {
		return this.solutionFirst;
	}


	public TestSuite getInitialTestSuite() {
		return initialTestSuite;
	}


	private void setTestSuiteIniziale(TestSuite testSuiteIniziale) {
		this.initialTestSuite = testSuiteIniziale;
	}
	
}
