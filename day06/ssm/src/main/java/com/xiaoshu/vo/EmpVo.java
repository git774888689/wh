package com.xiaoshu.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.xiaoshu.entity.Emp;

public class EmpVo extends Emp{
	private String dName;
	//必须是util下的
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date start;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date end;

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getdName() {
		return dName;
	}

	public void setdName(String dName) {
		this.dName = dName;
	}

}
