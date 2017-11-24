package kr.gaion.ceh.restapi.algorithms;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.AssociationRules.Rule;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowth.FreqItemset;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.FpGrowthResponse;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.bean.settings.FpGrowthSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;
import scala.Tuple2;

/**
 * To Use Spark-ML-library with data from Elastic-search
 * 
 * @author hoang
 *
 */
public class FpGrowth {

	final static Logger logger = LoggerFactory.getLogger(FpGrowth.class);
	final static String ALGORITHM_NAME = "FpGrowth";

	/**
	 * To create FP-Growth model from JavaRDD
	 * 
	 * @param config
	 * @param transactions
	 * @return
	 */
	private static FPGrowthModel<String> createFpGrowthModel(IConfigurable config, JavaRDD<List<String>> transactions) {
		transactions.cache();
		FPGrowth fpg = new FPGrowth();
		fpg.setMinSupport(config.getSetting(FpGrowthSettings.MIN_SUPPORT));
		fpg.setNumPartitions(config.getSetting(FpGrowthSettings.NUMBER_PARTITION));
		return fpg.run(transactions.cache());
	}

	/**
	 * To create FP-Growth model from file
	 * 
	 * @param config
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static FPGrowthModel<String> trainModel(IConfigurable config, String filePath) throws Exception {
		String delimiter = config.getSetting(AlgorithmSettings.DELIMITER);
		JavaRDD<String> data = SparkEsConnector.getJavaSparkContext().textFile(filePath);
		JavaRDD<List<String>> transactions = data.map(line -> Arrays.asList(line.split((delimiter))));
		return createFpGrowthModel(config, transactions);
	}

	/**
	 * To create FP-Growth model from data selected Elastic-search
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static FPGrowthModel<String> trainModel(IConfigurable config) throws Exception {

		logger.info("Train FP-Growth model from data selected Elastic-search");
		JavaRDD<List<String>> transactions = getDataFromES(config);
		FPGrowthModel<String> model = createFpGrowthModel(config, transactions);

		String modelName = config.getSetting(FpGrowthSettings.MODEL_NAME);
		logger.info("save FPGrowth model as name: " + modelName);
		String modelFullPathName = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, ALGORITHM_NAME,
				Constants.MODEL_DIR, modelName);
		Utilities.deleteIfExisted(modelFullPathName);
		model.save(SparkEsConnector.getSparkContext(), modelFullPathName);

		return model;
	}

	/**
	 * To write Association Rules to Elastic-search
	 */
	public static void writeAssociationRulesToEs(IConfigurable config, FPGrowthModel<String> model) {

		logger.info("\n\nWriting AssociationRules to Elastic-Search\n");
		String index = config.getSetting(FpGrowthSettings.ES_WRITING_INDEX);
		String type = config.getSetting(FpGrowthSettings.ES_WRITING_TYPE);
		String url = SparkEsConnector.getURL(index, type);
		double minConfidence = config.getSetting(FpGrowthSettings.MIN_CONFIDENCE);

		JavaRDD<Rule<String>> rules = model.generateAssociationRules(minConfidence).toJavaRDD();
		@SuppressWarnings("serial")
		JavaRDD<Map<String, ?>> javaMapRdd = rules.map(new Function<AssociationRules.Rule<String>, Map<String, ?>>() {
			@Override
			public Map<String, ?> call(Rule<String> rule) throws Exception {
				Map<String, ?> element = ImmutableMap.of("antecedent", rule.javaAntecedent(), "consequent",
						rule.javaConsequent(), "confidence", rule.confidence());
				return element;
			}
		});

		logger.info("save to Elasticsearch");
		JavaEsSpark.saveToEs(javaMapRdd, url);
		logger.info("saved Assosiaction rules to Elasticsearch successfully");
	}

	/**
	 * To write frequent items to Elastic-search
	 */
	public static void writeFrequentItemsets(IConfigurable config, FPGrowthModel<String> model) {

		logger.info("\n\nWriting frequent items to Elastic-Search\n");
		String index = config.getSetting(FpGrowthSettings.ES_WRITING_INDEX);
		String type = config.getSetting(FpGrowthSettings.ES_WRITING_TYPE);
		String url = SparkEsConnector.getURL(index, type);

		// filter the results which has frequent pattern length < 2
		@SuppressWarnings("serial")
		JavaRDD<FPGrowth.FreqItemset<String>> freq = model.freqItemsets().toJavaRDD()
				.filter(new Function<FPGrowth.FreqItemset<String>, Boolean>() {
					@Override
					public Boolean call(FreqItemset<String> itemset) throws Exception {
						if (itemset.javaItems().size() < 2) {
							return false;
						} else {
							return true;
						}
					}
				});

		@SuppressWarnings("serial")
		JavaRDD<Map<String, ?>> javaMapRdd = freq.map(new Function<FPGrowth.FreqItemset<String>, Map<String, ?>>() {

			@Override
			public Map<String, ?> call(FreqItemset<String> itemset) throws Exception {
				return ImmutableMap.of("items", itemset.javaItems(), "freq", itemset.freq());
			}
		});

		logger.info("save to Elasticsearch");
		JavaEsSpark.saveToEs(javaMapRdd, url);
		logger.info("saved frequent items to Elasticsearch successfully");
	}

	/**
	 * To get FP-Growth metrics from ElasticSearch
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	/*
	 * public static IResponseObject getMetricsFromES(IConfigurable config) throws
	 * Exception { IResponseObject response = new
	 * FPGrowthResponse(ResponseType.OBJECT_DATA); List<Map<String, Object>>
	 * asRulesData = null; List<Map<String, Object>> freqPatternsData = null; String
	 * index = config.getSetting(FPGrowthSettings.ES_WRITING_INDEX); String
	 * asRulesType = config.getSetting(FPGrowthSettings.ES_WRITING_ASR_TYPE); String
	 * freqPatternsType = config.getSetting(FPGrowthSettings.ES_WRITING_FREQ_TYPE);
	 * String asRulesQuery = ApiGetDataFromEs.makeQuery(index, asRulesType,
	 * "source"); String freqPatternsQuery = ApiGetDataFromEs.makeQuery(index,
	 * freqPatternsType, "source");
	 * 
	 * logger.info("get Association rules from ES, query: " + asRulesQuery);
	 * asRulesData = ApiGetDataFromEs.search(asRulesQuery);
	 * response.set(FPGrowthResponse.ASSOCIATION_RULES, asRulesData, List.class);
	 * 
	 * logger.info("get Frequent Patterns from ES, query: " + freqPatternsQuery);
	 * freqPatternsData = ApiGetDataFromEs.search(freqPatternsQuery);
	 * response.set(FPGrowthResponse.FREQUENT_PATTERNS, freqPatternsData,
	 * List.class);
	 * 
	 * logger.info("Get success, return response"); return response; }
	 */

	/**
	 * To get FP-Growth metrics from FPGrowthModel
	 * 
	 * @param config
	 * @return
	 */
	public static IResponseObject getFrequentPatterns(FPGrowthModel<String> model, IConfigurable config) {

		// filter the results which has frequent pattern length < 2
		@SuppressWarnings("serial")
		JavaRDD<FPGrowth.FreqItemset<String>> freq = model.freqItemsets().toJavaRDD()
				.filter(new Function<FPGrowth.FreqItemset<String>, Boolean>() {
					@Override
					public Boolean call(FreqItemset<String> itemset) throws Exception {
						if (itemset.javaItems().size() < 2) {
							return false;
						} else {
							return true;
						}
					}
				});

		@SuppressWarnings("serial")
		JavaRDD<Map<String, ?>> javaMapRddFreq = freq.map(new Function<FPGrowth.FreqItemset<String>, Map<String, ?>>() {

			@Override
			public Map<String, ?> call(FreqItemset<String> itemset) throws Exception {
				return ImmutableMap.of("items", itemset.javaItems(), "freq", itemset.freq());
			}
		});

		IResponseObject response = new FpGrowthResponse(ResponseType.OBJECT_DATA);
		response.set(FpGrowthResponse.FREQUENT_PATTERNS, javaMapRddFreq.collect(), List.class);

		return response;
	}

	/**
	 * to get AssociationRules from FPGrowthModel
	 * 
	 * @param model
	 * @param config
	 * @return
	 */
	public static IResponseObject getAssociationRules(FPGrowthModel<String> model, IConfigurable config) {

		IResponseObject response = new FpGrowthResponse(ResponseType.OBJECT_DATA);
		double minConfidence = config.getSetting(FpGrowthSettings.MIN_CONFIDENCE);
		JavaRDD<Rule<String>> rules = model.generateAssociationRules(minConfidence).toJavaRDD();

		@SuppressWarnings("serial")
		JavaRDD<Map<String, ?>> javaMapRdd = rules.map(new Function<AssociationRules.Rule<String>, Map<String, ?>>() {
			@Override
			public Map<String, ?> call(Rule<String> rule) throws Exception {
				Map<String, ?> element = ImmutableMap.of("antecedent", rule.javaAntecedent(), "consequent",
						rule.javaConsequent(), "confidence", rule.confidence());
				return element;
			}
		});
		response.set(FpGrowthResponse.ASSOCIATION_RULES, javaMapRdd.collect(), List.class);

		return response;
	}

	/**
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("serial")
	public static JavaRDD<List<String>> getDataFromES(IConfigurable config) throws Exception {

		logger.info("Get data from ElasticSearch");
		JavaRDD<List<String>> transactions = null;
		JavaPairRDD<String, Map<String, Object>> esData = null;

		// get data from ElasticSearch
		esData = SparkEsConnector.getDataFromEs(config);

		// map data get from ElasticSearch
		String delimiter = config.getSetting(AlgorithmSettings.DELIMITER);
		String fieldName = config.getSetting(AlgorithmSettings.ES_READING_FIELDNAME);
		transactions = esData.map(new Function<Tuple2<String, Map<String, Object>>, List<String>>() {
			@Override
			public List<String> call(Tuple2<String, Map<String, Object>> tuple) throws Exception {
				List<String> tran = Arrays.asList(tuple._2.get(fieldName).toString().split(delimiter));
				return tran;
			}
		});

		return transactions;
	}

}
