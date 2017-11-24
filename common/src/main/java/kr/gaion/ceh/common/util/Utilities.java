package kr.gaion.ceh.common.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.interfaces.IConfigurable;

public class Utilities {

	/**
	 * To make URI for sending REST request to Spark server
	 * 
	 * @param config
	 * @param api
	 * @return
	 */
	public static String getUri(IConfigurable config, String api) {

		StringBuilder uriBuilder = new StringBuilder();
		String sparkIpAddress = config.getSetting(Constants.CONF_SPARK_HOST);
		String sparkPort = config.getSetting(Constants.CONF_SPARK_PORT);

		// make uri
		uriBuilder.append(Constants.PROPTOCOL);
		uriBuilder.append("://");
		uriBuilder.append(sparkIpAddress);
		uriBuilder.append(":");
		uriBuilder.append(sparkPort);
		uriBuilder.append("/");
		uriBuilder.append(api);

		return uriBuilder.toString();
	}

	/**
	 * To make URI for sending REST request to Spark server
	 * 
	 * @param config
	 * @return
	 */
	public static String getUri(IConfigurable config) {

		StringBuilder uriBuilder = new StringBuilder();
		String sparkIpAddress = config.getSetting(Constants.CONF_SPARK_HOST);
		String sparkPort = config.getSetting(Constants.CONF_SPARK_PORT);

		// make uri
		uriBuilder.append(Constants.PROPTOCOL);
		uriBuilder.append("://");
		uriBuilder.append(sparkIpAddress);
		uriBuilder.append(":");
		uriBuilder.append(sparkPort);
		uriBuilder.append("/");
		uriBuilder.append("ml-execute");
		return uriBuilder.toString();
	}

	/**
	 * To convert all field values from an object to a list of values
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> convertAllFieldsToList(Object obj) {
		ArrayList<T> list = new ArrayList<>();
		Field[] fields = obj.getClass().getDeclaredFields();

		for (Field f : fields) {
			try {
				list.add((T) f.get(obj));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * To get list of sub directories in specified directory
	 * 
	 * @param dir
	 * @return
	 */
	public static String[] getListFoldersInDir(String dir) {
		File file = new File(dir);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		return directories;
	}

	/**
	 * to make file path
	 * 
	 * @param strings
	 * @return
	 * @throws IOException
	 */
	public static String getPathInWorkingFolder(String... strings) throws IOException {

		String homeDir = System.getenv(Constants.SYS_ENV_CEH_HOME);
		if (homeDir == null) {
			throw new IOException("The environment CEH_HOME is not yet set.");
		} else {
			System.out.println("HOME_DIR: " + homeDir);
			// continue ..
		}

		StringBuilder pathBuilder = new StringBuilder(homeDir);
		char lastChar = pathBuilder.charAt(pathBuilder.length() - 1);

		// to check CEH_HOME path
		if (String.valueOf(lastChar) != File.separator) {
			pathBuilder.append(File.separator);
		}

		if (strings.length > 0) {
			for (String str : strings) {
				pathBuilder.append(str);
				pathBuilder.append(File.separator);
			}
			pathBuilder.deleteCharAt(pathBuilder.length() - 1);
		}
		return pathBuilder.toString();
	}

	/**
	 * to create temple File
	 * 
	 * @return
	 * @throws IOException
	 */
	public static File makeTempleFile() throws IOException {

		String rootPath = getPathInWorkingFolder(Constants.DATA_DIR, "tmp");
		File dir = new File(rootPath);

		if (!dir.exists())
			dir.mkdirs();

		// Create the temple file on Spark server
		File tmp = File.createTempFile("file_", ".tmp", dir);
		return tmp;
	}

	/**
	 * to round the double number
	 * 
	 * @param value
	 * @param places
	 * @return
	 */
	public static double roundDouble(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	/**
	 * to parse double
	 * 
	 * @param matrix
	 * @return
	 */
	/*
	 * public static double[] convertMatrix(Object matrix) { // remove redandant
	 * spaces String line = matrix.toString().replaceAll("\\s{2,}", " ").trim();
	 * double[] dValues = null; String[] values = line.split(" "); dValues = new
	 * double[values.length]; int i = 0; for (String value : values) { dValues[i++]
	 * = Double.parseDouble(value); }
	 * 
	 * return dValues; }
	 */

	/**
	 * to delete file or directory if it already existed
	 * 
	 * @param path
	 * @throws IOException
	 */
	public static void deleteIfExisted(String path) throws IOException {
		File fileOrDir = new File(path);
		if (fileOrDir.exists()) {
			if (fileOrDir.isFile()) {
				FileUtils.forceDelete(fileOrDir);
			} else if (fileOrDir.isDirectory()) {
				FileUtils.deleteDirectory(fileOrDir);
			} else {
				// continue
			}
		} else {
			// continue ..
		}
		/*
		 * File modelInfo = new File(path + ".info"); if (modelInfo.exists()) {
		 * FileUtils.forceDelete(modelInfo); } else { // continue .. }
		 */
	}

	/**
	 * To send current configuration and request for run to Spark-server
	 */
	/*
	 * public static void requestSparkServer(){ try { FileConfigLoader config =
	 * FileConfigLoader.getInstance(); String serverName =
	 * config.getSetting(Constants.SPARK_HOST); int port =
	 * Integer.parseInt(config.getSetting(Constants.SPARK_PORT)); Socket client =
	 * null; OutputStream outToServer = null; client = new Socket(serverName, port);
	 * // sending outToServer = client.getOutputStream();
	 * config.sendProperties(outToServer); client.close(); }catch( IOException e) {
	 * e.printStackTrace(); } }
	 */
}
