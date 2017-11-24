package kr.gaion.ceh.restapi.algorithms.cluster4j;

import com.clust4j.algo.KMeans;
import com.clust4j.algo.KMeansParameters;
import com.clust4j.algo.KMedoids;
import com.clust4j.algo.KMedoidsParameters;
import com.clust4j.data.DataSet;
import com.clust4j.data.ExampleDataSets;

public class Sample {

	public static void main(String[] args) {
		DataSet data =  ExampleDataSets.loadIris();
		KMeans km = new KMeansParameters(4).fitNewModel(data.getDataRef());
		final int[] results = km.getLabels();
		for (int i: results) {
			System.out.print(i);
			System.out.print(",");
		}
		
		KMedoids kmd = new KMedoidsParameters(5).fitNewModel(data.getData());
		final int[] results2 = kmd.getLabels();
		System.out.println("");
		for (int i: results2) {
			System.out.print(i);
			System.out.print(",");
		}
	}

}
