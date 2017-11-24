package kr.gaion.ceh.restapi.handler;

import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.restapi.algorithms.SvcClassifier;
import kr.gaion.ceh.restapi.interfaces.IPredictable;
import kr.gaion.ceh.restapi.interfaces.ITrainable;

/**
 * 
 * @author hoang
 *
 */
public class ApiSvcClassifier implements ITrainable, IPredictable {

	@Override
	public IResponseObject predict(IConfigurable config) throws Exception {
		return SvcClassifier.predict(config);
	}

	@Override
	public IResponseObject train(IConfigurable config) throws Exception {
		return SvcClassifier.train(config);
	}

}