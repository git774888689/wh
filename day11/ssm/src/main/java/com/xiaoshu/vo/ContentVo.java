package com.xiaoshu.vo;

import com.xiaoshu.entity.Content;

public class ContentVo extends Content{
	private String categoryname;
	private Integer num;
	public String getCategoryname() {
		return categoryname;
	}
	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}

}
