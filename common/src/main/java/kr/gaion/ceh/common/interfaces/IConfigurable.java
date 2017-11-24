package kr.gaion.ceh.common.interfaces;

/**
 * All classes use to pass settings from client to Spark Rest-API have to
 * implement this interface
 * 
 * @author hoang
 *
 */
public interface IConfigurable {

	/**
	 * To get setting value from key
	 * 
	 * @param key
	 * @return
	 */
	public <T> T getSetting(String key);

	/**
	 * to set value for key
	 * 
	 * @param key
	 * @param value
	 */
	public <T> void set(String key, T value);
}
