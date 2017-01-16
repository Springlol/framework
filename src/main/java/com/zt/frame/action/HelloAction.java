package com.zt.frame.action;

import java.util.Date;

/**
 * Created by zhoutao on 2016/11/28.
 */
public class HelloAction {
	private String name;
	private String pwd;
	private int age;
	private Double price;
	private Date date;

	public String hello(){
		System.out.println(this.toString());

		return "success";
	}


	public void setName(String name) {
		this.name = name;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public void setPrice(Double price){
		this.price = price;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "HelloAction{" +
				"name='" + name + '\'' +
				", pwd='" + pwd + '\'' +
				", age=" + age +
				", price=" + price +
				", date=" + date +
				'}';
	}
}
