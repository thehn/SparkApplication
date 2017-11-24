package kr.gaion.ceh.restapi.elasticsearch;

import java.util.HashMap;
import java.util.Map;

import kr.gaion.ceh.restapi.interfaces.IQueryable;

/**
 * this class contains all conditions for querying data from a database
 * 
 * @author hoang
 * @param <T>
 *
 */
public class EsQueryConditions<T> implements IQueryable {

	/**
	 * property
	 */
	private Map<String, Object> conditions;

	/**
	 * Constructor
	 */
	public EsQueryConditions() {
		conditions = new HashMap<>();
	}

	public void set(String name, Object value) {
		conditions.put(name, value);
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	@Override
	public <T> T get(String name) {
		return (T) conditions.get(name);
	}

	/**
	 * to clear
	 */
	public void clear() {
		this.conditions.clear();
	}
}
