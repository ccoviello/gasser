package it.unibas.baselab.gasser.dissimilarity;

import java.util.HashMap;
import java.util.Map;

import it.unibas.jcc.data.JavaLine;

public class Encoder {
	
	private Map<JavaLine, Character> encodingMap; 
	private int index;
	
	public Encoder(){
		encodingMap = new HashMap<JavaLine, Character>();
		index = 0;
	}
	
	public Character getEncodingOf(JavaLine javaLine){
		SymbolFactory factory = SymbolFactory.getSingleton();
		Character encoding = encodingMap.get(javaLine); 
		if(encoding==null){
			encoding = factory.getSymbol(index);
			encodingMap.put(javaLine, encoding);
			index++;
		}
		return encoding;
	}

}

