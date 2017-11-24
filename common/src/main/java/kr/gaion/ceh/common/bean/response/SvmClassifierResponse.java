package kr.gaion.ceh.common.bean.response;

import java.util.List;

import kr.gaion.ceh.common.bean.settings.PredictionInfo;

public class SvmClassifierResponse extends ResponseBase {

	/*
	 * Class constants
	 */
	public static final String CONFUSION_MATRIX = "confusionMatrix";
	public static final String RECALL_BY_THRESHOLD = "recallByThreshold";
	public static final String F1_SCORE_BY_THRESHOLD = "f1ScoreByThreshold";
	public static final String F2_SCORE_BY_THRESHOLD = "f2ScoreByThreshold";
	public static final String ROC_CURVE_BY_THRESHOLD = "rocByThresholds";
	public static final String AREA_UNDER_ROC = "areaUnderRoc";
	public static final String AREA_UNDER_PRECISION_RECALL_CURVE = "areaUnderPrecisionRecallCurve";
	public static final String PRECISION_BY_THRESHOLD = "precisionByThreshold";
	public static final String PRECISION_RECALL_CURVE = "precisionRecallCurve";
	public static final String PREDICTION_INFOR = "predictionInfo";
	public static final String PREDICTED_ACTUAL_FEATURE_INFO = "predictedActualFeatureLine"; // #PC0002
	public static final String PREDICTED_FEATURE_INFO = "predictedFeatureLine"; // #PC0002
	public static final String LIST_FEATURES = "listFeatures"; // #PC0002
	public static final String LIST_THRESHOLD = "thresholds";
	public static final String LABELS = "labels";

	public static final String WEIGHTED_FALSE_POSITIVE = "weightedFalsePositiveRate";
	public static final String WEIGHTED_F_MEASURE = "weightedFMeasure";
	public static final String ACCURACY = "accuracy";
	public static final String WEIGHTED_PRECISION = "weightedPrecision";
	public static final String WEIGHTED_RECALL = "weightedRecall";
	public static final String WEIGHTED_TRUE_POSISTIVE = "weightedTruePositiveRate";

	/**
	 * Constructor
	 * 
	 * @param type
	 */
	public SvmClassifierResponse(ResponseType type) {
		super(type);
	}

	/*
	 * Evaluation properties
	 */
	private List<?> rocByThresholds;
	private double areaUnderRoc;
	private double areaUnderPrecisionRecallCurve;
	private List<Double> thresholds;
	private double[] confusionMatrix;
	private double[] labels;
	private List<?> precisionByThreshold;
	private List<?> recallByThreshold;
	private List<?> f1ScoreByThreshold;
	private List<?> f2ScoreByThreshold;
	private List<?> precisionRecallCurve;
	private List<PredictionInfo<?, ?>> predictionInfo;

	/*
	 * Prediction properties
	 */
	private double weightedFalsePositiveRate;
	private double weightedFMeasure;
	private double accuracy;
	private double weightedPrecision;
	private double weightedRecall;
	private double weightedTruePositiveRate;
	
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
	 * Setters and Getters
	 */

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

	public String[] getListFeatures() {
		return listFeatures;
	}

	public void setListFeatures(String[] listFeatures) {
		this.listFeatures = listFeatures;
	}

	public List<?> getF1ScoreByThreshold() {
		return f1ScoreByThreshold;
	}

	public double[] getLabels() {
		return labels;
	}

	public void setLabels(double[] labels) {
		this.labels = labels;
	}

	public void setF1ScoreByThreshold(List<?> f1ScoreByThreshold) {
		this.f1ScoreByThreshold = f1ScoreByThreshold;
	}

	public double getAreaUnderRoc() {
		return areaUnderRoc;
	}

	public List<?> getRocByThresholds() {
		return rocByThresholds;
	}

	public void setRocByThresholds(List<?> rocByThresholds) {
		this.rocByThresholds = rocByThresholds;
	}

	public void setAreaUnderRoc(double areaUnderRoc) {
		this.areaUnderRoc = areaUnderRoc;
	}

	public double getAreaUnderPrecisionRecallCurve() {
		return areaUnderPrecisionRecallCurve;
	}

	public void setAreaUnderPrecisionRecallCurve(double areaUnderPrecisionRecallCurve) {
		this.areaUnderPrecisionRecallCurve = areaUnderPrecisionRecallCurve;
	}

	public double[] getConfusionMatrix() {
		return confusionMatrix;
	}

	public void setConfusionMatrix(double[] confusionMatrix) {
		this.confusionMatrix = confusionMatrix;
	}

	public List<?> getPrecisionByThreshold() {
		return precisionByThreshold;
	}

	public void setPrecisionByThreshold(List<?> precisionByThreshold) {
		this.precisionByThreshold = precisionByThreshold;
	}

	public List<?> getRecallByThreshold() {
		return recallByThreshold;
	}

	public void setRecallByThreshold(List<?> recallByThreshold) {
		this.recallByThreshold = recallByThreshold;
	}

	public List<?> getF2ScoreByThreshold() {
		return f2ScoreByThreshold;
	}

	public void setF2ScoreByThreshold(List<?> f2ScoreByThreshold) {
		this.f2ScoreByThreshold = f2ScoreByThreshold;
	}

	public List<?> getPrecisionRecallCurve() {
		return precisionRecallCurve;
	}

	public void setPrecisionRecallCurve(List<?> precisionRecallCurve) {
		this.precisionRecallCurve = precisionRecallCurve;
	}

	public List<PredictionInfo<?, ?>> getPredictionInfo() {
		return predictionInfo;
	}

	public void setPredictionInfo(List<PredictionInfo<?, ?>> predictionInfo) {
		this.predictionInfo = predictionInfo;
	}

	public List<Double> getThresholds() {
		return thresholds;
	}

	public void setThresholds(List<Double> thresholds) {
		this.thresholds = thresholds;
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