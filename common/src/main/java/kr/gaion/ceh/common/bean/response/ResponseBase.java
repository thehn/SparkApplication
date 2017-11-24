package kr.gaion.ceh.common.bean.response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import kr.gaion.ceh.common.interfaces.IResponseObject;

/**
 * Base class for response
 * 
 * @author hoang
 *
 */

public class ResponseBase implements IResponseObject {

	/*
	 * Class constants
	 */
	public static final String STATUS = "status";
	public static final String TYPE = "type";
	public static final String MESSAGE = "message";

	/**
	 * Constructor
	 * 
	 * @param type
	 */
	public ResponseBase(ResponseType type) {
		this.status = ResponseStatus.INITIALIZED;
		this.type = type;
	}

	/**
	 * to indicate that response is [message] or [object data]
	 */
	private ResponseType type;

	/**
	 * to indicate status of running task
	 */
	private ResponseStatus status;

	private String message;

	/*
	 * Getters and Setters
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResponseType getType() {
		return type;
	}

	public void setType(ResponseType type) {
		this.type = type;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	/**
	 * to get value from object by name
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public <T> T getResponse(String name) {
		T value = null;
		String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
		Method method = null;
		try {
			method = this.getClass().getMethod(methodName, null);
			value = (T) method.invoke(this, null);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return value;
	}

	@Override
	public <T> void set(String key, T value, Class<?> valueType) {
		String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
		Method method = null;
		try {
			method = this.getClass().getMethod(methodName, new Class[] { valueType });
			method.invoke(this, value);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
