package kr.gaion.ceh.restapi.interfaces;

/**
 * 
 * @author hoang
 *
 */
public interface IQueryable {
	public <T> T get(String name);
}
