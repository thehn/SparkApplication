package kr.gaion.ceh.web.model;

public class User {

	protected String id;
	protected String password;

	public User() {
	}

	public User(String id, String password) {
		setId(id);
		setPassword(password);
	}

	public String getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
