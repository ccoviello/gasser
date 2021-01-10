package it.unibas.baselab.gasser.dissimilarity;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class SymbolFactory {
	
	private static SymbolFactory singleton;
	
	private List<Character> symbols;
	
	public final static int BMP_UPPER_BOUND = 65536;
	
	private SymbolFactory(){
		symbols = new ArrayList<Character>();
		for (int i = 0; i < BMP_UPPER_BOUND; i++) {
			if(Character.isDefined(i) && !Character.isISOControl(i) && isLetterOrNumber(i) && isNormalized(i)){
				symbols.add(new Character((char)i));
			}
		}
	}
	
	private boolean isLetterOrNumber(int codePoint){
		int type = Character.getType(codePoint);
		if(type == Character.LETTER_NUMBER || type == Character.LOWERCASE_LETTER 
				|| type == Character.UPPERCASE_LETTER || type == Character.OTHER_LETTER){
			return true;
		}
		return false; 
	}
	
	private boolean isNormalized(int codePoint){
		String s = new String(new char[]{(char)codePoint});
		if(Normalizer.isNormalized(s, Normalizer.Form.NFD)){
			return true;
		}
		return false;
	}
	
	public static SymbolFactory getSingleton(){
		if(singleton == null){
			singleton = new SymbolFactory();
		}
		return singleton;
	}
	
	public Character getSymbol(int index){
		return symbols.get(index);
	}
	
	public int maxNoSymbols(){
		return symbols.size();
	}
	

}