package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.DeptMapper;
import com.xiaoshu.dao.EmpMapper;
import com.xiaoshu.entity.Dept;
import com.xiaoshu.entity.Emp;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;
import com.xiaoshu.vo.EmpVo;

@Service
public class EmpService {

	@Autowired
	EmpMapper empMapper;
	
	@Autowired
	DeptMapper deptMapper;

	// 删除
	public void delete(Integer id) throws Exception {
		empMapper.deleteByPrimaryKey(id);
	};


	// 通过用户名判断是否存在，（新增时不能重名）
//	public User existUserWithUserName(String userName) throws Exception {
//		UserExample example = new UserExample();
//		Criteria criteria = example.createCriteria();
//		criteria.andUsernameEqualTo(userName);
//		List<User> userList = userMapper.selectByExample(example);
//		return userList.isEmpty()?null:userList.get(0);
//	};


	public PageInfo<EmpVo> findEmpPage(EmpVo empVo, int pageNum, int pageSize, String ordername, String order) {
		PageHelper.startPage(pageNum, pageSize);
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"eId";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		List<EmpVo> userList = empMapper.findAll(empVo);
		PageInfo<EmpVo> pageInfo = new PageInfo<EmpVo>(userList);
		return pageInfo;
	}


	public List<Dept> findDept() {
		return deptMapper.selectAll();
	}


	public void add(Emp emp) {
		empMapper.insert(emp);
	}


	public void update(Emp emp) {
		empMapper.updateByPrimaryKey(emp);
	}


}
