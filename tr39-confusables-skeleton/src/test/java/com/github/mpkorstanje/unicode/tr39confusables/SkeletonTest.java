package com.github.mpkorstanje.unicode.tr39confusables;

import org.junit.Test;

import static com.github.mpkorstanje.unicode.tr39confusables.Skeleton.skeleton;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@SuppressWarnings("javadoc")
public class SkeletonTest {
	
	@Test
	public void paypal(){
		assertSameSkeleton("ρ⍺у𝓅𝒂ן", "𝔭𝒶ỿ𝕡𝕒ℓ");
		assertSameSkeleton("paypal", "paypal");
	}
	@Test
	public void mixedCase(){
		// Case is not affected by skeleton function
		assertDifferentSkeleton("paypal", "payPal");
		assertSameSkeleton("payPal", "payPal");
	}
	@Test
	public void diacritics(){
		// Diacritics are affected not by skeleton function
		assertDifferentSkeleton("ÂȘȚÎ", "asti");
		assertSameSkeleton("ÂȘȚÎ", "ÂȘȚÎ");
	}
	
	
	@Test
	public void w(){
		assertSameSkeleton("𝖶", "W");
	}
	
	@Test
	public void scope(){
		assertSameSkeleton("scope", "scope");
	}
	
	@Test
	public void closeMidFrontRoundedVowel(){
		assertSameSkeleton("ø", "o̷");
	}

	@Test
	public void zero(){
		assertSameSkeleton("O", "0");
	}
	
	@Test
	public void nu(){
		assertSameSkeleton("ν", "v");
	}
	
	@Test
	public void iota(){
		assertSameSkeleton("Ι", "l");
	}
	
	
	private static void assertSameSkeleton(String a, String b) {
		assertThat("skeleton representations should be equal", skeleton(a),equalTo(skeleton(b)));
	}
	
	private static void assertDifferentSkeleton(String a, String b) {
		assertThat("skeleton representations should not be equal", skeleton(a),not(equalTo(skeleton(b))));
	}

}
