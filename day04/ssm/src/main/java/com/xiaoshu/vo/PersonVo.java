package com.xiaoshu.vo;

import com.xiaoshu.entity.Person;

public class PersonVo extends Person{
	
	private String companyName;
	private Integer num;
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	

}
