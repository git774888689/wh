package com.xiaoshu.vo;

import com.xiaoshu.entity.Device;

public class DeviceVo extends Device{
	private String typename;
	private Integer num;
	
	
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	

}
