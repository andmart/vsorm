package br.com.toolbox.simpleorm.util;

import java.util.HashMap;

public class Session {

	private static Session instance = null;

	private HashMap<String, Object> session;

	private Session() {
		session = new HashMap<String, Object>();
	}

	public static Session getSession() {
		if (instance == null)
			instance = new Session();
		return instance;
	}

	public Object get(String key) {
		return session.get(key);
	}

	public void set(String key, Object value) {
		session.put(key, value);
	}

}
