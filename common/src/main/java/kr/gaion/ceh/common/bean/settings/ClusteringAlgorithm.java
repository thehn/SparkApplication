package kr.gaion.ceh.common.bean.settings;

public class ClusteringAlgorithm extends AlgorithmSettings {

	/**
	 * Class constants
	 */
	public final static String NUM_CLUSTERS = "numClusters";
	public final static String NUM_ITERATIONS = "numIterations";
	public final static String EPSILON = "epsilon";

	/*
	 * default values
	 */
	public static final int DEFAULT_ITERATION = 10;
	public static final double DEFAULT_EPSILON = 0.5;

	protected int numClusters;
	protected int numIterations;
	double epsilon;

	public double getEpsilon() {
		if (this.epsilon == 0) {
			return DEFAULT_EPSILON;
		} else
			return epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	public int getNumIterations() {
		if (this.numIterations == 0) {
			return DEFAULT_ITERATION;
		} else
			return numIterations;
	}

	public void setNumIterations(int numIterations) {
		this.numIterations = numIterations;
	}

	public int getNumClusters() {
		return numClusters;
	}

	public void setNumClusters(int numClusters) {
		this.numClusters = numClusters;
	}
}
