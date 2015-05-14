package com.github.mpkorstanje.unicode.tr39confusables;

import java.text.Normalizer;
import static com.github.mpkorstanje.unicode.tr39confusables.Confusables.MIXED_SCRIPT_ANY_CASE;

public class Skeleton {
	

	private final String skeleton;

	public static String skeleton(String s) {
		return skeleton(s,MIXED_SCRIPT_ANY_CASE);
	}

	public static String skeleton(String s, Confusables confusables) {
		// To see whether two strings X and Y are confusable according to a
		// given table (abbreviated as X ≅ Y), an implementation uses a
		// transform of X called a skeleton(X) defined by:
		//
		// 1. Converting X to NFD format, as described in [UAX15].
		s = Normalizer.normalize(s, Normalizer.Form.NFD);

		// 2. Successively mapping each source character in X to the target
		// string
		// according to the specified data table.
		final StringBuilder sb = new StringBuilder();

		for (int offset = 0; offset < s.length(); offset = s
				.offsetByCodePoints(offset, 1)) {
			final int codePoint = s.codePointAt(offset);
			final int[] codePoints = confusables.get(codePoint);
			sb.append(new String(codePoints, 0, codePoints.length));
		}
		s = sb.toString();

		// 3. Reapplying NFD.
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
		return s;
	}

	public Skeleton(String s) {
		this(s, MIXED_SCRIPT_ANY_CASE);
	}
	public Skeleton(String s, Confusables confusables) {
		this.skeleton = skeleton(s,confusables);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((skeleton == null) ? 0 : skeleton.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Skeleton other = (Skeleton) obj;
		if (skeleton == null) {
			if (other.skeleton != null)
				return false;
		} else if (!skeleton.equals(other.skeleton))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return skeleton;
	}

}
