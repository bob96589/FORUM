package com.bob.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SqlStatement {

	final Map<String, Object> parameter;

	public SqlStatement(Map<String, Object> map) {
		this.parameter = map;
	}

	public SqlStatement(Collection<Map<String, Object>> collection) {
		this.parameter = new HashMap<String, Object>();
		for (Map<String, Object> map : collection) {
			parameter.putAll(map);
		}
	}

	public Map<String, Object> getParameter() {
		return parameter;
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(String key) {
		if (parameter.containsKey(key)) {
			Object obj = parameter.get(key);
			return (T) obj;
		}
		return null;
	}

	public boolean containsKey(String key) {
		return parameter.containsKey(key);
	}
}
