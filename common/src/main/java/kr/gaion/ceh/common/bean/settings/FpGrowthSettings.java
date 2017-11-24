package kr.gaion.ceh.common.bean.settings;

/**
 * This class use to save all setting values of Spark ML FP-Growth application
 * </br>
 * Extended from SparkAppSettings
 * 
 * @author hoang
 *
 */
public class FpGrowthSettings extends AlgorithmSettings {

	/**
	 * Class Constants
	 */
	public static final String MIN_SUPPORT = "minSupport";
	public static final String ALGORITHM_NAME = "FpGrowth";
	public static final String MIN_CONFIDENCE = "minConfidence";
	public static final String NUMBER_PARTITION = "numberPartition";

	/*
	 * Default values
	 */
	public final static int DEFAULT_NUMBER_PARTITION = 10;

	/**
	 * Properties
	 */
	protected double minSupport;
	protected double minConfidence;
	protected int numberPartition;

	/**
	 * Getter
	 */
	public void setMinSupport(double minSupport) {
		this.minSupport = minSupport;
	}

	public void setMinConfidence(double minConfidence) {
		this.minConfidence = minConfidence;
	}

	public void setNumberPartition(int numberPartition) {
		this.numberPartition = numberPartition;
	}

	/**
	 * Setter
	 */
	public double getMinSupport() {
		return this.minSupport;
	}

	public double getMinConfidence() {
		return this.minConfidence;
	}

	public int getNumberPartition() {
		if (this.numberPartition == 0) {
			return DEFAULT_NUMBER_PARTITION;
		} else
			return this.numberPartition;
	}
}
