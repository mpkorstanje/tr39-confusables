package com.github.mpkorstanje.unicode.tr39confusables;

import static com.github.mpkorstanje.unicode.tr39confusables.Confusables.MIXED_SCRIPT_ANY_CASE;



import com.google.caliper.Benchmark;
import com.google.caliper.Param;
import com.google.caliper.runner.CaliperMain;

import static java.text.Normalizer.normalize;
import static java.text.Normalizer.Form.NFD;



@SuppressWarnings("javadoc")
public class SkeletonCaliper {


	public static void main(String[] args) {
		CaliperMain.main(SkeletonCaliper.class, args);
	}

	@Param
	Value b;
	enum Value {
		Paypal_1("ρ⍺у𝓅𝒂ן"),Paypal_2("𝔭𝒶ỿ𝕡𝕒ℓ"),Paypal_3("paypal");

		final String s;

		Value(String s) {
			this.s = s;
		}
	}
	
	@Benchmark
	float latest(int reps) {
		String s = b.s;
		float dummy = 0;
		for (int i = 0; i < reps; i++) {
			dummy += Skeleton.skeleton(s).hashCode();
		}
		return dummy;
	}
	
	@Benchmark
	float v_0_5_0(int reps) {
		String s = b.s;
		float dummy = 0;
		for (int i = 0; i < reps; i++) {
			dummy += Skeleton_0_5_0.skeleton(s).hashCode();
		}
		return dummy;
	}
	
	private static class Skeleton_0_5_0 {

		public static String skeleton(String s) {
			return skeleton(s, MIXED_SCRIPT_ANY_CASE);
		}


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
		
		public Skeleton_0_5_0(String s) {
			this(s, MIXED_SCRIPT_ANY_CASE);
		}

		public Skeleton_0_5_0(String s, Confusables confusables) {
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
			Skeleton_0_5_0 other = (Skeleton_0_5_0) obj;

			return skeleton.equals(other.skeleton);
		}

		@Override
		public int hashCode() {
			return skeleton.hashCode();
		}

		@Override
		public String toString() {
			return skeleton;
		}

	}
}
