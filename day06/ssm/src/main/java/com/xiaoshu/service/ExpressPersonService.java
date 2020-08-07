package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.ExpressCompanyMapper;
import com.xiaoshu.dao.ExpressPersonMapper;
import com.xiaoshu.entity.ExpressCompany;
import com.xiaoshu.entity.ExpressCompanyExample;
import com.xiaoshu.entity.ExpressCompanyExample.Criteria;
import com.xiaoshu.entity.ExpressPerson;
import com.xiaoshu.vo.ExpressPersonVo;

@Service
public class ExpressPersonService {

	@Autowired
	ExpressPersonMapper expressPersonMapper;
	@Autowired
	ExpressCompanyMapper expressCompanyMapper;


	// 删除
	public void delete(Integer id) throws Exception {
		expressPersonMapper.deleteByPrimaryKey(id);
	};


	// 通过用户名判断是否存在，（新增时不能重名）
//	public User existUserWithUserName(String userName) throws Exception {
//		UserExample example = new UserExample();
//		Criteria criteria = example.createCriteria();
//		criteria.andUsernameEqualTo(userName);
//		List<User> userList = userMapper.selectByExample(example);
//		return userList.isEmpty()?null:userList.get(0);
//	};

	public PageInfo<ExpressPersonVo> findUserPage(ExpressPersonVo expressPersonVo, int pageNum, int pageSize, String ordername, String order) {
		PageHelper.startPage(pageNum, pageSize);
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"id";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		List<ExpressPersonVo> userList = expressPersonMapper.findAll(expressPersonVo);
		PageInfo<ExpressPersonVo> pageInfo = new PageInfo<ExpressPersonVo>(userList);
		return pageInfo;
	}


	public List<ExpressCompany> findCompany() {
		return expressCompanyMapper.selectAll();
	}


	public void add(ExpressPerson expressPerson) {
		expressPersonMapper.insert(expressPerson);
	}


	public void update(ExpressPerson expressPerson) {
		expressPersonMapper.updateByPrimaryKey(expressPerson);
	}


	public Integer findCompanyId(String cName) {
		ExpressCompanyExample example = new ExpressCompanyExample();
		Criteria criteria = example.createCriteria();
		criteria.andExpressNameEqualTo(cName);
		List<ExpressCompany> list = expressCompanyMapper.selectByExample(example);
		return list.get(0).getId();
	}


}
