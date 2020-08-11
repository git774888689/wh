package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoshu.dao.GoodsTypeMapper;
import com.xiaoshu.entity.GoodsType;

@Service
public class GoodsTypeService {

	@Autowired
	GoodsTypeMapper typeMapper;
	
	public List<GoodsType> findType() {
		return typeMapper.selectAll();
	}
}
