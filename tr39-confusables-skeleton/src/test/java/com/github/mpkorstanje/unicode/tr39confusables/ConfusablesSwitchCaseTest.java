package com.github.mpkorstanje.unicode.tr39confusables;

import static java.text.Normalizer.normalize;
import static java.text.Normalizer.Form.NFD;
import static org.junit.Assert.*;
import static com.github.mpkorstanje.unicode.tr39confusables.Confusables.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ConfusablesSwitchCaseTest {

	@Test
	public void confusablesSwitchCaseTest() {
		// * # ( Ι → l ) GREEK CAPITAL LETTER IOTA → LATIN SMALL LETTER L
		String source = "Ι";
		String target = "l";
		assertTrue(source.length() == 1);
		source = normalize(source, NFD);
		target = normalize(target, NFD);

		assertArrayEquals(codePoints(target), new ConfusablesSwitchCase().get(source.codePointAt(0)));
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
