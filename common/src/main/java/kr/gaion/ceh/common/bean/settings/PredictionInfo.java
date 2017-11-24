package kr.gaion.ceh.common.bean.settings;

/**
 * To save information of prediction
 * 
 * @author hoang
 *
 * @param <T>
 */
public class PredictionInfo<T, V> {

	/*
	 * Class properties
	 */
	protected T predictedValue;
	protected T actualValue;
	protected V features;

	/*
	 * Setters and Getters
	 */
	public T getPredictedValue() {
		return predictedValue;
	}

	public void setPredictedValue(T predictedValue) {
		this.predictedValue = predictedValue;
	}

	public T getActualValue() {
		return actualValue;
	}

	public void setActualValue(T actualValue) {
		this.actualValue = actualValue;
	}

	public V getFeatures() {
		return features;
	}

	public void setFeatures(V features) {
		this.features = features;
	}

}
