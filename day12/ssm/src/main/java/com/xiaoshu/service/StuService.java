package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.StuMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Stu;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;
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
//	public User existUserWithUserName(String userName) throws Exception {
//		UserExample example = new UserExample();
//		Criteria criteria = example.createCriteria();
//		criteria.andUsernameEqualTo(userName);
//		List<User> userList = userMapper.selectByExample(example);
//		return userList.isEmpty()?null:userList.get(0);
//	};

	public PageInfo<StuVo> findStuPage(StuVo stuVo, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<StuVo> userList = stuMapper.findAll(stuVo);
		PageInfo<StuVo> pageInfo = new PageInfo<StuVo>(userList);
		return pageInfo;
	}


	public void add(Stu stu) {
		stuMapper.insert(stu);
	}


	public void update(Stu stu) {
		stuMapper.updateByPrimaryKey(stu);
	}


	public List<StuVo> tongji() {
		return stuMapper.tongji();
	}

}
