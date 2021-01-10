package it.unibas.baselab.gasser.jmetal;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.uma.jmetal.operator.MutationOperator;

public class MyMutation implements MutationOperator<MySolution> {

	private static Random random = new Random();

	
	@Override
	public MySolution execute(MySolution solution) {		
		int min = 1;
		int max = solution.getSizeMappa() + 1;
		int numberCeilToChange = ThreadLocalRandom.current().nextInt(min, max);
		
		Set<Integer> ceilsToMutations = new HashSet();
		while (ceilsToMutations.size() < numberCeilToChange) {
			int ceilToChange = 0;
			do {
				ceilToChange = random.nextInt(solution.getSizeMappa())+1;
				ceilsToMutations.add(ceilToChange);
			} while(ceilToChange == 0  || ceilToChange == solution.getSizeMappa());
		}
		for (Integer cella : ceilsToMutations) {
			doMutation(solution, cella);
		}
		return solution;
	}

	

	public MySolution doMutation(MySolution solution, int ceilToChange) {
		int valueToChange = solution.getValueMapFromKey(String.valueOf(ceilToChange));
		if (valueToChange == 0) {
			solution.putValueMapFromKey(String.valueOf(ceilToChange), 1);			
		} else {
				solution.putValueMapFromKey(String.valueOf(ceilToChange), 0);			
		}
		return solution;
	}
	
}