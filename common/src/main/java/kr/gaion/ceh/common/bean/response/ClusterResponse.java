package kr.gaion.ceh.common.bean.response;

import java.util.List;

import kr.gaion.ceh.common.bean.settings.PredictionInfo;

public class ClusterResponse extends ResponseBase {

	protected double cost;
	protected double wssse;
	protected int[] labels;
	protected List<double[]> centers;
	protected List<PredictionInfo<?, ?>> predictionInfo;
	protected int numberOfIdentifiedClusters;
	protected int numberOfNoisePoints;

	/**
	 * Constructor
	 * 
	 * @param type
	 */
	public ClusterResponse(ResponseType type) {
		super(type);
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getWssse() {
		return wssse;
	}

	public void setWssse(double wssse) {
		this.wssse = wssse;
	}

	public List<double[]> getCenters() {
		return centers;
	}

	public void setCenters(List<double[]> centers) {
		this.centers = centers;
	}

	public List<PredictionInfo<?, ?>> getPredictionInfo() {
		return predictionInfo;
	}

	public void setPredictionInfo(List<PredictionInfo<?, ?>> predictionInfo) {
		this.predictionInfo = predictionInfo;
	}

	public int[] getLabels() {
		return labels;
	}

	public void setLabels(int[] labels) {
		this.labels = labels;
	}

	public int getNumberOfIdentifiedClusters() {
		return numberOfIdentifiedClusters;
	}

	public void setNumberOfIdentifiedClusters(int numberOfIdentifiedClusters) {
		this.numberOfIdentifiedClusters = numberOfIdentifiedClusters;
	}

	public int getNumberOfNoisePoints() {
		return numberOfNoisePoints;
	}

	public void setNumberOfNoisePoints(int numberOfNoisePoints) {
		this.numberOfNoisePoints = numberOfNoisePoints;
	}

}
