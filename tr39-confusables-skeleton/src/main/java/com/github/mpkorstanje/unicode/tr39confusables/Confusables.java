package com.github.mpkorstanje.unicode.tr39confusables;

public interface Confusables {
	
	Confusables MIXED_SCRIPT_ANY_CASE = new MixedScriptAnyCase();

	
	int[] get(int codePoint);

}
