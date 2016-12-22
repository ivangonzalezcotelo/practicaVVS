package es.udc.pa.pa007.auctionhouse.test.model.quickcheck;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class MyCharacterGenerator extends Generator<String> {

	private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
	private static final String ALL_MY_CHARS = LOWERCASE_CHARS;
	public static final int CAPACITY = 6;

	public MyCharacterGenerator() {
		super(String.class);
	}

	@Override
	public String generate(SourceOfRandomness random, GenerationStatus status) {
		StringBuilder sb = new StringBuilder(CAPACITY);
		for (int i = 0; i < CAPACITY; i++) {
			int randomIndex = random.nextInt(ALL_MY_CHARS.length());
			sb.append(ALL_MY_CHARS.charAt(randomIndex));
		}
		return sb.toString();
	}

}