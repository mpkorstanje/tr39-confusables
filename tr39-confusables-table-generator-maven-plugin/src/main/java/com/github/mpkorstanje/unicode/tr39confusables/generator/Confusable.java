package com.github.mpkorstanje.unicode.tr39confusables.generator;

import java.util.Arrays;

final class Confusable {

	public final int source;
	public final Table table;
	public final int[] target;
	public final String comment;

	public Confusable(Table table, int source, int[] target, String comment) {
		this.table = table;
		this.source = source;
		this.target = target;
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Confusable [source=" + source + ", target="
				+ Arrays.toString(target) + ", table=" + table + "]";
	}

}