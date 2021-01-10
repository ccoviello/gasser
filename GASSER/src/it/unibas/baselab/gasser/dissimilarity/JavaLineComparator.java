package it.unibas.baselab.gasser.dissimilarity;

import java.util.Comparator;

import it.unibas.jcc.data.JavaLine;

public class JavaLineComparator implements Comparator<JavaLine> {

	@Override
	public int compare(JavaLine javaLine1, JavaLine javaLine2) {
		return javaLine1.getId().compareTo(javaLine2.getId());		
	}

}
