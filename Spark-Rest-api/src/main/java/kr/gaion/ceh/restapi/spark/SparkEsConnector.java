package kr.gaion.ceh.restapi.spark;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DecimalType;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.bean.settings.EsIndexInfo;
import kr.gaion.ceh.common.bean.settings.SupervisedAlgorithm;
import kr.gaion.ceh.common.config.FileConfigLoader;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.util.CsvFormatConversion;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.bean.LabeledData;
import scala.Tuple2;

/**
 * 
 * @author hoang
 *
 */
@Deprecated
public class SparkEsConnector {
	final static Logger logger = LoggerFactory.getLogger(SparkEsConnector.class);
	private static SparkConf sConf = null;
	private static JavaSparkContext jvSc = null;
	private static FileConfigLoader sparkSettings = null;

	// private static SparkContext sc;
	/**
	 * Constructor It is private method Get setting value from configuration file
	 * then initialize Spark object
	 * 
	 * @throws Exception
	 */
	private SparkEsConnector() throws Exception {

		String sparkConfigFile = Utilities.getPathInWorkingFolder(Constants.CONF_DIR, Constants.SPARK_CONFIG_FILE);
		try {
			sparkSettings = new FileConfigLoader(sparkConfigFile);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// for this application (Rest-API)
		sConf = new SparkConf();
		// sConf.setJars(new String[] { Constants.SPA_JAR_ELASTICSEARCH });

		/**
		 * All configuration:
		 */
		loadAllSettingsFromConfFile(sparkSettings, sConf);

		// Enable event logging
		// sConf.set("spark.eventLog.enabled", "true");
		// SetUp HADOOP_HOME environment variable on the OS level or
		// programmatically:
		// Note that if you enable event logging, you have to set Hadoop home
		// directory
		String hadoopHomeDir = sparkSettings.getSetting("hadoop.home.dir");
		if (hadoopHomeDir != null && hadoopHomeDir.length() > 0) {
			System.setProperty("hadoop.home.dir", sparkSettings.getSetting("hadoop.home.dir"));
		}

	}

	/**
	 * To get SparkConf, Singleton pattern (within thread safe) was applied
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public synchronized static SparkConf getSparkConf() throws Exception {
		if (sConf == null || jvSc == null || jvSc.sc().isStopped()) {
			new SparkEsConnector();
		}
		return sConf;
	}

	/**
	 * To get JavaSparkContext Singleton (within thread safe) pattern is applied
	 * 
	 * @return
	 * @throws Exception
	 */
	public synchronized static JavaSparkContext getJavaSparkContext() throws Exception {
		if (jvSc == null || jvSc.sc().isStopped()) {
			jvSc = new JavaSparkContext(getSparkConf());
			return jvSc;
		} else {
			return jvSc;
		}
	}

	/**
	 * To get SparkContext Singleton pattern is applied
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static SparkContext getSparkContext() throws Exception {
		return getJavaSparkContext().sc();
	}

	/**
	 * To destroy JavaSparkContext
	 */
	public static void stopJavaSparkContext() {
		if (jvSc != null && !jvSc.sc().isStopped()) {
			jvSc.stop();
		}
	}

	/**
	 * To write data to Elastic-search<br>
	 * It needs to call getJavaSparkContext() before call this method<br>
	 * After, it has to invoke stopJavaSparkContext() too<br>
	 */
	public static void writeDataToEs(String url, Map<String, Object> mapData) {
		JavaRDD<Map<String, ?>> javaRdd = jvSc.parallelize(ImmutableList.of(mapData));
		JavaEsSpark.saveToEs(javaRdd, url);
	}

	/**
	 * To write data with JSON format to Elastic-search<br>
	 * It needs to call getJavaSparkContext() before call this method<br>
	 * After, it has to invoke stopJavaSparkContext() too<br>
	 */
	@Deprecated
	public static void writeDataToEs(String url, String jsonData) {
		JavaRDD<String> javaRdd = jvSc.parallelize(ImmutableList.of(jsonData));
		JavaEsSpark.saveToEs(javaRdd, url);
	}

	/**
	 * To write data from specific file to Elastic-search<br>
	 * 
	 * @param url
	 * @param _source
	 * @param dataFilePath
	 * @throws Exception
	 */
	public static void writeDataToEs(String url, String _source, String dataFilePath) throws Exception {

		logger.info("get Spark context");
		// getJavaSparkContext();

		logger.info("load data from file to RDD" + dataFilePath + " to Elasticsearch");
		JavaRDD<String> data = SparkEsConnector.getJavaSparkContext().textFile(dataFilePath);

		logger.info("map RDD");

		JavaRDD<Map<String, ?>> javaMapRdd = data.map(new Function<String, Map<String, ?>>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3912348950508313615L;

			@Override
			public Map<String, ?> call(String line) throws Exception {
				return ImmutableMap.of(_source, line);
			}
		});

		logger.info("save to Elasticsearch");
		JavaEsSpark.saveToEs(javaMapRdd, url);
		logger.info("savedfile " + dataFilePath + " to Elasticsearch successfully");

		return;
	}

	/**
	 * to index data from CSV file to Elasticsearch
	 * 
	 * @param url
	 * @param dataFilePath
	 * @param separator
	 * @throws Exception
	 */
	public static void writeCsvFileToEs(String url, String dataFilePath, String separator) throws Exception {

		logger.info("get Spark context");
		// getJavaSparkContext();

		logger.info("load data from file to RDD" + dataFilePath + " to Elasticsearch");
		JavaRDD<String> data = SparkEsConnector.getJavaSparkContext().textFile(dataFilePath);

		// extract first line
		String firstLine = data.first();

		// because the fields were saved in elasticsearch was unordered, therefore it
		// needs to save the header to make sure the correct order of Vector for
		// training and predicting
		Map<String, Object> headerInfo = new HashMap<>(); // #PC0001
		headerInfo.put("header", firstLine); // #PC0001
		headerInfo.put("delimiter", separator); // #PC0001
		SparkEsConnector.writeDataToEs(url + "_header", headerInfo); // #PC0001

		String[] header = firstLine.split(separator);
		JavaRDD<String> filteredData = data.filter(row -> !row.equals(firstLine));

		logger.info("map RDD");
		JavaRDD<String> javaMapRdd = filteredData.map(new Function<String, String>() {

			private static final long serialVersionUID = -7962090590463344171L;

			@Override
			public String call(String line) throws Exception {
				String json = CsvFormatConversion.convertCsvToJson(header, line, separator);

				return json;
			}
		});

		logger.info("save to Elasticsearch");
		JavaEsSpark.saveJsonToEs(javaMapRdd, url);
		logger.info("saved file " + dataFilePath + " to Elasticsearch successfully");

		return;
	}

	/**
	 * to save LabeledPoint data to ElasticSearch
	 * 
	 * @param url
	 * @param _source
	 * @param dataFilePath
	 * @throws Exception
	 */
	public static void writeLabeledPointDataToEs(String url, String _source, String dataFilePath) throws Exception {

		logger.info("get Spark context");
		// getJavaSparkContext();

		logger.info("get LabeledPoint data from file");
		JavaRDD<LabeledPoint> javaRddData = MLUtils.loadLabeledPoints(getSparkContext(), dataFilePath).toJavaRDD();

		logger.info("processing JavaRDD ..");
		JavaRDD<Map<String, ?>> javaMapRdd = javaRddData.map(new Function<LabeledPoint, Map<String, ?>>() {
			private static final long serialVersionUID = 3018760313379004239L;

			@Override
			public Map<String, ?> call(LabeledPoint labelPoint) throws Exception {
				return ImmutableMap.of(_source, labelPoint.toString());
			}
		});

		logger.info("processed JavaRDD, saving to Elasticsearch");
		JavaEsSpark.saveToEs(javaMapRdd, url);
		logger.info("saved to Elasticsearch successfully");

		logger.info("stop Spark context");
		// stopJavaSparkContext();

		return;
	}

	/**
	 * To read data from Elastic-search<br>
	 * 
	 * @param config
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static JavaPairRDD<String, Map<String, Object>> getDataFromEs(IConfigurable config, String... query)
			throws Exception {
		jvSc = getJavaSparkContext();
		String index = config.getSetting(AlgorithmSettings.ES_READING_INDEX);
		String type = config.getSetting(AlgorithmSettings.ES_READING_TYPE);

		if (query.length > 0) {
			return JavaEsSpark.esRDD(jvSc, getURL(index, type), query[0]);
		} else {
			return JavaEsSpark.esRDD(jvSc, getURL(index, type));
		}

	}

	/**
	 * To read data from Elastic-search<br>
	 * 
	 * @param index
	 * @param type
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static JavaPairRDD<String, Map<String, Object>> getDataFromEs(String index, String type, String... query)
			throws Exception {

		jvSc = getJavaSparkContext();
		if (query.length > 0) {
			return JavaEsSpark.esRDD(jvSc, getURL(index, type), query[0]);
		} else {
			return JavaEsSpark.esRDD(jvSc, getURL(index, type));
		}

	}

	/**
	 * to get unlabeled data from ElasticSearch
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static JavaRDD<Vector> getUnlabeledDataFromES(IConfigurable config) throws Exception {
		String esDataIndex = config.getSetting(AlgorithmSettings.ES_READING_INDEX);
		String esDataType = config.getSetting(AlgorithmSettings.ES_READING_TYPE);
		String esDataField = config.getSetting(AlgorithmSettings.ES_READING_FIELDNAME);
		String delimiter = config.getSetting(AlgorithmSettings.DELIMITER);
		// String[] featureCols =
		// config.getSetting(AlgorithmSettings.LIST_FEATURES_COL); // TODO

		JavaPairRDD<String, Map<String, Object>> jvRddData = SparkEsConnector.getDataFromEs(config);
		/*
		 * JavaPairRDD<String, Map<String, Object>> jvRddIndex =
		 * SparkESConnector.getDataFromEs(esDataIndex, esDataType +
		 * Constants.SPA_ES_LABELED_TYPE); int labeledIndex =
		 * Integer.parseInt(jvRddIndex.values().first().get(Constants.
		 * SPA_ES_LABELED_INDEX).toString()); logger.info("Index of labeled field: " +
		 * labeledIndex);
		 */

		logger.info(String.format("Get unlabeled data from Elasticsearchfor: _index: %s, _type: %s, _source: %s",
				esDataIndex, esDataType, esDataField));
		JavaRDD<Vector> esData = null;
		esData = jvRddData.map(new Function<Tuple2<String, Map<String, Object>>, Vector>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7391353001607139892L;

			@Override
			public Vector call(Tuple2<String, Map<String, Object>> tuple) throws Exception {
				String line = (String) tuple._2.get(esDataField);
				String[] parts = line.split(delimiter);
				/*
				 * double[] v = new double[parts.length - 1]; for (int i = 0, j = 0; i <
				 * parts.length; i++) { if (i != labeledIndex) { v[j++] =
				 * Double.parseDouble(parts[i]); } }
				 */
				double[] v = new double[parts.length];
				for (int i = 0, j = 0; i < parts.length; i++) {
					v[j++] = Double.parseDouble(parts[i]);
				}
				return Vectors.dense(v);
			}
		});

		return esData;
	}

	/**
	 * to get LabeledPoint data from CSV format data at Elasticsearch
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static JavaRDD<LabeledPoint> getLabeledPointFromEsCsvFormat(IConfigurable config) throws Exception {

		// get needed settings
		String esDataIndex = config.getSetting(AlgorithmSettings.ES_READING_INDEX);
		String esDataType = config.getSetting(AlgorithmSettings.ES_READING_TYPE);
		String classCol = config.getSetting(AlgorithmSettings.CLASS_COL);
		String[] featureCols = config.getSetting(AlgorithmSettings.LIST_FEATURES_COL);

		logger.info("Getting data from ElasticSearch for: " + esDataIndex + "/" + esDataType);
		JavaRDD<LabeledPoint> lpData = null;

		JavaPairRDD<String, Map<String, Object>> esDataRDD = SparkEsConnector.getDataFromEs(config);

		// filter out the row which is not enough amount of field
		// however, in the case you are sure the data is appropriate, you should skip this block of code to improve performance
		/*int featureSize = featureCols.length;
		 esDataRDD = esDataRDD.filter(new Function<Tuple2<String, Map<String, Object>>, Boolean>() {

			private static final long serialVersionUID = -1332956515774724250L;

			@Override
			public Boolean call(Tuple2<String, Map<String, Object>> tuple2) throws Exception {
				if (tuple2._2.size() < featureSize) {
					return Boolean.FALSE;
				}
				return Boolean.TRUE;
			}
		});*/ //#PC0009

		lpData = esDataRDD.map(new Function<Tuple2<String, Map<String, Object>>, LabeledPoint>() {

			private static final long serialVersionUID = 175856991710808092L;

			@Override
			public LabeledPoint call(Tuple2<String, Map<String, Object>> tuple2) throws Exception {
				Map<String, Object> mapData = tuple2._2();
				Double label = Double.parseDouble(mapData.get(classCol).toString());
				double[] vector = new double[featureCols.length];
				int index = 0;
				for (String feature : featureCols) {
					vector[index++] = Double.parseDouble(mapData.get(feature).toString());
				}

				return new LabeledPoint(label, Vectors.dense(vector));
			}
		});

		return lpData;
	}

	/**
	 * to get LabeledPoint data from CSV format data at Elasticsearch
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static JavaRDD<LabeledPoint> getLabeledPointFromEsCsvFormat(IConfigurable config, int[] count)
			throws Exception {

		// get needed settings
		String esDataIndex = config.getSetting(AlgorithmSettings.ES_READING_INDEX);
		String esDataType = config.getSetting(AlgorithmSettings.ES_READING_TYPE);
		String classCol = config.getSetting(AlgorithmSettings.CLASS_COL);
		String[] featureCols = config.getSetting(AlgorithmSettings.LIST_FEATURES_COL);

		logger.info("Getting data from ElasticSearch for: " + esDataIndex + "/" + esDataType);
		JavaRDD<LabeledPoint> lpData = null;

		JavaPairRDD<String, Map<String, Object>> esDataRDD = SparkEsConnector.getDataFromEs(config);

		// filter out the row which is not enough amount of field.
		/*int featureSize = featureCols.length;
		 esDataRDD = esDataRDD.filter(new Function<Tuple2<String, Map<String, Object>>, Boolean>() {
			private static final long serialVersionUID = -2078123279758935509L;

			@Override
			public Boolean call(Tuple2<String, Map<String, Object>> tuple2) throws Exception {
				if (tuple2._2.size() < featureSize) {
					return Boolean.FALSE;
				}
				return Boolean.TRUE;
			}
		});*/ //#PC0009

		// Get all classes, count distinct
		/*JavaRDD<String> listAllDistinctClasses = esDataRDD
			.map(new Function<Tuple2<String, Map<String, Object>>, String>() {
				private static final long serialVersionUID = -1578903038986150503L;
	
				@Override
				public String call(Tuple2<String, Map<String, Object>> tuple2) throws Exception {
					if (tuple2._2().get(classCol) != null) {
						return tuple2._2().get(classCol).toString();
					} else {
						return "0";
					}
				}
			});*/
		
		// count[0] = new Long(esDataRDD.count()).intValue(); //#PC0010

		lpData = esDataRDD.map(new Function<Tuple2<String, Map<String, Object>>, LabeledPoint>() {

			private static final long serialVersionUID = 175856991710808092L;

			@Override
			public LabeledPoint call(Tuple2<String, Map<String, Object>> tuple2) throws Exception {
				Map<String, Object> mapData = tuple2._2();
				Double label = Double.parseDouble(mapData.get(classCol).toString());
				double[] vector = new double[featureCols.length];
				int index = 0;
				for (String feature : featureCols) {
					vector[index++] = Double.parseDouble(mapData.get(feature).toString());
				}

				return new LabeledPoint(label, Vectors.dense(vector));
			}
		});

		return lpData;
	}

	/**
	 * to get LabeledPoint data from CSV format data at Elasticsearch use for RF
	 * algorithm only
	 * 
	 * @param config
	 * @param listClasses
	 * @return
	 * @throws Exception
	 */
	public static JavaRDD<LabeledPoint> getLabeledPointFromEsCsvFormat(IConfigurable config, List<String> listClasses)
			throws Exception {

		// get needed settings
		String esDataIndex = config.getSetting(AlgorithmSettings.ES_READING_INDEX);
		String esDataType = config.getSetting(AlgorithmSettings.ES_READING_TYPE);
		String classCol = config.getSetting(AlgorithmSettings.CLASS_COL);
		String[] featureCols = config.getSetting(AlgorithmSettings.LIST_FEATURES_COL);
		int featureSize = featureCols.length;

		logger.info("Getting data from ElasticSearch for: " + esDataIndex + "/" + esDataType);
		JavaRDD<LabeledPoint> lpData = null;

		JavaPairRDD<String, Map<String, Object>> esDataRDD = SparkEsConnector.getDataFromEs(config);

		// Get all classes
		JavaRDD<String> listAllClasses = esDataRDD.map(new Function<Tuple2<String, Map<String, Object>>, String>() {

			private static final long serialVersionUID = -872130053904407599L;

			@Override
			public String call(Tuple2<String, Map<String, Object>> tuple2) throws Exception {
				if (tuple2._2().get(classCol) != null) {
					return tuple2._2().get(classCol).toString();
				} else {
					return "0";
				}

			}
		});
		listClasses.addAll(listAllClasses.distinct().collect());
		/*
		 * Map<String, Double> categoriesMap = new HashMap<>(); Double index = 0.0; for
		 * ( String category : listClasses) { categoriesMap.put(category, index++); }
		 */

		// filter out the row which is not enough amount of field.
		esDataRDD = esDataRDD.filter(new Function<Tuple2<String, Map<String, Object>>, Boolean>() {

			private static final long serialVersionUID = -2078123279758935509L;

			@Override
			public Boolean call(Tuple2<String, Map<String, Object>> tuple2) throws Exception {
				if (tuple2._2.size() < featureSize) {
					return Boolean.FALSE;
				}
				return Boolean.TRUE;
			}
		});

		lpData = esDataRDD.map(new Function<Tuple2<String, Map<String, Object>>, LabeledPoint>() {

			private static final long serialVersionUID = 175856991710808092L;

			@Override
			public LabeledPoint call(Tuple2<String, Map<String, Object>> tuple2) throws Exception {
				Map<String, Object> mapData = tuple2._2();
				String strLabel = mapData.get(classCol).toString();
				double[] vector = new double[featureCols.length];
				int index = 0;
				for (String feature : featureCols) {
					vector[index++] = Double.parseDouble(mapData.get(feature).toString());
				}

				return new LabeledPoint(listClasses.indexOf(strLabel), Vectors.dense(vector));
			}
		});

		return lpData;
	}

	/**
	 * To get data from ElasticSearch <br>
	 * Parse them to LabeledPoint <br>
	 * Data from ElasticSearch has to be the default format used as bellow: <br>
	 * <li>Split by " " (space)
	 * <li>First element is label
	 * <li>The follow elements are features
	 * 
	 * @param config
	 * @return JavaRDD<LabeledPoint>
	 * @throws Exception
	 */
	@Deprecated
	public static JavaRDD<LabeledPoint> getLabeledDataFromES(IConfigurable config) throws Exception {

		String esDataIndex = config.getSetting(AlgorithmSettings.ES_READING_INDEX);
		String esDataType = config.getSetting(AlgorithmSettings.ES_READING_TYPE);
		String esDataField = config.getSetting(AlgorithmSettings.ES_READING_FIELDNAME);
		String delimiter = config.getSetting(AlgorithmSettings.DELIMITER);
		int indexOfLabeledField = config.getSetting(SupervisedAlgorithm.INDEX_OF_LABELED_FIELD);

		logger.info("Getting data from ElasticSearch for: " + esDataIndex + "/" + esDataType + "/" + esDataField);
		JavaRDD<LabeledPoint> esData = null;

		JavaPairRDD<String, Map<String, Object>> jvRddData = SparkEsConnector.getDataFromEs(config);
		/*
		 * JavaPairRDD<String, Map<String, Object>> jvRddIndex =
		 * SparkEsConnector.getDataFromEs(esDataIndex, esDataType +
		 * Constants.SPA_ES_LABELED_TYPE); int labeledIndex =
		 * Integer.parseInt(jvRddIndex.values().first().get(Constants.
		 * SPA_ES_LABELED_INDEX).toString()); logger.info("Index of labeled field: " +
		 * labeledIndex);
		 */

		// To get data format
		JavaPairRDD<String, Map<String, Object>> jvRddFormat = getDataFromEs(esDataIndex,
				esDataType + Constants.SPA_ES_DATA_FORMAT_TYPE);
		int dataFormat = -1;
		try {
			dataFormat = Integer.parseInt(jvRddFormat.values().first().get(Constants.SPA_ES_DATA_FORMAT).toString());
		} catch (Exception e) {
			logger.warn(e.getLocalizedMessage());
		}
		logger.info("Data format: " + dataFormat);

		// get data from ElasticSearch
		switch (dataFormat) {
		/*
		 * case EsIndexInfo.DENSE_FORMAT: {
		 * 
		 * }
		 */
		case EsIndexInfo.SPARSE_FORMAT: {
			logger.info("Get data from Elasticsearch with data format is SPARSE");
			esData = jvRddData.map(new Function<Tuple2<String, Map<String, Object>>, LabeledPoint>() {

				private static final long serialVersionUID = -6704947718339229465L;

				@Override
				public LabeledPoint call(Tuple2<String, Map<String, Object>> tuple) throws Exception {
					String line = (String) tuple._2.get(esDataField);
					/*
					 * TODO String[] parts = line.split(delimiter); int[] indices = new
					 * int[parts.length - 1]; double[] values = new double[parts.length - 1]; for
					 * (int i = 0, j = 0; i < parts.length; i++) { if (i != labeledIndex) { String[]
					 * indexValuePair = parts[i].split(":"); if (indexValuePair.length >= 2) {
					 * indices[j] = Integer.parseInt(indexValuePair[0]); values[j++] =
					 * Double.parseDouble(indexValuePair[1]); } } } return new
					 * LabeledPoint(Double.parseDouble(parts[labeledIndex]), Vectors.sparse(0,
					 * indices, values));
					 */
					return LabeledPoint.parse(line);
				}
			});
			break;
		}
		default: {
			logger.info("Get data from Elasticsearch with data format is DENSE");
			esData = jvRddData.map(new Function<Tuple2<String, Map<String, Object>>, LabeledPoint>() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 6451853182116940221L;

				@Override
				public LabeledPoint call(Tuple2<String, Map<String, Object>> tuple) throws Exception {
					String line = (String) tuple._2.get(esDataField);
					String[] parts = line.split(delimiter);
					double[] v = new double[parts.length - 1];
					for (int i = 0, j = 0; i < parts.length; i++) {
						if (i != indexOfLabeledField) {
							v[j++] = Double.parseDouble(parts[i]);
						}
					}
					return new LabeledPoint(Double.parseDouble(parts[indexOfLabeledField]), Vectors.dense(v));
				}
			});
			break;
		}
		}

		// return JavaRDD<LabeledPoint>
		return esData;
	}

	/**
	 * to load all unlabeled data from file
	 * 
	 * @param config
	 * @return
	 */

	public static JavaRDD<Vector> loadUnlabeledDataFromFile(IConfigurable config) {
		JavaRDD<Vector> vectorRdd = null;
		JavaRDD<String> dataRDD = null;
		String path = config.getSetting(AlgorithmSettings.FILE_NAME);
		String delimiter = config.getSetting(AlgorithmSettings.DELIMITER);
		String[] fieldsForPredict = config.getSetting(AlgorithmSettings.LIST_FIELD_FOR_PREDICT);
		int featureSize = fieldsForPredict.length;

		dataRDD = jvSc.textFile(path);
		int[] selectedFeatureIndices = new int[featureSize]; // to map all fields to indices
		String header = dataRDD.first();
		List<String> originalColsList = Arrays.asList(header.split(delimiter));
		int index = 0;
		for (String selectedCol : fieldsForPredict) {
			selectedFeatureIndices[index++] = originalColsList.indexOf(selectedCol);
		}

		// filter out the row which is not enough amount of field or it is first row
		dataRDD = dataRDD.filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 2038343200137773812L;

			@Override
			public Boolean call(String line) throws Exception {
				if (line.split(delimiter).length < featureSize || header.equals(line))
					return Boolean.FALSE;
				return Boolean.TRUE;
			}
		});

		vectorRdd = dataRDD.map(new Function<String, Vector>() {
			private static final long serialVersionUID = -4318446083260144966L;

			@Override
			public Vector call(String line) throws Exception {
				int index = 0;
				double[] vector = new double[featureSize];
				String[] sarray = line.split(delimiter);
				for (Integer selectedIndex : selectedFeatureIndices) {
					vector[index++] = Double.parseDouble(sarray[selectedIndex]);
				}
				return Vectors.dense(vector);
			}
		});

		return vectorRdd;
	}

	/**
	 * to load all data (double type) from file without processing
	 * 
	 * @param config
	 * @return
	 */

	public static JavaRDD<double[]> loadDataFromFile(IConfigurable config) {
		JavaRDD<double[]> vectorRdd = null;
		JavaRDD<String> dataRdd = null;
		String path = config.getSetting(AlgorithmSettings.FILE_NAME);
		String delimiter = config.getSetting(AlgorithmSettings.DELIMITER);

		dataRdd = jvSc.textFile(path);
		vectorRdd = dataRdd.map(new Function<String, double[]>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7128869977811592292L;

			@Override
			public double[] call(String line) throws Exception {
				String[] sarray = line.split(delimiter);
				double[] values = new double[sarray.length];
				for (int i = 0; i < sarray.length; i++) {
					if (sarray[i].length() > 0) {
						values[i] = Double.parseDouble(sarray[i]);
					}
				}
				return values;
			}
		});

		return vectorRdd;
	}

	/**
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */

	public static JavaRDD<Vector> getClusteringDataFromES(IConfigurable config) throws Exception {

		logger.info("Get data from ElasticSearch");

		JavaRDD<Vector> parsedData = null;
		JavaPairRDD<String, Map<String, Object>> esData = null;

		// get data from ElasticSearch
		esData = SparkEsConnector.getDataFromEs(config);

		// map data get from ElasticSearch
		String delimiter = config.getSetting(AlgorithmSettings.DELIMITER);
		String fieldName = config.getSetting(AlgorithmSettings.ES_READING_FIELDNAME);
		parsedData = esData.map(new Function<Tuple2<String, Map<String, Object>>, Vector>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 8268370166156583201L;

			@Override
			public Vector call(Tuple2<String, Map<String, Object>> tuple) throws Exception {
				String line = (String) tuple._2().get(fieldName);
				line = line.trim();
				// logger.info("line : " + line);
				String[] sarray = line.split(delimiter);
				double[] values = new double[sarray.length];
				for (int i = 0; i < sarray.length; i++) {
					if (sarray[i].length() > 0) {
						values[i] = Double.parseDouble(sarray[i]);
					}
				}
				return Vectors.dense(values);
			}
		});

		return parsedData;
	}

	/**
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */

	public static JavaRDD<double[]> getClusterDataFromESForDataSet(IConfigurable config) throws Exception {

		logger.info("Get data from ElasticSearch");

		JavaRDD<double[]> parsedData = null;
		JavaPairRDD<String, Map<String, Object>> esData = null;

		// get data from ElasticSearch
		esData = SparkEsConnector.getDataFromEs(config);

		// map data get from ElasticSearch
		String delimiter = config.getSetting(AlgorithmSettings.DELIMITER);
		String fieldName = config.getSetting(AlgorithmSettings.ES_READING_FIELDNAME);
		parsedData = esData.map(new Function<Tuple2<String, Map<String, Object>>, double[]>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8359568738112488699L;

			@Override
			public double[] call(Tuple2<String, Map<String, Object>> tuple) throws Exception {
				String line = (String) tuple._2().get(fieldName);
				line = line.trim();
				// logger.info("line : " + line);
				String[] sarray = line.split(delimiter);
				double[] values = new double[sarray.length];
				for (int i = 0; i < sarray.length; i++) {
					if (sarray[i].length() > 0) {
						values[i] = Double.parseDouble(sarray[i]);
					}
				}
				return values;
			}
		});

		return parsedData;
	}

	/**
	 * to get the data from Elasticsearch line by line and do not process any thing.
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static JavaRDD<String> getDataFromESWithoutProcessing(IConfigurable config) throws Exception {
		logger.info("Get data from ElasticSearch");

		JavaRDD<String> parsedData = null;
		JavaPairRDD<String, Map<String, Object>> esData = null;

		// get data from ElasticSearch
		esData = SparkEsConnector.getDataFromEs(config);

		// map data get from ElasticSearch
		String fieldName = config.getSetting(AlgorithmSettings.ES_READING_FIELDNAME);
		parsedData = esData.map(new Function<Tuple2<String, Map<String, Object>>, String>() {
			private static final long serialVersionUID = 4905959970153110906L;

			@Override
			public String call(Tuple2<String, Map<String, Object>> tuple) throws Exception {
				String line = (String) tuple._2().get(fieldName);
				return line;
			}
		});

		return parsedData;
	}

	/**
	 * To make URL for reading-writing to Elastic-search
	 */
	public static String getURL(String index, String type) {

		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(index);
		strBuilder.append("/");
		strBuilder.append(type);

		return strBuilder.toString();
	}

	/**
	 * to load setting from configuration file
	 * 
	 * @param key
	 */
	@SuppressWarnings("unused")
	private void loadSetting(String key) {
		try {
			sConf.set(key, sparkSettings.getSetting(key));
		} catch (NullPointerException e) {
			System.err.println("This setting was not existed: " + key);
		}
	}

	/**
	 * to load all settings from configuration file, located in
	 * _app_home_dir/conf/app.conf
	 * 
	 * @param sparkSettings
	 * @param sparkConf
	 */
	private void loadAllSettingsFromConfFile(FileConfigLoader sparkSettings, SparkConf sparkConf) {

		Properties prop = sparkSettings.getProperties();
		for (Entry<Object, Object> entry : prop.entrySet()) {
			sparkConf.set(entry.getKey().toString(), entry.getValue().toString());
		}
	}

	/**
	 * to get data from Elasticsearch as DataFrame format
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static Dataset<Row> getDatasetFromESWithDenseFormat(IConfigurable config) throws Exception {

		String esDataIndex = config.getSetting(AlgorithmSettings.ES_READING_INDEX);
		String esDataType = config.getSetting(AlgorithmSettings.ES_READING_TYPE);
		String classCol = config.getSetting(AlgorithmSettings.CLASS_COL); // #PC0008
		String[] featureCols = config.getSetting(AlgorithmSettings.LIST_FEATURES_COL); // #PC0008
		// String esDataField = config.getSetting(AlgorithmSettings.ES_READING_FIELDNAME); // #PC0008
		/*int indexOfLabeledField = config.getSetting(SupervisedAlgorithm.INDEX_OF_LABELED_FIELD);
		String delimiter = config.getSetting(AlgorithmSettings.DELIMITER);*/ // #PC0008

		logger.info("Getting data from ElasticSearch for: " + esDataIndex + "/" + esDataType + "/"/* + esDataField // #PC0008*/);
		JavaRDD<LabeledData> esData = null;

		JavaPairRDD<String, Map<String, Object>> jvRddData = SparkEsConnector.getDataFromEs(config);

		// get data from ElasticSearch
		esData = jvRddData.map(new Function<Tuple2<String, Map<String, Object>>, LabeledData>() {

			private static final long serialVersionUID = -8474215019819694581L;

			public LabeledData call(Tuple2<String, Map<String, Object>> tuple2) throws Exception {
				/*String line = (String) tuple._2.get(esDataField);
				String[] parts = line.split(delimiter);
				double[] v = new double[parts.length - 1];
				for (int i = 0, j = 0; i < parts.length; i++) {
					if (i != indexOfLabeledField) {
						v[j++] = Double.parseDouble(parts[i]);
					}
				}*/ // #PC0008
				// #PC0008 - Start
				Map<String, Object> mapData = tuple2._2();
				Double label = Double.parseDouble(mapData.get(classCol).toString());
				double[] vector = new double[featureCols.length];
				int index = 0;
				for (String feature : featureCols) {
					vector[index++] = Double.parseDouble(mapData.get(feature).toString());
				}
				// #PC0008 - End
				
				LabeledData dataReturn = new LabeledData();
				dataReturn.setLabel(label); // #PC0008
				dataReturn.setFeatures(org.apache.spark.ml.linalg.Vectors.dense(vector)); // #PC0008

				return dataReturn;
			}
		});

		SparkSession spark = SparkSession.builder().config(SparkEsConnector.getSparkConf()).getOrCreate();
		Dataset<Row> dataFrame = spark.createDataFrame(esData, LabeledData.class);

		return dataFrame;
	}
	
	

	/**
	 * to load data from file, for predicting
	 * 
	 * @param config
	 * @return JavaRDD<java.util.Vector<?>>
	 * @throws Exception
	 */
	public static JavaRDD<org.apache.spark.ml.linalg.Vector> getMlVectorFromFileWithDenseFormat(IConfigurable config)
			throws Exception {

		JavaRDD<org.apache.spark.ml.linalg.Vector> vectorRdd = null;
		JavaRDD<String> dataRdd = null;
		String path = config.getSetting(AlgorithmSettings.FILE_NAME);
		String delimiter = config.getSetting(AlgorithmSettings.DELIMITER);
		String[] fieldsForPredict = config.getSetting(AlgorithmSettings.LIST_FIELD_FOR_PREDICT);
		int featureSize = fieldsForPredict.length;
		
		dataRdd = jvSc.textFile(path);
		int[] selectedFeatureIndices = new int[featureSize]; // to map all fields to indices
		String header = dataRdd.first();
		List<String> originalColsList = Arrays.asList(header.split(delimiter));
		int index = 0;
		for (String selectedCol : fieldsForPredict) {
			selectedFeatureIndices[index++] = originalColsList.indexOf(selectedCol);
		}
		// filter out the row which is not enough amount of field or it is first row
		dataRdd = dataRdd.filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = -2861481426068992013L;
			@Override
			public Boolean call(String line) throws Exception {
				if (line.split(delimiter).length < featureSize || header.equals(line))
					return Boolean.FALSE;
				return Boolean.TRUE;
			}
		});
		
		// convert to proper vector
		vectorRdd = dataRdd.map(new Function<String, org.apache.spark.ml.linalg.Vector>() {
			private static final long serialVersionUID = 1137193584871871653L;
			@Override
			public org.apache.spark.ml.linalg.Vector call(String line) throws Exception {
				String[] sarray = line.split(delimiter);
				double[] values = new double[featureSize];
				int index = 0;
				for (int selectedIndex : selectedFeatureIndices) {
					if (sarray[selectedIndex].length() > 0) {
						values[index++] = Double.parseDouble(sarray[selectedIndex]);
					}
				}

				return org.apache.spark.ml.linalg.Vectors.dense(values);
			}
		});

		return vectorRdd;
	}

	/**
	 * to get data from Elasticsearch for predicting<br>
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static JavaRDD<org.apache.spark.ml.linalg.Vector> getMlVectorFromESWithDenseFormat(IConfigurable config)
			throws Exception {
		String esDataIndex = config.getSetting(AlgorithmSettings.ES_READING_INDEX);
		String esDataType = config.getSetting(AlgorithmSettings.ES_READING_TYPE);
		String esDataField = config.getSetting(AlgorithmSettings.ES_READING_FIELDNAME);
		String delimiter = config.getSetting(AlgorithmSettings.DELIMITER);

		JavaPairRDD<String, Map<String, Object>> jvRddData = SparkEsConnector.getDataFromEs(config);

		logger.info(String.format("Get unlabeled data from Elasticsearchfor: _index: %s, _type: %s, _source: %s",
				esDataIndex, esDataType, esDataField));
		JavaRDD<org.apache.spark.ml.linalg.Vector> esData = null;
		esData = jvRddData.map(new Function<Tuple2<String, Map<String, Object>>, org.apache.spark.ml.linalg.Vector>() {

			private static final long serialVersionUID = -756948830657342663L;

			@Override
			public org.apache.spark.ml.linalg.Vector call(Tuple2<String, Map<String, Object>> tuple) throws Exception {
				String line = (String) tuple._2.get(esDataField);
				String[] parts = line.split(delimiter);
				double[] v = new double[parts.length];
				for (int i = 0, j = 0; i < parts.length; i++) {
					v[j++] = Double.parseDouble(parts[i]);
				}
				return org.apache.spark.ml.linalg.Vectors.dense(v);
			}
		});

		return esData;
	}
	
	/**
	 * to get unlabeled dataset/vector/feature from file
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static Dataset<?> getUnlabeledDatasetFromFile(IConfigurable config) throws Exception{
		String path = config.getSetting(AlgorithmSettings.FILE_NAME);
		String delimiter = config.getSetting(AlgorithmSettings.DELIMITER);
		String[] fieldsForPredict = config.getSetting(AlgorithmSettings.LIST_FIELD_FOR_PREDICT);
		
		// Load data from CSV file
		SparkSession spark = SparkSession.builder().config(SparkEsConnector.getSparkConf()).getOrCreate();
		Dataset<Row> originalData = spark.read().format("com.databricks.spark.csv")
				.option("header", "true")
				.option("nullValue", "0.0")
				.option("sep", delimiter)
				.load(path);
		
		// Convert/cast StringType to DecimalType (VectorAssembler does not support StringType)
		String[] fieldsForAssembling = new String[fieldsForPredict.length];
		int index = 0;
		for (String field : fieldsForPredict) {
			fieldsForAssembling[index++] = field + "_str";
			originalData = originalData.withColumn(field + "_str", originalData.col(field).cast(new DecimalType(38,0)));
		}
		
		// Create `features` column
		VectorAssembler assembler = new VectorAssembler()
				.setInputCols(fieldsForAssembling)
				.setOutputCol("features");
		Dataset<Row> data = assembler.transform(originalData);
		
		// return `features` only
		return data.select("features");
	}

}
