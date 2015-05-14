package com.github.mpkorstanje.unicode.tr39confusables.generator;

import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Collections2.filter;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Predicate;

final class TableModel {

	private class BelowPivot implements Predicate<Confusable> {

		private final Confusable pivot;

		public BelowPivot(Confusable pivot) {
			this.pivot = pivot;
		}

		@Override
		public boolean apply(Confusable input) {
			return pivot.source > input.source;
		}

	}

	private final List<Confusable> confusables;
	private final Collection<Confusable> confusables01;
	private final Collection<Confusable> confusables02;
	private final Confusable pivot;

	public TableModel(List<Confusable> confusables) {
		this.confusables = confusables;
		this.pivot = confusables.get(confusables.size() / 2);
		this.confusables01 = filter(confusables, new BelowPivot(pivot));
		this.confusables02 = filter(confusables, not(new BelowPivot(pivot)));
	}

	public List<Confusable> getConfusables() {
		return confusables;
	}

	public Collection<Confusable> getConfusables01() {
		return confusables01;
	}

	public Collection<Confusable> getConfusables02() {
		return confusables02;
	}

	public Confusable getPivot() {
		return pivot;
	}

}