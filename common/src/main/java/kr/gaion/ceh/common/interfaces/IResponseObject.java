package kr.gaion.ceh.common.interfaces;

public interface IResponseObject {
	public <T> T getResponse(String name);
	public <T> void set(String key, T value, Class<?> valueType);
}
