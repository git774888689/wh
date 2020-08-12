package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoshu.dao.ContentCategoryMapper;
import com.xiaoshu.entity.ContentCategory;
import com.xiaoshu.entity.ContentCategoryExample;
import com.xiaoshu.entity.ContentCategoryExample.Criteria;

@Service
public class ContentCategoryService {

	@Autowired
	ContentCategoryMapper contentCategoryMapper;

	public List<ContentCategory> findCategory() {
		return contentCategoryMapper.findCategory();
	}

	public Integer findIdByName(String contentcategoryidTo) {
		List<ContentCategory> cList = contentCategoryMapper.findIdByName(contentcategoryidTo);
		return cList.get(0).getContentcategoryid();
	}
	
	

}
