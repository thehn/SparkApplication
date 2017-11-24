package kr.gaion.ceh.common.bean.response;

import java.util.List;
import java.util.Map;

public class FpGrowthResponse extends ResponseBase {

	/*
	 * Class constants
	 */
	public static final String ASSOCIATION_RULES = "asRules";
	public static final String FREQUENT_PATTERNS = "freqPatterns";

	/*
	 * Class properties
	 */
	private List<Map<String, Object>> asRules;
	private List<Map<String, Object>> freqPatterns;

	/*
	 * Getters and Setters
	 */
	public List<Map<String, Object>> getAsRules() {
		return asRules;
	}

	public void setAsRules(List<Map<String, Object>> asRules) {
		this.asRules = asRules;
	}

	public List<Map<String, Object>> getFreqPatterns() {
		return freqPatterns;
	}

	public void setFreqPatterns(List<Map<String, Object>> freqPatterns) {
		this.freqPatterns = freqPatterns;
	}

	public FpGrowthResponse(ResponseType type) {
		super(type);
	}

}
