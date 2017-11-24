package kr.gaion.ceh.restapi.interfaces;

import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;

public interface IPredictable {
	/**
	 * To predict, using trained model
	 * @param config
	 * @return
	 * @throws Exception 
	 */
	public IResponseObject predict(IConfigurable config) throws Exception;
}
