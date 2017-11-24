package kr.gaion.ceh.common.bean.settings;

public class SvmClassifierSettings extends SupervisedAlgorithm {

	/*
	 * Constants of class
	 */

	public static final String NUMBER_ITERATIONS = "numIterations";
	public static final String THRESHOLD = "threshold";
	public static final String ALGORITHM_NAME = "SvmClassifier";

	/**
	 * default values
	 */
	public static final int DEFAULT_ITERATION = 10;

	private int numIterations;

	private double threshold;

	/*
	 * Setters and Getters
	 */

	public void setNumIterations(int numIterations) {
		this.numIterations = numIterations;
	}

	public int getNumIterations() {
		if (numIterations == 0) {
			return DEFAULT_ITERATION;
		} else
			return this.numIterations;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

}
