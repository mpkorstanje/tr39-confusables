package com.github.mpkorstanje.unicode.tr39confusables;

import static java.text.Normalizer.normalize;
import static java.text.Normalizer.Form.NFD;
import static com.github.mpkorstanje.unicode.tr39confusables.Confusables.MIXED_SCRIPT_ANY_CASE;
/**
 * Implementation of Skeleton transform in <a
 * href="http://www.unicode.org/reports/tr39">Unicode TR39</a>.
 * <p>
 * 
 * To see whether two strings X and Y are confusable according to a given
 * {@link Confusables} table (abbreviated as X ≅ Y), an implementation uses a
 * transform of X called a skeleton(X).
 * <p>
 * The transform consist of:
 * <p>
 * 
 * <ol>
 * <li>Converting X to NFD format.
 * <li>Successively mapping each source character in X to the target string
 * according to the specified data table.
 * <li >Reapplying NFD.
 * </ol>
 * 
 * The resulting strings skeleton(X) and skeleton(Y) can be compared. If they
 * are identical (codepoint-for-codepoint), then X ≅ Y according to the table.
 * <p>
 * The transform imposes transitivity on the data, so if X ≅ Y and Y ≅ Z, then X
 * ≅ Z.
 * <p>
 * Note: The strings skeleton(X) and skeleton(Y) are not intended for display,
 * storage or transmission. They should be thought of as an intermediate
 * processing form, similar to a hashcode. The characters in skeleton(X) and
 * skeleton(Y) are not guaranteed to be identifier characters.
 * 
 * @author mpkorstanje
 *
 */
public final class Skeleton {

	/**
	 * Applies a skeleton transform to {@code s}. Uses the the Mixed Script Any
	 * Case table.
	 * 
	 * @param s
	 *            a string
	 * @return a string transformed by skeleton(X)
	 * @throws NullPointerException
	 *             when {@code s } is null
	 */
	public static String skeleton(String s) {
		return skeleton(s, MIXED_SCRIPT_ANY_CASE);
	}

	/**
	 * Applies a skeleton transform to {@code s} using the given
	 * {@code confusables} as the lookup table.
	 * 
	 * @param s
	 *            a string
	 * @param confusables
	 *            a confusables lookup table
	 * @return a string transformed by skeleton(X)
	 * @throws NullPointerException
	 *             when {@code s } or {@code confusables} is null
	 */
	public static String skeleton(String s, Confusables confusables) {
		if (s == null)
			throw new NullPointerException("s may not be null");
		if (confusables == null)
			throw new NullPointerException("confusables may not be null");

		// 1. Converting X to NFD format
		s = normalize(s, NFD);

		// 2. Successively mapping each source character in X to the target
		// string according to the specified data table.
		final StringBuilder sb = new StringBuilder();
		for (int codePoint : codePoints(s)) {
			final int[] codePoints = confusables.get(codePoint);
			sb.append(new String(codePoints, 0, codePoints.length));
		}

		// 3. Reapplying NFD.
		s = normalize(sb.toString(), NFD);
		return s;
	}

	private static int[] codePoints(String s) {
		int[] codePoints = new int[s.codePointCount(0, s.length())];
		int i = 0;

		for (int offset = 0; offset < s.length(); offset = s
				.offsetByCodePoints(offset, 1)) {
			codePoints[i++] = s.codePointAt(offset);
		}

		return codePoints;
	}

	private final String skeleton;

	/**
	 * Creates a skeleton transform of {@code s}. Uses the the Mixed Script Any
	 * Case table.
	 * 
	 * @param s
	 *            a string
	 * @throws NullPointerException
	 *             when {@code s } is null
	 */
	public Skeleton(String s) {
		this(s, MIXED_SCRIPT_ANY_CASE);
	}

	/**
	 * Creates a skeleton transform of {@code s} using {@code confusables} as
	 * the lookup table.
	 * 
	 * @param s
	 *            a string
	 * @param confusables
	 *            a confusables lookup table
	 * @throws NullPointerException
	 *             when {@code s } or {@code confusables} is null
	 */
	public Skeleton(String s, Confusables confusables) {
		this.skeleton = skeleton(s, confusables);
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

		return skeleton.equals(other.skeleton);
	}

	@Override
	public int hashCode() {
		return skeleton.hashCode();
	}

	/**
	 * Returns the skeleton transformed string.
	 * 
	 * @return the skeleton transformed string
	 */
	@Override
	public String toString() {
		return skeleton;
	}

}
