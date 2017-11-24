package kr.gaion.ceh.restapi.bean;

import java.io.Serializable;
import org.apache.spark.ml.linalg.Vector;

public class LabeledData implements Serializable {
	private static final long serialVersionUID = 6847391030863644368L;

	private Double label;
	private Vector features;

	public Double getLabel() {
		return label;
	}

	public void setLabel(Double label) {
		this.label = label;
	}

	public Vector getFeatures() {
		return features;
	}

	public void setFeatures(Vector features) {
		this.features = features;
	}
}