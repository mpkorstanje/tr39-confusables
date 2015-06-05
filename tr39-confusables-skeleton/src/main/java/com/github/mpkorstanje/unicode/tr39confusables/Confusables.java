package com.github.mpkorstanje.unicode.tr39confusables;

/**
 * Implementation of confusables tables in <a
 * href="http://www.unicode.org/reports/tr39">Unicode TR39</a>.
 * <p>
 * To see whether two strings X and Y are confusable according to a given table
 * (abbreviated as X ≅ Y), use  {@link Skeleton}.
 * <p>
 * The data is organized into four different tables, depending on the desired
 * parameters. Each table provides a mapping from source characters to target
 * strings. On the basis of this data, there are three main classes of
 * confusable strings:
 * <p>
 * Definitions:
 * <p>
 * 
 * X and Y are single-script confusables if they are confusable according to the
 * Single-Script table, and each of them is a single script string according to
 * <a href="http://www.unicode.org/reports/tr39/#Mixed_Script_Detection">Section
 * 5, Mixed-Script Detection</a>, and it is the same script for each.
 * <p>
 * Examples: "so̷s" and "søs" in Latin, where the first word has the character
 * "o" followed by the character U+0337 ( ̷  ) COMBINING SHORT SOLIDUS OVERLAY.
 * <p>
 * X and Y are <a
 * href="http://www.unicode.org/reports/tr39/#mixed_script_confusables"
 * >mixed-script confusables</a> if they are confusable according to the
 * Mixed-Script table, and they are not single-script confusables.
 * <p>
 * Examples: "paypal" and "pаypаl", where the second word has the character
 * U+0430 ( а ) CYRILLIC SMALL LETTER A.
 * <p>
 * X and Y are <a
 * href="http://www.unicode.org/reports/tr39/#whole_script_confusables"
 * >whole-script confusables</a> if they are mixed-script confusables, and each
 * of them is a single script string.
 * <p>
 * Example: "scope" in Latin and "ѕсоре" in Cyrillic.
 * <p>
 * The mixed script any case table is the most complete. It is thus the recommended table to use,
 * unless there is a strong reason to use one of the others.
 * 
 * @author mpkorstanje
 *
 */
public interface Confusables {
	/**
	 * This table is used to test cases of single-script confusables, where both
	 * the source character and the target string are case folded. For example:
	 * <p>
	 * # ( ø → ℴ̸ ) LATIN SMALL LETTER O WITH STROKE → SCRIPT SMALL O, COMBINING
	 * LONG SOLIDUS OVERLAY # →o̷→
	 */

	Confusables SINGLE_SCRIPT_LOWER_CASE = new SingleScriptLowerCase();
	/**
	 * This table is used to test cases of single-script confusables, where the
	 * output allows for mixed case (which may be later folded away). For
	 * example, this table contains the following entry not found in SL:
	 * <p>
	 * # ( O → 0 ) LATIN CAPITAL LETTER O → DIGIT ZERO
	 */
	Confusables SINGLE_SCRIPT_ANY_CASE = new SingleScriptAnyCase();

	/**
	 * This table is used to test cases of mixed-script and whole-script
	 * confusables, where both the source character and the target string are
	 * case folded. For example, this table contains the following entry not
	 * found in SL or SA:
	 * <p>
	 * # ( μ → 𝛍 ) GREEK SMALL LETTER MU → MATHEMATICAL BOLD SMALL MU
	 */
	Confusables MIXED_SCRIPT_LOWER_CASE = new MixedScriptLowerCase();
	/**
	 * This table is used to test cases of mixed-script and whole-script
	 * confusables, where the output allows for mixed case (which may be later
	 * folded away). For example, this table contains the following entry not
	 * found in SL, SA, or ML:
	 * <p>
	 * # ( Ι → l ) GREEK CAPITAL LETTER IOTA → LATIN SMALL LETTER L
	 */
	Confusables MIXED_SCRIPT_ANY_CASE = new MixedScriptAnyCase();

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
