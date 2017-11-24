package kr.gaion.ceh.web.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.settings.PredictionInfo;
import kr.gaion.ceh.common.util.Utilities;

/**
 * to perform Input/Output streaming
 * 
 * @author hoang
 *
 */
public class FileOperations {

	final static Logger logger = LoggerFactory.getLogger(FileOperations.class);

	/**
	 * to write predicted data to CSV file
	 * 
	 * @param listPredictionInfo
	 * @param fileNameWithoutExtension
	 * @return
	 * @throws IOException
	 */
	public static String savePredictedInfoToCSV(List<PredictionInfo<?, ?>> listPredictionInfo,
			String fileNameWithoutExtension) throws IOException {
		StringBuilder dataBuilder = new StringBuilder();
		StringBuilder jsonElementBuilder = null;
		String lineSeparator = System.lineSeparator();
		String csvSeparator = Constants.CSV_SEPARATOR;
		String fileName = fileNameWithoutExtension + Constants.CSV_EXTENSION;
		String fileOutput = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, fileName);
		Gson gson = new Gson();
		JsonObject jsonObj = null;

		for (PredictionInfo<?, ?> predictInfo : listPredictionInfo) {
			dataBuilder.append(predictInfo.getPredictedValue());
			dataBuilder.append(csvSeparator);
			jsonObj = gson.fromJson(predictInfo.getFeatures().toString(), JsonObject.class);
			JsonElement values = jsonObj.get("values");
			// to remove square brackets [] at index 0 and length of string
			jsonElementBuilder = new StringBuilder(values.toString());
			jsonElementBuilder.deleteCharAt(0);
			jsonElementBuilder.deleteCharAt(jsonElementBuilder.length() - 1);
			dataBuilder.append(jsonElementBuilder);
			dataBuilder.append(lineSeparator);
		}

		if (checkFile(fileOutput)) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileOutput));
			writer.write(dataBuilder.toString());
			writer.close();
		} else {
			logger.info("Target file is existed and currently locked");
			return null;
		}

		return fileOutput;
	}

	/**
	 * to check if file is locked or not
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean checkFile(String filePath) {

		try {
			FileUtils.touch(new File(filePath));
		} catch (IOException e) {
			return false;
			// TODO: handle exception
		}

		return true;
	}

}
