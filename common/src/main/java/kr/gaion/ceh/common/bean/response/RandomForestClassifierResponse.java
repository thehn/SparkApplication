package kr.gaion.ceh.common.bean.response;

/**
 * to save all response data for Random Forest Classifier algorithm
 * 
 * @author hoang
 *
 */
public class RandomForestClassifierResponse extends ClassficationResponse {

	/*
	 * class constants
	 */
	public static final String DECISION_TREE = "decisionTree";

	/*
	 * class properties
	 */
	protected String decisionTree;

	public RandomForestClassifierResponse(ResponseType type) {
		super(type);
	}

	public String getDecisionTree() {
		return decisionTree;
	}

	public void setDecisionTree(String decisionTree) {
		this.decisionTree = decisionTree;
	}
}
