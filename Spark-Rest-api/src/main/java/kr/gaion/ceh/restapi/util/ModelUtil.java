package kr.gaion.ceh.restapi.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.settings.ModelInfo;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.elasticsearch.EsConnector;

public class ModelUtil extends ModelInfo {

	protected IConfigurable esConfig;

	/**
	 * Constructor
	 * 
	 * @param algorithmName
	 */
	public ModelUtil(String algorithmName) {
		super(algorithmName);
	}

	/**
	 * Constructor with 2 parameters
	 * 
	 * @param algorithmName
	 * @param esConfig
	 */
	public ModelUtil(String algorithmName, IConfigurable esConfig) {
		super(algorithmName);
		this.esConfig = esConfig;
	}

	/**
	 * to delete all information about specified model
	 */
	@Override
	public void deleteModel(String modelName) throws IOException {
		String rootDir = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, this.algorithmName, Constants.MODEL_DIR);
		String pathname = rootDir + File.separator + modelName;
		File model = new File(pathname);
		FileUtils.deleteDirectory(model);
		
		// delete file .info
		File modelInfo = new File(pathname + ".info");
		if(modelInfo.exists()) {
			FileUtils.forceDelete(modelInfo);
		}

		// delete data from ElasticSearch
		/*EsQueryConditions<String> query = new EsQueryConditions<>();
		query.set("index", this.algorithmName.toLowerCase());
		query.set("type", modelName);
		EsConnector esCon = EsConnector.getInstance(this.esConfig);
		esCon.delete(query);*/ // #PC0022
		EsConnector.getInstance(this.esConfig).deleteOldMlResponse(algorithmName, modelName); // #PC0022
	}

}
