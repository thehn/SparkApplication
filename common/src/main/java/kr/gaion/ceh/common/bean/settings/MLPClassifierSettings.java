package kr.gaion.ceh.common.bean.settings;

public class MLPClassifierSettings extends SupervisedAlgorithm {

	/*
	 * Keys represent for field
	 */
	public final static String ALGORITHM_NAME = "MLPClassifier";
	public final static String BLOCK_SIZE = "blockSize";
	public final static String MAX_ITER = "maxIter";
	public final static String LAYERS = "layers";

	/*
	 * default values
	 */
	public static final int DEFAULT_SIZE_INTER_LAYER_1 = 6;
	public static final int DEFAULT_SIZE_INTER_LAYER_2 = 8;
	public static final int DEFAULT_BLOCK_SIZE = 128;
	public static final int DEFAULT_MAX_ITERATION = 10;

	/**
	 * fraction must be [0, 100.0]
	 */
	protected int numFeatures;
	protected int numClasses;
	protected int sizeInterLayer1;
	protected int sizeInterLayer2;
	protected int blockSize;
	protected int maxIter;

	/**
	 * 
	 * @return
	 */
	public int getBlockSize() {
		if (blockSize == 0) {
			return DEFAULT_BLOCK_SIZE;
		} else
			return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getMaxIter() {
		if (maxIter == 0) {
			return DEFAULT_MAX_ITERATION;
		} else
			return maxIter;
	}

	public void setMaxIter(int maxIter) {
		this.maxIter = maxIter;
	}

	public int getNumFeatures() {
		return numFeatures;
	}

	public void setNumFeatures(int numFeatures) {
		this.numFeatures = numFeatures;
	}

	public int getNumClasses() {
		return numClasses;
	}

	public void setNumClasses(int numClasses) {
		this.numClasses = numClasses;
	}

	public int getSizeInterLayer1() {
		if (sizeInterLayer1 == 0) {
			return DEFAULT_SIZE_INTER_LAYER_1;
		} else
			return sizeInterLayer1;
	}

	public void setSizeInterLayer1(int sizeInterLayer1) {
		this.sizeInterLayer1 = sizeInterLayer1;
	}

	public int getSizeInterLayer2() {
		if (sizeInterLayer2 == 0) {
			return DEFAULT_SIZE_INTER_LAYER_2;
		} else
			return sizeInterLayer2;
	}

	public void setSizeInterLayer2(int sizeInterLayer2) {
		this.sizeInterLayer2 = sizeInterLayer2;
	}

	/**
	 * to get layers
	 * 
	 * @return
	 */
	public int[] getLayers() {
		return new int[] { getNumFeatures(), getNumClasses(), getSizeInterLayer1(), getSizeInterLayer2() };
	}

}
