package kr.gaion.ceh.web.interfaces;

/**
 * for DB connection
 * 
 * @author hoang
 *
 */
public interface IDbOperations {
	public <T> T select(IQueryConditions query);

	public <T> boolean insert(IQueryConditions query, T data);

	public boolean delete(IQueryConditions query);
}
