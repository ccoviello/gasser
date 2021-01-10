package it.unibas.baselab.gasser.jmetal;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.uma.jmetal.operator.CrossoverOperator;

public class MyCrossover implements CrossoverOperator<MySolution>{
	
	private static Random random = new Random();

	
	@Override
	public List<MySolution> execute(List<MySolution> parents) {
		MySolution father = parents.get(0);
		MySolution mother = parents.get(1);
		return doTwoPointsCrossover(father,mother);
		//return parents;
	}

	
	private List<MySolution> doTwoPointsCrossover(MySolution father, MySolution mother) {
		int pointBreak1, pointBreak2;
		do {
			pointBreak1 = ThreadLocalRandom.current().nextInt(0, father.getSizeMappa());
			pointBreak2 = ThreadLocalRandom.current().nextInt(pointBreak1+1, father.getSizeMappa()+1);
		} while(pointBreak1 == 0  || pointBreak1 == father.getSizeMappa()||
				pointBreak2 == 0  || pointBreak2 == father.getSizeMappa());
		MySolution firstSon = new MySolution();
		MySolution secondSon = new MySolution();
		
		
		addBeforeBreakPoint (firstSon, mother, pointBreak1);
		addBeforeBreakPoint (secondSon, father, pointBreak1);

		addBetweenBreakPoints (firstSon, father, pointBreak1, pointBreak2);
		addBetweenBreakPoints (secondSon, mother, pointBreak1, pointBreak2);

		
		addAfterBreakPoint (firstSon, mother, pointBreak2);
		addAfterBreakPoint (secondSon, father, pointBreak2);
		
		List<MySolution> swapped = new ArrayList<MySolution>();
		swapped.add(firstSon);
		swapped.add(secondSon);
		return swapped;
	}


	private List<MySolution> doSinglePointCrossover(MySolution father, MySolution mother) {
		int pointBreak;
		do {
			pointBreak = random.nextInt(father.getSizeMappa()+1);
		} while(pointBreak == 0  || pointBreak == father.getSizeMappa());
	
		MySolution firstSon = new MySolution();
		MySolution secondSon = new MySolution();
		
		addBeforeBreakPoint (firstSon, father, pointBreak);
		addBeforeBreakPoint (secondSon, mother, pointBreak);
		
		addAfterBreakPoint (secondSon, father, pointBreak);
		addAfterBreakPoint (firstSon, mother, pointBreak);
		
		List<MySolution> swapped = new ArrayList<MySolution>();
		swapped.add(firstSon);
		swapped.add(secondSon);
		return swapped;
	}
	
	private void addBeforeBreakPoint (MySolution son, MySolution parent, int pointBreak) {
		for (Map.Entry<String, Integer> entry : parent.getMapTestCase().entrySet()) {
			if (Integer.parseInt(entry.getKey()) <= pointBreak){
				son.putValueMapFromKey(entry.getKey(), entry.getValue());				
			}
		}
	}
	
	private void addAfterBreakPoint (MySolution son, MySolution parent, int pointBreak) {
		for (Map.Entry<String, Integer> entry : parent.getMapTestCase().entrySet()) {
			if (Integer.parseInt(entry.getKey()) > pointBreak){
				son.putValueMapFromKey(entry.getKey(), entry.getValue());				
			}
		}
	}
	
	private void addBetweenBreakPoints (MySolution son, MySolution parent, int pointBreak1, int pointBreak2) {	
		for (Map.Entry<String, Integer> entry : parent.getMapTestCase().entrySet()) {
			if (Integer.parseInt(entry.getKey()) > pointBreak1 && Integer.parseInt(entry.getKey()) <= pointBreak2){
				son.putValueMapFromKey(entry.getKey(), entry.getValue());				
			}
		}
	}

	@Override
	public int getNumberOfGeneratedChildren() {
		return 2;
	}

	@Override
	public int getNumberOfRequiredParents() {
		return 2;
	}


}
