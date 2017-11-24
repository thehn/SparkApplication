package kr.gaion.ceh.restapi.data.processing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * to process Basket data
 * 
 * @author hoang
 *
 */
public class BasketDataProcessor {

	final static Logger logger = LoggerFactory.getLogger(BasketDataProcessor.class);

	/**
	 * This data structure simulates: Customer_ID=>[Categories_list]
	 */
	private HashMap<String, SortedSet<String>> processedData;

	/*
	 * Constructor
	 */
	public BasketDataProcessor() {
		this.processedData = new HashMap<>();

	}

	/*
	 * Getter
	 */
	public HashMap<String, SortedSet<String>> getProcessedData() {
		return this.processedData;
	}

	/*
	 * Setter
	 */
	public void add(String _id, String _category) {
		SortedSet<String> hs_temp = new TreeSet<String>();
		SortedSet<String> reference = null;
		hs_temp.add(_category);
		if (this.processedData.containsKey(_id)) {
			reference = this.processedData.get(_id);
			reference.addAll(hs_temp);
		} else {
			this.processedData.put(_id, hs_temp);
		}
	}

	/**
	 * This function perform data processing, then save to file
	 * 
	 * @param filePath
	 * @param outputFile
	 * @throws IOException
	 */
	public void processDataFromFile(String filePath, String outputFile, String delimiter, int indexID,
			int indexCategory) throws IOException {
		logger.info("Start: processDataFromFile");
		long startTime = System.currentTimeMillis();
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = null;
		String[] arr = null;
		while ((line = br.readLine()) != null) {
			// process the line
			arr = line.split(delimiter);
			this.add(arr[indexID], arr[indexCategory]);
		}
		br.close();
		this.writeProcessedDataToFile(outputFile, delimiter);
		logger.info("Elapsed time: " + (System.currentTimeMillis() - startTime));
	}

	/**
	 * 
	 * @param outputFile
	 * @throws IOException
	 */
	public void writeProcessedDataToFile(String outputFile, String delimiter) throws IOException {
		logger.info("Start: writeProcessedDataToFile");
		long startTime = System.currentTimeMillis();
		BufferedWriter buff = null;
		StringBuilder strbuilder = new StringBuilder();
		buff = new BufferedWriter(new FileWriter(outputFile));

		for (SortedSet<String> entry : this.processedData.values()) {

			for (String s : entry) {
				strbuilder.append(s);
				strbuilder.append(delimiter);
			}
			strbuilder.deleteCharAt(strbuilder.length() - 1);
			buff.write(strbuilder.toString());
			strbuilder.setLength(0);
			//
			buff.newLine();
		}
		buff.close();
		logger.info("Elapsed time: " + (System.currentTimeMillis() - startTime));
	}

}
