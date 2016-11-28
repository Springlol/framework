package com.zt.frame.action;

/**
 * Created by zhoutao on 2016/11/28.
 */
public class HelloAction {
	private String name;
	private String pwd;


	public String hello(){
		System.out.println("name="+name+",pwd="+pwd);
		return "success";
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
