package com.zt.frame.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoutao on 2016/11/28.
 */
public class Action {
	private String name;
	private String classes;
	private String method = "execute";
	private Map<String,Object> result = new HashMap<String,Object>();

	public Action() {
	}

	public Action(String name, String classes, String method) {
		this.name = name;
		this.classes = classes;
		this.method = method;
	}

	public Action(String name, String classes) {
		this.name = name;
		this.classes = classes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}
}
