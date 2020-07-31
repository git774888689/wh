package com.xiaoshu.vo;

import com.xiaoshu.entity.Student;

public class StudentVo extends Student{
	private String cname;
	private Integer num;

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public StudentVo() {
		super();
	}

	public StudentVo(String cname) {
		super();
		this.cname = cname;
	}

	@Override
	public String toString() {
		return "StudentVo [cname=" + cname + ", getId()=" + getId() + ", getCourseid()=" + getCourseid()
				+ ", getSname()=" + getSname() + ", getAge()=" + getAge() + ", getCode()=" + getCode() + ", getGrade()="
				+ getGrade() + ", getEntrytime()=" + getEntrytime() + ", getCreatetime()=" + getCreatetime()
				+ ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}
	
	
}
