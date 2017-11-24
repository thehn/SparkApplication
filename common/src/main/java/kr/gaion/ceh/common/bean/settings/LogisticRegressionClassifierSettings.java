package kr.gaion.ceh.common.bean.settings;

/**
 * 
 * @author hoang
 *
 */
public class LogisticRegressionClassifierSettings extends SupervisedAlgorithm {

	public static final String ALGORITHM_NAME = "LogisticRegressionClassifier";
	public static final String NUMBER_ITERATIONS = "numIterations";
	public static final String THRESHOLD = "threshold";
	public static final String REG_PARAM = "regParam";
	public static final String INTERCEPT = "intercept";
	public static final String ELASTIC_NET_MIXING = "alpha";

	/**
	 * default values
	 */
	public static final int DEFAULT_ITERATION = 10;
	public static final double DEFAULT_REG_PARAM = 0.0;

	/**
	 * Set the ElasticNet mixing parameter. For alpha = 0, the penalty is an L2
	 * penalty. For alpha = 1, it is an L1 penalty. For alpha in (0,1), the penalty
	 * is a combination of L1 and L2. Default is 0.0 which is an L2 penalty.
	 * 
	 * Note: Fitting under bound constrained optimization only supports L2
	 * regularization, so throws exception if this param is non-zero value.
	 */
	private double alpha;
	private int numIterations;
	private double threshold;
	private double regParam; // regularization parameter
	private boolean intercept = true; // default is true

	/**
	 * @return the alpha
	 */
	public double getAlpha() {
		return alpha;
	}

	/**
	 * @param alpha
	 *            the alpha to set
	 */
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	/**
	 * @return the numIterations
	 */
	public int getNumIterations() {
		if (numIterations == 0) {
			return DEFAULT_ITERATION;
		} else
			return this.numIterations;
	}

	/**
	 * @param numIterations
	 *            the numIterations to set
	 */
	public void setNumIterations(int numIterations) {
		this.numIterations = numIterations;
	}

	/**
	 * @return the regParam
	 */
	public double getRegParam() {
		if (regParam == 0) {
			return DEFAULT_REG_PARAM;
		} else
			return this.regParam;
	}

	/**
	 * @param regParam
	 *            the regParam to set
	 */
	public void setRegParam(double regParam) {
		this.regParam = regParam;
	}

	/**
	 * @return the threshold
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold
	 *            the threshold to set
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	/**
	 * @return the intercept
	 */
	public boolean getIntercept() {
		return intercept;
	}

	/**
	 * @param intercept
	 *            the intercept to set
	 */
	public void setIntercept(boolean intercept) {
		this.intercept = intercept;
	}
}
