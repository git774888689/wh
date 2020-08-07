package com.xiaoshu.vo;

import com.xiaoshu.entity.ExpressPerson;

public class ExpressPersonVo extends ExpressPerson{
	private String cName;
	private String status;
	private Integer num;
	
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}

}
