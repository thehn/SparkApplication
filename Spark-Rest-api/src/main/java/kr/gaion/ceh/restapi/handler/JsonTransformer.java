package kr.gaion.ceh.restapi.handler;

import com.google.gson.Gson;

import spark.ResponseTransformer;

/**
 * 
 * @author hoang
 *
 */
public class JsonTransformer implements ResponseTransformer {
	private Gson gson = new Gson();

	@Override
	public String render(Object model) throws Exception {
		return gson.toJson(model);
	}

}
