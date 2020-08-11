package com.xiaoshu.vo;

import com.xiaoshu.entity.Student;

public class StudentVo extends Student{
	private String cName;
	private Integer num;
	
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	

}
