package kr.gaion.ceh.web.response_processing;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * 
 * @author Youngsung Son
 *
 */
public class PatternDataProcessing {

	private static Gson gson = new Gson();
	static StringBuilder strBuilder = new StringBuilder();

	/**
	 * 
	 * @param freqPattersResponse
	 * @return
	 */
	public String processedPattersData(List<Map<String, Object>> freqPattersResponse) {

		List<List<?>> sortedItems = sortItems(freqPattersResponse);
		String jsonResult = gson.toJson(sortedItems);

		return jsonResult;
	}

	/**Get items and freq from ES 
	 * 
	 * @param freqPattersResponse
	 * @return
	 */
	public static List<List<?>> sortItems(List<Map<String, Object>> freqPattersResponse) {

		List<List<?>> sortedItems = new ArrayList<>();

		for (Map<String, Object> item : freqPattersResponse) {

			List<String> tempList = new ArrayList<String>();

			@SuppressWarnings("unchecked")
			List<String> items = (List<String>) (item.get("items"));

			// List<String> freq = (List<String>) (item.get("freq"));
			String freq = item.get("freq").toString();

			List<String> trimedList = trimList(items, freq);
			tempList.addAll(trimedList);
			String[] itemsArray = tempList.toArray(new String[tempList.size()]);
			sortedItems.add(Arrays.asList(itemsArray));
		}

		return sortedItems;
	}

	/**Trim items and freq data from ES to match JSON format for visualization
	 * 
	 * @param items
	 * @param freq
	 * @return
	 */
	public static List<String> trimList(List<String> items, String freq) {
		
		List<List<?>> tempList1 = new ArrayList<>();
		List<String> tempList2 = new ArrayList<String>();
		
		//Ascending order and convert String type
		String[] sortedItems = sortNum(items);
		tempList1.add(Arrays.asList(sortedItems));
		String strItems = tempList1.toString();
		
		//Matching JSON format for FP Visualization(Sequence Sunburst)
		String trimedItems = strItems.replaceAll(", ", "-").replaceAll("\\[", "").replaceAll("\\]", "");

		tempList2.add(trimedItems);
		tempList2.add(freq);

		return tempList2;
	}
	
	/**Sort items and append from freqPattersResponse data Ascending order
	 * it needs for gradation in FP visualization
	 * 
	 * @param listOfItems
	 * @return
	 */
	public static String[] sortNum(List<String> listOfItems) {

		int aryLength = listOfItems.size();
		int[] temp;
		temp = new int[aryLength];
		int index = 0;

		for (String item : listOfItems) {
			temp[index++] = Integer.parseInt(item);
		}

		Arrays.sort(temp);

		String[] tempArray = new String[aryLength];

		index = 0;
		for (int item : temp) {
			tempArray[index++] = Integer.toString(item);
		}

		return tempArray;
	}
}
