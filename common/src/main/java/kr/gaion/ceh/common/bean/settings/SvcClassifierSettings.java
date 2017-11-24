package kr.gaion.ceh.common.bean.settings;

/**
 * 
 * @author hoang
 *
 */
public class SvcClassifierSettings extends SupervisedAlgorithm{
	
	public final static String ALGORITHM_NAME = "SvcClassifier";
	public static final String NUMBER_ITERATIONS = "numIterations";
	public static final String THRESHOLD = "threshold";
	public static final String REG_PARAM = "regParam";
	public static final String INTERCEPT = "intercept";

	private int numIterations;
	private double threshold;
	private double regParam; // regularization parameter
	private boolean intercept = true; // default is true
	
	/**
	 * default values
	 */
	public static final int DEFAULT_ITERATION = 10;
	public static final double DEFAULT_REG_PARAM = 0.0;

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

	public double getRegParam() {
		if (regParam == 0) {
			return DEFAULT_REG_PARAM;
		} else
			return this.regParam;
	}

	public void setRegParam(double regParam) {
		this.regParam = regParam;
	}

	public boolean getIntercept() {
		return intercept;
	}

	public void setIntercept(boolean intercept) {
		this.intercept = intercept;
	}
	
}
