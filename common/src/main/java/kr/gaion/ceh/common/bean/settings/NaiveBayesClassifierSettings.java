package kr.gaion.ceh.common.bean.settings;

public class NaiveBayesClassifierSettings extends SupervisedAlgorithm {
	public final static String ALGORITHM_NAME = "NaiveBayesClassifier";
	public final static String LAMBDA = "lambda";

	/**
	 * default lambda value
	 */
	public static final double DEFAULT_LAMBDA = 1.0;

	/**
	 * lambda: The smoothing parameter
	 */
	private double lambda;

	public double getLambda() {
		if (lambda == 0) {
			return DEFAULT_LAMBDA;
		} else
			return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

}
