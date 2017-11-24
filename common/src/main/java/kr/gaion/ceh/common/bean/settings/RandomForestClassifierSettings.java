package kr.gaion.ceh.common.bean.settings;

public class RandomForestClassifierSettings extends SupervisedAlgorithm {

	/*
	 * Constants of class
	 */
	public static final String NUMCLASSES = "numClasses";
	public static final String NUMTREES = "numTrees";
	public static final String FEATURE_SUBSET_STRATEGY = "featureSubsetStrategy";
	public static final String IMPURITY = "impurity";
	public static final String MAXDEPTHS = "maxDepths";
	public static final String MAXBINS = "maxBins";
	public static final String ALGORITHM_NAME = "RandomForestClassifier";

	/*
	 * default values
	 */
	public static final int DEFAULT_NUMBER_TREES = 6;
	public static final int DEFAULT_MAXDEPTHS = 8;
	public static final int DEFAULT_MAXBINS = 100;
	public static final String DEFAULT_IMPURITY = "gini";
	public static final String DEFAULT_FEATURE_SUBSET_STRATEGY = "auto";

	/**
	 * Number of classes for classification
	 */
	private int numClasses;

	/**
	 * Number of trees in the random forest.
	 */
	private int numTrees;

	/**
	 * Number of features to consider for splits at each node. Supported values:
	 * "auto", "all", "sqrt", "log2", "onethird". If "auto" is set, this parameter
	 * is set based on numTrees: if numTrees == 1, set to "all"; if numTrees is
	 * greater than 1 (forest) set to "sqrt".
	 */
	private String featureSubsetStrategy;

	/**
	 * Criterion used for information gain calculation. Supported values: "gini"
	 * (recommended) or "entropy".
	 */
	private String impurity;

	/**
	 * Maximum depth of the tree (e.g. depth 0 means 1 leaf node, depth 1 means 1
	 * internal node + 2 leaf nodes). (suggested value: 4)
	 */
	private int maxDepths;

	/**
	 * Maximum number of bins used for splitting features (suggested value: 100)
	 */
	private int maxBins;

	/**
	 * setters (if values were not set, the default values would be set)
	 */
	public void setNumClasses(int numClasses) {
		this.numClasses = (numClasses == 0 ? 2 : numClasses);
	}

	public void setNumTrees(int numTrees) {
		this.numTrees = (numTrees == 0 ? 5 : numTrees);
	}

	public void setFeatureSubsetStrategy(String featureSubsetStrategy) {
		this.featureSubsetStrategy = (featureSubsetStrategy == null ? "auto" : featureSubsetStrategy);
	}

	public void setImpurity(String impurity) {
		this.impurity = (impurity == null ? "gini" : impurity);
	}

	public void setMaxDepths(int maxDepth) {
		this.maxDepths = (maxDepth == 0 ? 4 : maxDepth);
	}

	public void setMaxBins(int maxBins) {
		this.maxBins = (maxBins == 0 ? 100 : maxBins);
	}

	/**
	 * getters
	 */
	public int getNumClasses() {
		return this.numClasses;
	}

	public int getNumTrees() {
		if (this.numTrees == 0) {
			return DEFAULT_NUMBER_TREES;
		} else
			return this.numTrees;
	}

	public String getFeatureSubsetStrategy() {
		if (this.featureSubsetStrategy == null || this.featureSubsetStrategy.isEmpty()) {
			return DEFAULT_FEATURE_SUBSET_STRATEGY;
		} else
			return this.featureSubsetStrategy;
	}

	public String getImpurity() {
		if (this.impurity == null || this.impurity.isEmpty()) {
			return DEFAULT_IMPURITY;
		} else
			return this.impurity;
	}

	public int getMaxDepths() {
		if (this.maxDepths == 0) {
			return DEFAULT_MAXDEPTHS;
		} else
			return this.maxDepths;
	}

	public int getMaxBins() {
		if (this.maxBins == 0) {
			return DEFAULT_MAXBINS;
		} else
			return this.maxBins;
	}
}
