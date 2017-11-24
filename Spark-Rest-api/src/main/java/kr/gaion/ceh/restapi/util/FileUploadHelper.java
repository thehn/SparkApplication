package kr.gaion.ceh.restapi.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.gaion.ceh.common.util.Utilities;

public class FileUploadHelper {
	
	private final static Logger logger = LoggerFactory.getLogger(FileUploadHelper.class);
	
	public static Map<String, ?> receiveDataFromClient(byte[] data) throws Exception {
		// Creating the directory to store file
		logger.info("Creating the directory to store file");
		File sparkFile = Utilities.makeTempleFile();
		String fileFullPath = sparkFile.getAbsolutePath();

		logger.info("Wrting file: " + fileFullPath);
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(sparkFile));
		stream.write(data);
		stream.close();
		
		// listUploadedFiles.add(sparkFile.getPath());
		Map<String, Object> returnInfo = new HashMap<>();
		returnInfo.put("fileName", fileFullPath);
		
		// read information of fields
		// #P00005 - Start
		/*JavaRDD<String> dataRdd = SparkEsConnector.getJavaSparkContext().textFile(fileFullPath);
		String header = dataRdd.first();
		returnInfo.put("header", header);*/
		// #P00005 - End
		
		return returnInfo;
	}
}
