package kr.gaion.ceh.restapi.handler;

import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.restapi.algorithms.FSChiSqSelector;
import kr.gaion.ceh.restapi.interfaces.ITrainable;

public class ApiFSChiSqSelector implements ITrainable {

	@Override
	public IResponseObject train(IConfigurable config) throws Exception {
		return FSChiSqSelector.train(config);
	}

}
