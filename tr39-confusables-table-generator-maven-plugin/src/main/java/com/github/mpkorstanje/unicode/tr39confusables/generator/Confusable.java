package com.github.mpkorstanje.unicode.tr39confusables.generator;

import java.util.Arrays;

final class Confusable {

	public final int source;
	public final Table table;
	public final int[] target;

	public Confusable(Table table, int source, int[] target) {
		this.table = table;
		this.source = source;
		this.target = target;
	}

	@Override
	public String toString() {
		return "Confusable [source=" + source + ", target="
				+ Arrays.toString(target) + ", table=" + table + "]";
	}

}