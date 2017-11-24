package kr.gaion.ceh.common.bean.settings;

/**
 * Specify format of the data
 * <li> SPARSE
 * <li> DENSE
 * 
 * @author hoang
 *
 */
public enum DataFormat {
	/**
	 * Basket data format - for pattern mining
	 */
	BASKET,
	/**
	 * LIBSVM uses the so called "sparse" format where zero
	 * values do not need to be stored. Hence a data with attributes 1 0 2 0 is
	 * represented as 1:1 3:2 ...
	 */
	SPARSE,

	/**
	 * dense format: contains all the attributes
	 */
	DENSE,
	
	/**
	 * data from CSV file
	 */
	CSV;
}
