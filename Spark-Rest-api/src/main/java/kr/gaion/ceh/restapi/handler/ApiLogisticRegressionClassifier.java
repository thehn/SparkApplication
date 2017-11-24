package kr.gaion.ceh.restapi.handler;

import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.restapi.algorithms.LogisticRegressionClassifier;
import kr.gaion.ceh.restapi.interfaces.IPredictable;
import kr.gaion.ceh.restapi.interfaces.ITrainable;

/**
 * 
 * @author hoang
 *
 */
public class ApiLogisticRegressionClassifier implements ITrainable, IPredictable {

	@Override
	public IResponseObject predict(IConfigurable config) throws Exception {
		return LogisticRegressionClassifier.predict(config);
	}

	@Override
	public IResponseObject train(IConfigurable config) throws Exception {
		return LogisticRegressionClassifier.train(config);
	}

}
