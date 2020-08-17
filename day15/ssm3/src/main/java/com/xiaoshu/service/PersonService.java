package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.BankMapper;
import com.xiaoshu.dao.PersonMapper;
import com.xiaoshu.entity.Bank;
import com.xiaoshu.entity.BankExample;
import com.xiaoshu.entity.BankExample.Criteria;
import com.xiaoshu.entity.Person;
import com.xiaoshu.vo.PersonVo;

@Service
public class PersonService {

	@Autowired
	PersonMapper personMapper;
	
	@Autowired
	BankMapper bankMapper;

	// 新增
	public void add(Person t) throws Exception {
		personMapper.insert(t);
	};

	// 修改
	public void update(Person t) throws Exception {
		personMapper.updateByPrimaryKeySelective(t);
	};

	// 删除
	public void delete(Integer id) throws Exception {
		personMapper.deleteByPrimaryKey(id);
	};


	// 通过用户名判断是否存在，（新增时不能重名）
	public Bank existBankWithBName(String bName) throws Exception {
		BankExample example = new BankExample();
		Criteria criteria = example.createCriteria();
		criteria.andBNameEqualTo(bName);
		List<Bank> userList = bankMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	};


	public PageInfo<PersonVo> findUserPage(PersonVo personVo, int pageNum, int pageSize, String ordername, String order) {
		PageHelper.startPage(pageNum, pageSize);
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"pId";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		List<PersonVo> userList = personMapper.findAll(personVo);
		PageInfo<PersonVo> pageInfo = new PageInfo<PersonVo>(userList);
		return pageInfo;
	}

	public List<PersonVo> tongji() {
		return personMapper.tongji();
	}


}
