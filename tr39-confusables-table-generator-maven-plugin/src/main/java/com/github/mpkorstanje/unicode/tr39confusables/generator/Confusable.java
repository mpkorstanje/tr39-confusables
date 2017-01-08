package com.github.mpkorstanje.unicode.tr39confusables.generator;

import java.util.Arrays;

final class Confusable {

	public final int source;
	public final int[] target;
	public final String comment;

	public Confusable(int source, int[] target, String comment) {
		this.source = source;
		this.target = target;
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "Confusable [source=" + source + ", target="
				+ Arrays.toString(target)  + "]";
	}

}