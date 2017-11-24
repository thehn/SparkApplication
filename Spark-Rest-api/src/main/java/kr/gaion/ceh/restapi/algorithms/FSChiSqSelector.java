package kr.gaion.ceh.restapi.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.feature.ChiSqSelector;
import org.apache.spark.mllib.feature.ChiSqSelectorModel;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.FSChiSqSelectorResponse;
import kr.gaion.ceh.common.bean.response.ResponseStatus;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.FSChiSqSelectorSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;

/**
 * 
 * @author hoang
 *
 */
public class FSChiSqSelector {

	public static IResponseObject train(IConfigurable config) throws Exception {

		// get data from elasticsearch
		JavaRDD<LabeledPoint> points = SparkEsConnector.getLabeledPointFromEsCsvFormat(config);

		// number of features
		int numberFeatures = config.getSetting(FSChiSqSelectorSettings.NUMBER_FEATURES);
		int bin = config.getSetting(FSChiSqSelectorSettings.NUMBER_BINS);
		String[] featureCols = config.getSetting(FSChiSqSelectorSettings.LIST_FEATURES_COL);
		String classCol = config.getSetting(FSChiSqSelectorSettings.CLASS_COL);
		String csvSeparator = Constants.CSV_SEPARATOR;

		// Discrete data in 16 equal bins since ChiSqSelector requires categorical
		// features
		// Although features are doubles, the ChiSqSelector treats each unique value as
		// a category
		JavaRDD<LabeledPoint> discretizedData = points.map(lp -> {
			double[] discretizedFeatures = new double[lp.features().size()];
			for (int i = 0; i < lp.features().size(); ++i) {
				discretizedFeatures[i] = Math.floor(lp.features().apply(i) / bin);
			}
			return new LabeledPoint(lp.label(), Vectors.dense(discretizedFeatures));
		});

		// Create ChiSqSelector that will select top x features
		ChiSqSelector selector = new ChiSqSelector(numberFeatures);

		// Create ChiSqSelector model (selecting features)
		ChiSqSelectorModel transformer = selector.fit(discretizedData.rdd());

		// Filter the top x features from each feature vector
		JavaRDD<String> filteredData = discretizedData.map(new Function<LabeledPoint, String>() {

			private static final long serialVersionUID = 2837247336854459658L;

			@Override
			public String call(LabeledPoint lp) throws Exception {
				StringBuilder dataBuilder = new StringBuilder();
				dataBuilder.append(lp.label());
				dataBuilder.append(csvSeparator);
				for (double value : transformer.transform(lp.features()).toArray()) {
					dataBuilder.append(value);
					dataBuilder.append(csvSeparator);
				}
				dataBuilder.deleteCharAt(dataBuilder.length() - 1);
				return dataBuilder.toString();
			}
		});

		List<String> selectedFields = new ArrayList<String>();
		int[] selectedIndices = transformer.selectedFeatures();
		for (int selectedIndex : selectedIndices) {
			selectedFields.add(featureCols[selectedIndex]);
		}

		FSChiSqSelectorResponse response = new FSChiSqSelectorResponse(ResponseType.OBJECT_DATA);
		response.setFilteredFeatures(filteredData.collect());
		response.setSelectedFields(selectedFields);
		response.setClassCol(classCol);
		response.setStatus(ResponseStatus.SUCCESS);

		return response;
	}

	/**
	 * to get filtered features and data RDD to each algorithm // #PC0016
	 * 
	 * @param config
	 *            input
	 * @param listSelectedFeatures
	 *            output
	 * @return
	 * @throws Exception
	 */
	public static JavaRDD<LabeledPoint> selectFeatures(IConfigurable config, List<String> selectedFields)
			throws Exception {
		// get data from elasticsearch
		JavaRDD<LabeledPoint> points = SparkEsConnector.getLabeledPointFromEsCsvFormat(config);

		// number of features
		int numberFeatures = config.getSetting(FSChiSqSelectorSettings.NUMBER_FEATURES);
		int bin = config.getSetting(FSChiSqSelectorSettings.NUMBER_BINS);

		// Discrete data in 16 equal bins since ChiSqSelector requires categorical
		// features
		// Although features are doubles, the ChiSqSelector treats each unique value as
		// a category
		JavaRDD<LabeledPoint> discretizedData = points.map(lp -> {
			double[] discretizedFeatures = new double[lp.features().size()];
			for (int i = 0; i < lp.features().size(); ++i) {
				discretizedFeatures[i] = Math.floor(lp.features().apply(i) / bin);
			}
			return new LabeledPoint(lp.label(), Vectors.dense(discretizedFeatures));
		});

		// Create ChiSqSelector that will select top x features
		ChiSqSelector selector = new ChiSqSelector(numberFeatures);

		selector.percentile();

		// Create ChiSqSelector model (selecting features)
		ChiSqSelectorModel transformer = selector.fit(discretizedData.rdd());

		JavaRDD<LabeledPoint> selectedDataRDD = discretizedData.map(new Function<LabeledPoint, LabeledPoint>() {
			private static final long serialVersionUID = -9208904204905041992L;

			@Override
			public LabeledPoint call(LabeledPoint lp) throws Exception {
				return LabeledPoint.apply(lp.label(), transformer.transform(lp.features()));
			}
		});

		String[] featureCols = config.getSetting(FSChiSqSelectorSettings.LIST_FEATURES_COL);
		if (selectedFields == null) {
			selectedFields = new ArrayList<String>();
		} else {
			// continue
		}
		int[] selectedIndices = transformer.selectedFeatures();
		for (int selectedIndex : selectedIndices) {
			selectedFields.add(featureCols[selectedIndex]);
		}

		return selectedDataRDD;
	}

	/**
	 * to get filtered features // #PC0016
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static String[] selectFeatures(IConfigurable config) throws Exception {
		// get data from elasticsearch
		JavaRDD<LabeledPoint> points = SparkEsConnector.getLabeledPointFromEsCsvFormat(config);

		// number of features
		int numberFeatures = config.getSetting(FSChiSqSelectorSettings.NUMBER_FEATURES);
		int bin = config.getSetting(FSChiSqSelectorSettings.NUMBER_BINS);

		// Discrete data in 16 equal bins since ChiSqSelector requires categorical
		// features
		// Although features are doubles, the ChiSqSelector treats each unique value as
		// a category
		JavaRDD<LabeledPoint> discretizedData = points.map(lp -> {
			double[] discretizedFeatures = new double[lp.features().size()];
			for (int i = 0; i < lp.features().size(); ++i) {
				discretizedFeatures[i] = Math.floor(lp.features().apply(i) / bin);
			}
			return new LabeledPoint(lp.label(), Vectors.dense(discretizedFeatures));
		});

		// Create ChiSqSelector that will select top x features
		ChiSqSelector selector = new ChiSqSelector(numberFeatures);

		// Create ChiSqSelector model (selecting features)
		ChiSqSelectorModel transformer = selector.fit(discretizedData.rdd());

		String[] featureCols = config.getSetting(FSChiSqSelectorSettings.LIST_FEATURES_COL);
		int[] selectedIndices = transformer.selectedFeatures();
		String[] retVal = new String[selectedIndices.length];
		int _index = 0;
		for (int selectedIndex : selectedIndices) {
			retVal[_index++] = featureCols[selectedIndex];
		}

		return retVal;
	}

}
