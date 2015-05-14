package com.github.mpkorstanje.unicode.tr39confusables.generator;

enum Table {

	MixedScriptAnyCase("MA"), SingleScriptLowerCase("SL"), SingleScriptAnyCase(
			"SA"), MixedScriptLowerCase("ML");
	
	private final String code;
	
	private Table(String code){
		this.code = code;
	}
	
	public static Table parseTable(String code){
		for(Table t : Table.values()){
			if(t.code.equals(code)){
				return t;
			}
		}
		
		throw new IllegalArgumentException(code);
	}

}