package com.xiaoshu.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.xiaoshu.entity.Stu;

public class StuVo extends Stu{
	private String mname;
	
	private Integer num;
	
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
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	

}
