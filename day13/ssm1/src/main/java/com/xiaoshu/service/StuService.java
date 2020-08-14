package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.dao.StuMapper;
import com.xiaoshu.entity.Stu;
import com.xiaoshu.entity.StuExample;
import com.xiaoshu.entity.StuExample.Criteria;
import com.xiaoshu.vo.StuVo;

@Service
public class StuService {

	@Autowired
	StuMapper stuMapper;


	// 删除
	public void delete(Integer id) throws Exception {
		stuMapper.deleteByPrimaryKey(id);
	};


	// 通过用户名判断是否存在，（新增时不能重名）
	public Stu existUserWithUserName(String name) throws Exception {
		StuExample example = new StuExample();
		Criteria criteria = example.createCriteria();
		criteria.andSNameEqualTo(name);
		List<Stu> userList = stuMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	};

	public PageInfo<StuVo> findUserPage(StuVo stuVo, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<StuVo> userList = stuMapper.findAll(stuVo);
		PageInfo<StuVo> pageInfo = new PageInfo<StuVo>(userList);
		return pageInfo;
	}


	public void update(Stu stu) {
		stuMapper.updateByPrimaryKey(stu);
	}


	public void add(Stu stu) {
		stuMapper.insert(stu);
	}


	public List<StuVo> tongji() {
		return stuMapper.tongji();
	}


}
