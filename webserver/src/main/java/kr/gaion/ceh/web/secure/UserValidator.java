package kr.gaion.ceh.web.secure;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import kr.gaion.ceh.common.Constants;

public class UserValidator {

	private BCryptPasswordEncoder pwdEncoder;

	public UserValidator() {
		pwdEncoder = new BCryptPasswordEncoder();
	}

	public boolean isUserValid(String id, String password) {
		if (id == Constants.ADMIN_USR && pwdEncoder.matches(password, Constants.ADMIN_PWD)) {
			return true;
		} else {
			return false;
		}
	}
}
