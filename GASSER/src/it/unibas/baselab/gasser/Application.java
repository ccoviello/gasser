package it.unibas.baselab.gasser;

import java.awt.EventQueue;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.CrowdingDistanceComparator;

import it.unibas.baselab.gasser.data.DAOCSVDissimilarityMatrix;
import it.unibas.baselab.gasser.dissimilarity.BoundrageStringKernelDissimilary;
import it.unibas.baselab.gasser.dissimilarity.CosineDissimilarity;
import it.unibas.baselab.gasser.dissimilarity.EuclideanDissimilarity;
import it.unibas.baselab.gasser.dissimilarity.HammingDissimilarity;
import it.unibas.baselab.gasser.dissimilarity.IDissimilarityStrategy;
import it.unibas.baselab.gasser.dissimilarity.JaccardDistanceDissimilarity;
import it.unibas.baselab.gasser.dissimilarity.KIndexDissimilarity;
import it.unibas.baselab.gasser.dissimilarity.LevenshteinDistance;
import it.unibas.baselab.gasser.jmetal.MyCrossover;
import it.unibas.baselab.gasser.jmetal.MyMutation;
import it.unibas.baselab.gasser.jmetal.MyProblem;
import it.unibas.baselab.gasser.jmetal.MySolution;
import it.unibas.baselab.gasser.jmetal.Simulator;
import it.unibas.jcc.data.TestCase;
import it.unibas.jcc.data.TestSuite;

public class Application extends AbstractAlgorithmRunner {

	private static String system = "";
	private static String covFile = "";
	private static String completeFile = "";
	private static String populationFile = "";

	private static int maxIteration = 1000;
	private static int populationSize = 100;

	private static boolean computeDissimilarity = true;
	private static String dissimilarityMeasureName = "";
	private static String dissimilarityFilePath = "";
	private static IDissimilarityStrategy dissimilarityMeasureStrategy = null;


	private static Application singleton = new Application();
	private static Logger logger = Logger.getLogger(Application.class.getSimpleName());


	private Application() {}

	public static Application getIstance() {
		return singleton;
	}

	public static void main(String[] args) {
		showMenu();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				launchJMetal();
			}
		});
	}


	private static void showMenu() {
		System.out.println("-------------------------------------------------------------");
		System.out.println("----------------------------GASSER---------------------------");
		System.out.println("-------------------------------------------------------------");
		Scanner input = new Scanner(System.in);
		System.out.println("-------------------------------------------------------------");
		System.out.println("Type the full path of your coverage information file:");
		covFile = input.nextLine();
		System.out.println("-------------------------------------------------------------");
		System.out.println("Type the name of you System Under Test:");
		system = input.nextLine();

		System.out.println("Do you have a dissimilarity matrix? [Y/N]");
		
		String response = input.nextLine(); 
		while((!response.equalsIgnoreCase("Y")) && (!response.equalsIgnoreCase("N"))) {
			System.out.println("Type Y or N");
			System.out.println("Do you have a dissimilarity matrix? [Y/N]");
			//input = new Scanner(System.in);
			response = input.nextLine(); 
		}
		if(response.equalsIgnoreCase("Y")) {
			computeDissimilarity = false;
		}
		
		if(computeDissimilarity) {
			System.out.println("-------------------------------------------------------------");
			System.out.println("Choose the dissimilarity measure you want to use:");
			System.out.println("C = Cosine Dissimilarity");
			System.out.println("E = Euclidean Distance");
			System.out.println("J = Jaccard-Based Dissimilarity");
			System.out.println("K = K-Based Dissimilarity");
			System.out.println("H = Hamming Distance");
			System.out.println("L = Levenshtein Distance");
			System.out.println("SK = String Kernel Dissimilarity");
			dissimilarityMeasureName = input.nextLine();
			while((!dissimilarityMeasureName.equalsIgnoreCase("C")) && (!dissimilarityMeasureName.equalsIgnoreCase("E")) && (!dissimilarityMeasureName.equalsIgnoreCase("J")) && (!dissimilarityMeasureName.equalsIgnoreCase("K")) && (!dissimilarityMeasureName.equalsIgnoreCase("H"))&& (!dissimilarityMeasureName.equalsIgnoreCase("L")) && (!dissimilarityMeasureName.equalsIgnoreCase("SK"))){
				System.out.println("ERROR. Type C or E or J or K or H or L or SK");
				System.out.println("Choose the dissimilarity measure you want to use :");
				System.out.println("C = Cosine Dissimilarity");
				System.out.println("E = Euclidean Distance");
				System.out.println("J = Jaccard-Based Dissimilarity");
				System.out.println("K = K-Based Dissimilarity");
				System.out.println("H = Hamming Distance");
				System.out.println("L = Levenshtein Distance");
				System.out.println("SK = String Kernel Dissimilarity");
				dissimilarityMeasureName = input.nextLine();
			}
		} else {
			System.out.println("-------------------------------------------------------------");
			System.out.println("Type the full path of your dissimilarity matrix file:");
			dissimilarityFilePath = input.nextLine();

		}
		System.out.println("-------------------------------------------------------------");
		System.out.println("The default value for maxEvaluations is equal to 1000, do you want to change it? [Y/N]");
		String setIterations = input.nextLine(); 
		while((!setIterations.equalsIgnoreCase("Y")) && (!setIterations.equalsIgnoreCase("N"))) {
			System.out.println("ERROR. Type Y or N");
			System.out.println("The default value for maxEvaluations is equal to 1000, do you want to change it? [Y/N]");
			setIterations = input.nextLine(); 
		}

		if(setIterations.equalsIgnoreCase("y")) {
			System.out.println("-------------------------------------------------------------");
			System.out.println("Type the maxEvaluations value (int):");
			String maxIterationValue = input.nextLine();
			boolean notNumber = true;
			while(notNumber){
				try {
					maxIteration = Integer.parseInt(maxIterationValue);
					notNumber = false;
				} catch (NumberFormatException e) {
					System.out.println("-------------------------------------------------------------");
					System.out.println("ERROR. maxEvaluations shoud be an integer.");
					System.out.println("Type the maxEvaluations value (integer):");
					maxIterationValue = input.nextLine();
				}
			}
		}

		System.out.println("-------------------------------------------------------------");
		System.out.println("The default value for populationSize is equal to 100, do you want to change it? [Y/N]");
		String setPopulationSize = input.nextLine(); 
		while((!setPopulationSize.equalsIgnoreCase("Y")) && (!setPopulationSize.equalsIgnoreCase("N"))) {
			System.out.println("ERROR. Type Y or N");
			System.out.println("The default value for populationSize is equal to 100, do you want to change it? [Y/N]");
			setPopulationSize = input.nextLine(); 
		}

		if(setPopulationSize.equalsIgnoreCase("y")) {
			System.out.println("-------------------------------------------------------------");
			System.out.println("Type the populationSize value (integer):");
			String populationSizeString = input.nextLine();

			boolean notNumber = true;
			while(notNumber){
				try {
					populationSize = Integer.parseInt(populationSizeString);
					notNumber = false;
				} catch (NumberFormatException e) {
					System.out.println("-------------------------------------------------------------");
					System.out.println("ERROR. populationSize shoud be an integer.");
					System.out.println("Type the populationSize value (integer):");
					populationSizeString = input.nextLine();
				}
			}
		}
		completeFile = "COMPLETE_SOLUTION" + system + ".tsv";
		populationFile = "POPULATION" + system + ".tsv";
	}

	private static void launchJMetal() {
		logger.info("******************runningGASSER******************");
		Simulator simulator = new Simulator(computeDissimilarity);
		Problem<MySolution> myProblem = new MyProblem(simulator, covFile);

		if(computeDissimilarity) {
			createDissimilarityMeasure();
			simulator.setStrategy(dissimilarityMeasureStrategy);
		} else {
			String dissimilarityPath = dissimilarityFilePath;
			it.unibas.baselab.gasser.data.IDAODissimilarityMatrix daoDissimilarityMatrix = new DAOCSVDissimilarityMatrix();
			it.unibas.baselab.gasser.data.DissimilarityMatrix dissimilarityMatrix = null;
			dissimilarityMatrix = daoDissimilarityMatrix.read(dissimilarityPath, ((MyProblem) myProblem).getInitialTestSuite());
			simulator.setDissimilarityMatrix(dissimilarityMatrix);
		}

		Algorithm<List<MySolution>> algorithm;
		CrossoverOperator<MySolution> crossover = new MyCrossover();
		MutationOperator<MySolution> mutation = new MyMutation();


		SelectionOperator<List<MySolution>, MySolution> selection = new BinaryTournamentSelection<MySolution>(new CrowdingDistanceComparator<MySolution>());
		algorithm = new NSGAIIBuilder<MySolution>(myProblem, crossover, mutation)
				.setSelectionOperator(selection)
				.setMaxEvaluations(maxIteration)
				.setPopulationSize(populationSize)
				.build();
		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		if(dissimilarityMeasureName.equals("SK")) {
			dissimilarityMeasureStrategy.close();
		}
		logger.info("Number of found solutions: " + algorithm.getResult().size());	

		HandlerFiles.getIstance().saveCompleteResult(completeFile, algorithm.getResult());
		HandlerFiles.getIstance().saveResults(populationFile, algorithm.getResult());
		logger.info("******************************DONE :)*****************************");
		logger.info("**********************Thanks for using GASSER*********************");

	}


	private static void createDissimilarityMeasure() {
		if(dissimilarityMeasureName.equalsIgnoreCase("C")) {
			dissimilarityMeasureStrategy = new CosineDissimilarity(HandlerFiles.getIstance().getTestSuite());
		} else if(dissimilarityMeasureName.equalsIgnoreCase("E")) {
			dissimilarityMeasureStrategy = new EuclideanDissimilarity(HandlerFiles.getIstance().getTestSuite());
		} else if(dissimilarityMeasureName.equalsIgnoreCase("J")) {
			dissimilarityMeasureStrategy = new JaccardDistanceDissimilarity(HandlerFiles.getIstance().getTestSuite());
		} else if(dissimilarityMeasureName.equalsIgnoreCase("K")) {
			dissimilarityMeasureStrategy = new KIndexDissimilarity(HandlerFiles.getIstance().getTestSuite());
		} else if(dissimilarityMeasureName.equalsIgnoreCase("H")) {
			dissimilarityMeasureStrategy = new HammingDissimilarity(HandlerFiles.getIstance().getTestSuite());
		} else if(dissimilarityMeasureName.equalsIgnoreCase("L")) {
			dissimilarityMeasureStrategy = new LevenshteinDistance();
		} else if(dissimilarityMeasureName.equalsIgnoreCase("SK")) {
			int mean = calculateCoveredLinesOnAvg(HandlerFiles.getIstance().getTestSuite());			
			dissimilarityMeasureStrategy = new BoundrageStringKernelDissimilary(mean,1.1);
		} 
	}

	private static int calculateCoveredLinesOnAvg(TestSuite originalTestSuite) {
		double mean = 0d;
		double n = originalTestSuite.getTestCases().size();
		for (int i = 0; i < n; i++) {
			TestCase testCase = originalTestSuite.getTestCases().get(i);
			mean += testCase.getCoveredJavaLines().size();	

			if(testCase.getCoveredJavaLines().size()==0){
				System.out.println(testCase);
			}

		}
		mean /= n; 
		return (int) mean;
	}
}


