package kr.gaion.ceh.web.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.bean.settings.DataLookupDefinition;
import kr.gaion.ceh.web.model.EsWebConnector;

/**
 * Control the Lookup Definition
 * 
 * @author hoang
 *
 */
@Controller
public class LookupDataController {

	private static Gson gson = new Gson();

	/**
	 * Lookup data (Load once)
	 */
	private static JsonArray arrLookupData = null;
	/**
	 * list lookup name
	 */
	private static List<String> listLookupName = new ArrayList<String>();

	static {
		// get file name full path
		String pathName = BaseController.webConfig.getSetting(Constants.CONF_DATA_LOOKUP_DEFINITION);
		String data = null;
		try {
			data = new String(Files.readAllBytes(Paths.get(pathName)), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		arrLookupData = gson.fromJson(data, JsonArray.class);

		for (JsonElement element : arrLookupData) {
			String lookupName = element.getAsJsonObject().get("lookupName").getAsString();
			listLookupName.add(lookupName);
		}
	}

	/**
	 * to apply all settings from Lookup definition to configuration instance
	 * 
	 * @param indexOfLookup
	 * @param config
	 */
	public static void applyLookupForSettings(int indexOfLookup, AlgorithmSettings config) {
		JsonObject jsonObj = arrLookupData.get(indexOfLookup).getAsJsonObject();
		DataLookupDefinition.applySettings(jsonObj, config);
	}

	/**
	 * to add new lookup definition in synchronized way
	 * 
	 * @param jsonObj
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("resource")
	private static synchronized void addNewLookupDefinition(JsonObject jsonObj)
			throws FileNotFoundException, IOException {

		// add to array of JSON objects
		arrLookupData.add(jsonObj);

		// update list of lookup name
		String lookupName = jsonObj.getAsJsonObject().get("lookupName").getAsString();
		if (listLookupName.contains(lookupName)) {
			// do not add
		} else {
			listLookupName.add(lookupName);
		}

		// update to file
		String pathName = BaseController.webConfig.getSetting(Constants.CONF_DATA_LOOKUP_DEFINITION);
		gson.newJsonWriter(new PrintWriter(new File(pathName))).jsonValue(gson.toJson(arrLookupData)).flush();
	}

	/**
	 * to replace old definition by new one
	 * 
	 * @param lookupName
	 * @param jsonObj
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("resource")
	private static synchronized void replaceLookupDefinition(String lookupName, JsonObject jsonObj)
			throws FileNotFoundException, IOException {

		int targetIndex = listLookupName.indexOf(lookupName);

		// replace data
		arrLookupData.set(targetIndex, jsonObj);

		// update to file
		String pathName = BaseController.webConfig.getSetting(Constants.CONF_DATA_LOOKUP_DEFINITION);
		gson.newJsonWriter(new PrintWriter(new File(pathName))).jsonValue(gson.toJson(arrLookupData)).flush();
	}

	/**
	 * to delete existed lookup definition
	 * 
	 * @param lookupName
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("resource")
	private static synchronized void deleteLookupDefinition(String lookupName)
			throws FileNotFoundException, IOException {

		int targetIndex = listLookupName.indexOf(lookupName);

		// delete from array of JSON objects
		arrLookupData.remove(targetIndex);

		// delete from list of Lookup name
		listLookupName.remove(targetIndex);

		// update to file
		String pathName = BaseController.webConfig.getSetting(Constants.CONF_DATA_LOOKUP_DEFINITION);
		gson.newJsonWriter(new PrintWriter(new File(pathName))).jsonValue(gson.toJson(arrLookupData)).flush();

	}

	/**
	 * to get Data Lookup definition
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getLookupDef", method = RequestMethod.GET)
	@ResponseBody
	public String getLookupDef() throws IOException {

		return arrLookupData.toString();
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/requestDeleteLookup", method = RequestMethod.POST)
	@ResponseBody
	public String requestDeleteLookup(@ModelAttribute("lookupSettings") DataLookupDefinition lookupSettings,
			BindingResult result) throws IOException {

		// delete old data
		deleteLookupDefinition(lookupSettings.getLookupName());

		return "true";
	}

	/**
	 * to add new or edit existed lookup definition
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/requestEditLookup", method = RequestMethod.POST)
	@ResponseBody
	public String requestEditLookup(@ModelAttribute("lookupSettings") DataLookupDefinition lookupSettings,
			BindingResult result) throws IOException {

		String jsonData = gson.toJson(lookupSettings);
		String action = lookupSettings.getAction();

		switch (action) {
		case DataLookupDefinition.ACTION_EDIT: {
			replaceLookupDefinition(lookupSettings.getLookupName(), lookupSettings.toJsonObject());
			break;
		}
		case DataLookupDefinition.ACTION_ADD: {
			addNewLookupDefinition(lookupSettings.toJsonObject());
			break;
		}
		default: {
			// do nothing
			break;
		}
		}

		return jsonData;
	}

	/**
	 * to check if the "lookup name" is existed or not
	 * 
	 * @param lookupName
	 * @return
	 */
	@RequestMapping(value = "/isValidLookupName/{lookupName}")
	@ResponseBody
	public Boolean isValidLookupName(@PathVariable("lookupName") String lookupName) {
		if (listLookupName.contains(lookupName)) {
			return Boolean.FALSE;
		} else
			return Boolean.TRUE;
	}

	/**
	 * to return list of Lookup definition and its index
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getListLookupDefs")
	@ResponseBody
	public List<?> getListLookupDefs() {

		List<Map<String, ?>> result = new ArrayList<>();
		Map<String, Object> m = null;
		Integer i = new Integer(0);
		for (String lookupName : listLookupName) {
			m = new HashMap<>();
			m.put("id", i);
			i = new Integer(i.intValue() + 1);
			m.put("text", lookupName);
			result.add(m);
		}
		return result;
	}

	/**
	 * to get all header fields of data
	 * 
	 * @param lookupIndex
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "getHeaderFields/{indexOfLookup}")
	@ResponseBody
	public List<?> getHeaderFields(@PathVariable("indexOfLookup") int indexOfLookup) throws IOException {
		
		JsonObject jsonObj = arrLookupData.get(indexOfLookup).getAsJsonObject();
		String index = jsonObj.getAsJsonObject().get("index").getAsString();
		String type = jsonObj.getAsJsonObject().get("type").getAsString();
		// List<String> fieldsList = getListFields(index, type);
		
		// #PC0001 : Start
		SearchResponse srp = EsWebConnector.getInstance(BaseController.webConfig).getClient().prepareSearch(index).setTypes(type + "_header").setSize(1).get();
		Map<String, Object> mapRsp = srp.getHits().getHits()[0].getSourceAsMap();
		String delimiter = mapRsp.get("delimiter").toString();
		String header = mapRsp.get("header").toString();
		String[] columnsList = header.split(delimiter);
		// #PC0001 : End
		
		List<Map<String, ?>> result = new ArrayList<>();
		Map<String, Object> m = null;
		for (String col : columnsList) {
			m = new HashMap<>();
			m.put("id", col);
			m.put("text", col);
			result.add(m);
		}
		return result;
	}

	/**
	 * to get all the fields of data from Elasticsearch (CSV formated data) This
	 * method is used to display header
	 * 
	 * @param index
	 * @param type
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "resource", "unchecked" })
	public static List<String> getListFields(String index, String type) throws IOException {
		List<String> fieldsList = new ArrayList<>();
		EsWebConnector esConn = EsWebConnector.getInstance(BaseController.webConfig);
		Client client = esConn.getClient();
		Map<String, Object> sourceMap = client.admin().cluster().prepareState().setIndices(index).execute().actionGet()
				.getState().getMetaData().index(index).mapping(type).getSourceAsMap();

		Map<String, Object> map = (Map<String, Object>) sourceMap.get("properties");
		Set<String> keys = map.keySet();
		for (String key : keys) {
			if (((Map<String, Object>) map.get(key)).containsKey("type")) {
				fieldsList.add(key);
			}
			// for nested fields (gess)
			/*
			 * else { List<String> tempList = getList(fieldName + "" + key + ".",
			 * (Map<String, Object>) map.get(key)); fieldList.addAll(tempList); }
			 */
		}

		return fieldsList;
	}
}
