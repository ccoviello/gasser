package it.unibas.baselab.gasser.dissimilarity;

public class Levenshtein {

	private static Levenshtein singleton = new Levenshtein();

	private Levenshtein() {}
	
	public static Levenshtein getIstance() {
		return singleton;
	}
  
    public final double distance(final String firstString, final String secondString) {
        if (firstString == null || secondString == null) {
            throw new NullPointerException("strings must not be null");
        }
        if (firstString.equals(secondString)) {
            return 0;
        }
        if (firstString.length() == 0) {
            return secondString.length();
        }
        if (secondString.length() == 0) {
            return firstString.length();
        }

        int[] firstVector = new int[secondString.length() + 1];
        int[] secondVector = new int[secondString.length() + 1];
        int[] temporaryVector;


        for (int i = 0; i < firstVector.length; i++) {
            firstVector[i] = i;
        }

        for (int i = 0; i < firstString.length(); i++) {
            secondVector[0] = i + 1;
            for (int j = 0; j < secondString.length(); j++) {
                int cost = 1;
                if (firstString.charAt(i) == secondString.charAt(j)) {
                    cost = 0;
                }
                secondVector[j + 1] = Math.min(secondVector[j] + 1,Math.min(firstVector[j + 1] + 1,firstVector[j] + cost)); 
            }
            temporaryVector = firstVector;
            firstVector = secondVector;
            secondVector = temporaryVector;

        }

        return firstVector[secondString.length()];
    }
}

