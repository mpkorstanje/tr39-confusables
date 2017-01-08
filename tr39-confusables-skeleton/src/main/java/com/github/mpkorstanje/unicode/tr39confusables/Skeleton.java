package com.github.mpkorstanje.unicode.tr39confusables;

import static java.lang.System.arraycopy;
import static java.text.Normalizer.normalize;
import static java.text.Normalizer.Form.NFD;

/**
 * Implementation of the Skeleton transform described in <a
 * href="http://www.unicode.org/reports/tr39">Unicode TR39</a>.
 * <p>
 * 
 * The skeleton transform makes it possible to determine if two strings X and Y
 * are confusable (abbreviated as X ≅ Y) according to a given
 * {@link Confusables} table.
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

	private static final Confusables table = new ConfusablesSwitchCase();

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
		if (s == null)
			throw new NullPointerException("s may not be null");

		// 1. Converting X to NFD format
		s = normalize(s, NFD);

		// 2. Successively mapping each source character in X to the target
		// string according to the specified data table.
		int i = 0;
		final int[][] codePoints = new int[s.length()][];
		for (int offset = 0; offset < s.length(); offset = s.offsetByCodePoints(offset, 1)) {
			codePoints[i++] = table.get(s.codePointAt(offset));
		}
		
		// 3. Reapplying NFD.
		s = normalize(toString(codePoints), NFD);
		return s;
	}

	private static String toString(final int[][] codePoints) {
		int length = 0;
		for (int[] array : codePoints) {
			if (array == null) {
				break;
			}
			length += array.length;
		}
		int[] result = new int[length];
		int pos = 0;
		for (int[] array : codePoints) {
			if (array == null) {
				break;
			}
			arraycopy(array, 0, result, pos, array.length);
			pos += array.length;
		}
		
		return new String(result, 0, result.length);
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
		this.skeleton = skeleton(s);
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
