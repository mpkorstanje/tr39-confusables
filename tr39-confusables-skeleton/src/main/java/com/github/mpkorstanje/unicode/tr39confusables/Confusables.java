package com.github.mpkorstanje.unicode.tr39confusables;

public interface Confusables {
	
	Confusables MIXED_SCRIPT_ANY_CASE = new MixedScriptAnyCase();
	Confusables MIXED_SCRIPT_LOWER_CASE = new MixedScriptLowerCase();
	Confusables SINGLE_SCRIPT_ANY_CASE = new SingleScriptAnyCase();
	Confusables SINGLE_SCRIPT_LOWER_CASE = new SingleScriptLowerCase();

	
	int[] get(int codePoint);

}
