package kr.gaion.ceh.restapi.interfaces;

import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;

public interface ITrainable {
	/**
	 * To train and save model (save model to Spark-server)
	 * @param config
	 * @return
	 * @throws Exception 
	 */
	public IResponseObject train(IConfigurable config) throws Exception;
}
