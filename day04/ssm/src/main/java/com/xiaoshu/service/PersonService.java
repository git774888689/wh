package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.CompanyMapper;
import com.xiaoshu.dao.PersonMapper;
import com.xiaoshu.entity.Company;
import com.xiaoshu.entity.Person;
import com.xiaoshu.vo.PersonVo;

@Service
public class PersonService {

	@Autowired
	PersonMapper personMapper;
	
	@Autowired
	CompanyMapper companyMapper;



//	// 通过用户名判断是否存在，（新增时不能重名）
//	public User existUserWithUserName(String userName) throws Exception {
//		UserExample example = new UserExample();
//		Criteria criteria = example.createCriteria();
//		criteria.andUsernameEqualTo(userName);
//		List<User> userList = userMapper.selectByExample(example);
//		return userList.isEmpty()?null:userList.get(0);
//	};


	public PageInfo<PersonVo> findUserPage(PersonVo personVo, int pageNum, int pageSize, String ordername, String order) {
		PageHelper.startPage(pageNum, pageSize);
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"id";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		List<PersonVo> userList = personMapper.findAll(personVo);
		PageInfo<PersonVo> pageInfo = new PageInfo<PersonVo>(userList);
		return pageInfo;
	}


	public List<Company> findCompany() {
		return companyMapper.selectAll();
	}


	public void add(Person person) {
		personMapper.insert(person);
	}


	public void update(Person person) {
		personMapper.updateByPrimaryKey(person);
	}


	public void delete(Integer id) {
		personMapper.deleteByPrimaryKey(id);
	}


}
