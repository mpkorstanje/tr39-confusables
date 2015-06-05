package com.github.mpkorstanje.unicode.tr39confusables;

import static java.text.Normalizer.normalize;
import static java.text.Normalizer.Form.NFD;
import static org.junit.Assert.*;
import static com.github.mpkorstanje.unicode.tr39confusables.Confusables.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ConfusablesTest {

	@Test
	public void mixedScriptAnyCase() {
		// * # ( Ι → l ) GREEK CAPITAL LETTER IOTA → LATIN SMALL LETTER L
		assertConfusable(MIXED_SCRIPT_ANY_CASE, "Ι", "l");
	}

	@Test
	public void singleScriptLowerCase() {
		// # ( ø → o̷ ) LATIN SMALL LETTER O WITH STROKE → LATIN SMALL LETTER O,
		// COMBINING SHORT SOLIDUS OVERLAY
//		test(SINGLE_SCRIPT_LOWER_CASE, "ø", "" + '\u2134' + '\u0338');
		assertConfusable(SINGLE_SCRIPT_LOWER_CASE, "ø", "ℴ̸".trim());
	}

	@Test
	public void singleScriptAnyCase() {
		// # ( O → 0 ) LATIN CAPITAL LETTER O → DIGIT ZERO
		assertConfusable(SINGLE_SCRIPT_ANY_CASE, "O", "0");
	}
	@Test
	public void mixedScriptLowerCase() {
		 // # ( μ → 𝛍 ) GREEK SMALL LETTER MU → MATHEMATICAL BOLD SMALL MU
		assertConfusable(MIXED_SCRIPT_LOWER_CASE, "μ", "𝛍");
	}

	private static void assertConfusable(Confusables table, String source, String target) {
		assertTrue(source.length() == 1);
		source = normalize(source, NFD);
		target = normalize(target, NFD);

		assertArrayEquals(codePoints(target), table.get(source.codePointAt(0)));
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

}
