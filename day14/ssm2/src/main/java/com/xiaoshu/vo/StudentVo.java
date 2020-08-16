package com.xiaoshu.vo;

import com.xiaoshu.entity.Student;

public class StudentVo extends Student{
	
	private String tName;
	
	private Integer num;

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
	
}
