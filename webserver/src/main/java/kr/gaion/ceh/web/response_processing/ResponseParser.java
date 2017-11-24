package kr.gaion.ceh.web.response_processing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.settings.PredictionInfo;
import kr.gaion.ceh.common.interfaces.IResponseObject;

/**
 * Provide the utilities for parsing data from Response, to view on web model
 * 
 * @author hoang
 *
 */
public class ResponseParser {

	private static Gson gson = new Gson();

	/**
	 * to make coordinates (x-y) for one ore multiple line chart from list
	 * response
	 * 
	 * @param listResponse
	 * @param listCharts
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String[]> parseCoordinatesForMultipleLineChart(IResponseObject objResponse,
			String[] listCharts) {

		Map<String, String[]> mapCoordinates = new HashMap<>();
		for (String chart : listCharts) {
			List<Map<String, Object>> listResponse = (List<Map<String, Object>>) objResponse.getResponse(chart);
			StringBuilder dataBuilderXAxis = new StringBuilder();
			StringBuilder dataBuilderYAxis = new StringBuilder();

			dataBuilderXAxis.append("[");
			dataBuilderYAxis.append("[");

			for (Iterator<Map<String, Object>> it = listResponse.iterator(); it.hasNext();) {
				Map<String, Object> map = it.next();
				dataBuilderXAxis.append(map.get(Constants.KEY_XAXIS).toString());
				dataBuilderXAxis.append(",");
				dataBuilderYAxis.append(map.get(Constants.KEY_YAXIS).toString());
				dataBuilderYAxis.append(",");
			}
			dataBuilderXAxis.deleteCharAt(dataBuilderXAxis.length() - 1);
			dataBuilderYAxis.deleteCharAt(dataBuilderYAxis.length() - 1);
			dataBuilderXAxis.append("]");
			dataBuilderYAxis.append("]");
			mapCoordinates.put(chart, new String[] { dataBuilderXAxis.toString(), dataBuilderYAxis.toString() });
		}

		return mapCoordinates;
	}

	/**
	 * to parse Predicted, Actual class and Features
	 * 
	 * @param listPredictionInfo
	 * @return
	 */
	public static String parsePredictedActualFeatureValues(List<PredictionInfo<?, ?>> listPredictionInfo) {
		StringBuilder dataBuilder = new StringBuilder();
		StringBuilder jsonElementBuilder = null;
		JsonObject jsonObj = null;
		dataBuilder.append("[");
		for (PredictionInfo<?, ?> predictInfo : listPredictionInfo) {
			dataBuilder.append("[");
			dataBuilder.append(predictInfo.getPredictedValue());
			dataBuilder.append(",");
			dataBuilder.append(predictInfo.getActualValue());
			dataBuilder.append(",");
			jsonObj = gson.fromJson(predictInfo.getFeatures().toString(), JsonObject.class);
			JsonElement values = jsonObj.get("values");
			// to remove square brackets [] at index 0 and length of string
			jsonElementBuilder = new StringBuilder(values.toString());
			jsonElementBuilder.replace(0, 1, "\"");
			jsonElementBuilder.replace(jsonElementBuilder.length() - 1, jsonElementBuilder.length(), "\"");
			dataBuilder.append(jsonElementBuilder);
			dataBuilder.append("]");
			dataBuilder.append(",");
		}
		dataBuilder.deleteCharAt(dataBuilder.length() - 1);
		dataBuilder.append("]");
		return dataBuilder.toString();
	}

}
