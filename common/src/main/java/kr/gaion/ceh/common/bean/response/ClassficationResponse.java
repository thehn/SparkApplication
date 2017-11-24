package kr.gaion.ceh.common.bean.response;

import java.util.List;

import kr.gaion.ceh.common.bean.settings.PredictionInfo;

public class ClassficationResponse extends ResponseBase {

	/*
	 * Class constants
	 */
	public static final String CONFUSION_MATRIX = "confusionMatrix";
	public static final String WEIGHTED_FALSE_POSITIVE = "weightedFalsePositiveRate";
	public static final String WEIGHTED_F_MEASURE = "weightedFMeasure";
	public static final String ACCURACY = "accuracy";
	public static final String WEIGHTED_PRECISION = "weightedPrecision";
	public static final String WEIGHTED_RECALL = "weightedRecall";
	public static final String WEIGHTED_TRUE_POSISTIVE = "weightedTruePositiveRate";
	public static final String PREDICTION_INFO = "predictionInfo";
	public static final String PREDICTED_ACTUAL_FEATURE_INFO = "predictedActualFeatureLine"; // #PC0002
	public static final String PREDICTED_FEATURE_INFO = "predictedFeatureLine"; // #PC0002
	public static final String LIST_FEATURES = "listFeatures"; // #PC0002
	public static final String LABELS = "labels";

	/**
	 * Constructor
	 * 
	 * @param type
	 */
	public ClassficationResponse(ResponseType type) {
		super(type);
	}

	/**
	 * Properties
	 */
	protected double[] confusionMatrix;
	protected double[] labels; // #PC0003
	// protected List<?> labels; #PC0003
	protected double weightedFalsePositiveRate;
	protected double weightedFMeasure;
	protected double accuracy;
	protected double weightedPrecision;
	protected double weightedRecall;
	protected double weightedTruePositiveRate;
	protected List<PredictionInfo<?, ?>> predictionInfo;

	/**
	 * each element of list is a line which represents for predicted label, actual
	 * label and the respective features
	 */
	protected List<String> predictedActualFeatureLine; // #PC0002

	/**
	 * each element of list is a line which represents for predicted label and the
	 * respective features
	 */
	protected List<String> predictedFeatureLine; // #PC0002
	protected String[] listFeatures; // #PC0002

	/*
	 * Getters and Setters
	 */

	public double[] getConfusionMatrix() {
		return confusionMatrix;
	}

	public String[] getListFeatures() {
		return listFeatures;
	}

	public void setListFeatures(String[] listFeatures) {
		this.listFeatures = listFeatures;
	}

	public List<String> getPredictedActualFeatureLine() {
		return predictedActualFeatureLine;
	}

	public void setPredictedActualFeatureLine(List<String> predictedActualFeatureLine) {
		this.predictedActualFeatureLine = predictedActualFeatureLine;
	}

	public List<String> getPredictedFeatureLine() {
		return predictedFeatureLine;
	}

	public void setPredictedFeatureLine(List<String> predictedFeatureLine) {
		this.predictedFeatureLine = predictedFeatureLine;
	}

	public double[] getLabels() {
		return labels;
	}

	public void setLabels(double[] labels) {
		this.labels = labels;
	}

	public List<PredictionInfo<?, ?>> getPredictionInfo() {
		return predictionInfo;
	}

	public void setPredictionInfo(List<PredictionInfo<?, ?>> predictionInfo) {
		this.predictionInfo = predictionInfo;
	}

	public void setConfusionMatrix(double[] confusionMatrix) {
		this.confusionMatrix = confusionMatrix;
	}

	public double getWeightedFalsePositiveRate() {
		return weightedFalsePositiveRate;
	}

	public void setWeightedFalsePositiveRate(double weightedFalsePositiveRate) {
		this.weightedFalsePositiveRate = weightedFalsePositiveRate;
	}

	public double getWeightedFMeasure() {
		return weightedFMeasure;
	}

	public void setWeightedFMeasure(double weightedFMeasure) {
		this.weightedFMeasure = weightedFMeasure;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public double getWeightedPrecision() {
		return weightedPrecision;
	}

	public void setWeightedPrecision(double weightedPrecision) {
		this.weightedPrecision = weightedPrecision;
	}

	public double getWeightedRecall() {
		return weightedRecall;
	}

	public void setWeightedRecall(double weightedRecall) {
		this.weightedRecall = weightedRecall;
	}

	public double getWeightedTruePositiveRate() {
		return weightedTruePositiveRate;
	}

	public void setWeightedTruePositiveRate(double weightedTruePositiveRate) {
		this.weightedTruePositiveRate = weightedTruePositiveRate;
	}
}
