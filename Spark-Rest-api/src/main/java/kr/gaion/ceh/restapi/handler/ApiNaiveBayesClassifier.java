package kr.gaion.ceh.restapi.handler;

import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.restapi.algorithms.NaiveBayesClassifier;
import kr.gaion.ceh.restapi.interfaces.IPredictable;
import kr.gaion.ceh.restapi.interfaces.ITrainable;

public class ApiNaiveBayesClassifier implements ITrainable, IPredictable {

	@Override
	public IResponseObject predict(IConfigurable config) throws Exception {
		return NaiveBayesClassifier.predict(config);
	}

	@Override
	public IResponseObject train(IConfigurable config) throws Exception {
		return NaiveBayesClassifier.train(config);
	}

}