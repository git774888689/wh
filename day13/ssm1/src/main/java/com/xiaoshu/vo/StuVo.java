package com.xiaoshu.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.xiaoshu.entity.Stu;

public class StuVo extends Stu{
	private String mName;
	
	private Integer num;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
    private Date start;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
    private Date end;

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

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

}
