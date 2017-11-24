package kr.gaion.ceh.restapi.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import kr.gaion.ceh.common.bean.settings.PredictionInfo;
import scala.Tuple2;
import scala.Tuple3;

/**
 * Provide the utilities relate to Prediction of algorithms
 * 
 * @author hoang
 *
 */
public class PredictionUtil {

	/**
	 * get results of predictions: includes Predicted values and Vectors
	 * 
	 * @param predictedAndVector
	 * @return
	 */
	public static <T, V> List<PredictionInfo<?, ?>> getPredictionInfo(JavaPairRDD<?, ?> predictedAndVector) {
		List<?> list = predictedAndVector.collect();
		List<PredictionInfo<?, ?>> listPredictionInfo = new ArrayList<>();
		PredictionInfo<T, V> predictionInfo = null;
		for (Object element : list) {
			predictionInfo = new PredictionInfo<>();
			@SuppressWarnings("unchecked")
			Tuple2<T, V> tuple = (Tuple2<T, V>) element;
			predictionInfo.setPredictedValue(tuple._1);
			predictionInfo.setFeatures(tuple._2);
			listPredictionInfo.add(predictionInfo);
		}

		return listPredictionInfo;
	}

	/**
	 * to get result of predictions: includes Predicted labels, Actual labels
	 * and Vectors
	 * 
	 * @param predictedLabelAndVector
	 * @return
	 */
	public static <T, V> List<PredictionInfo<?, ?>> getPredictionInfo(
			JavaRDD<Tuple3<T, T, V>> predictedLabelAndVector) {
		// TODO: add features
		List<?> list = predictedLabelAndVector.collect();
		List<PredictionInfo<?, ?>> listPredictionInfo = new ArrayList<>();
		PredictionInfo<T, V> predictionInfo = null;
		for (Object element : list) {
			predictionInfo = new PredictionInfo<>();
			@SuppressWarnings("unchecked")
			Tuple3<T, T, V> tuple = (Tuple3<T, T, V>) element;
			predictionInfo.setPredictedValue(tuple._1());
			predictionInfo.setActualValue(tuple._2());
			predictionInfo.setFeatures(tuple._3());
			listPredictionInfo.add(predictionInfo);
		}

		return listPredictionInfo;
	}
}
