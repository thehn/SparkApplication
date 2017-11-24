package kr.gaion.ceh.common.bean.settings;

import java.util.concurrent.ThreadLocalRandom;

public class SupervisedAlgorithm extends AlgorithmSettings {

	public static final String FRACTION = "fraction";
	public static final String SEED = "seed";
	public static final String INDEX_OF_LABELED_FIELD = "indexOfLabeledField";

	public final static double DEFAULT_FRACTION = 80.0;

	/**
	 * fraction must be [0, 100.0]
	 */
	protected double fraction;

	/**
	 * seed for randomly splitting data: training set & test set
	 */
	protected long seed;

	/*
	 * Getters and Setters
	 */
	public double getFraction() {
		if (fraction == 0) {
			return DEFAULT_FRACTION;
		} else
			return fraction;
	}

	public void setFraction(double fraction) {
		this.fraction = fraction;
	}

	public long getSeed() {
		if (seed == 0) {
			return ThreadLocalRandom.current().nextLong();
		} else
			return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

}
