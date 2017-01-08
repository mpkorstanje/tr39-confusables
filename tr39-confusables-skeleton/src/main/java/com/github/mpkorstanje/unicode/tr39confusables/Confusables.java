package com.github.mpkorstanje.unicode.tr39confusables;

/**
 * Implementation of confusables tables in <a
 * href="http://www.unicode.org/reports/tr39">Unicode TR39</a>.
 * <p>
 * To see whether two strings X and Y are confusable according to a given table
 * (abbreviated as X ≅ Y), use  {@link Skeleton}.
 * 
 * @author mpkorstanje
 *
 */
public interface Confusables {

	/**
	 * Looks up {@code codePoint} in the table and returns an array of
	 * codePoints that represent a the result of a skeleton(X) transform.
	 * 
	 * If {@code codePoint} does not occur in the table, an array containing
	 * just {@code codePoint} is returned. For efficiency {@code codePoint} is
	 * not checked for validity.
	 * 
	 * @param codePoint
	 * @return an array of code points
	 */
	int[] get(int codePoint);

}
