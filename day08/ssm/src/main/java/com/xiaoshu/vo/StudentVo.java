package com.xiaoshu.vo;

import com.xiaoshu.entity.Student;

public class StudentVo extends Student{
	private String sname;
	private Integer num;
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}

}
