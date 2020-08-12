package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.dao.ContentMapper;
import com.xiaoshu.entity.Content;
import com.xiaoshu.vo.ContentVo;

@Service
public class ContentService {

	@Autowired
	ContentMapper contentMapper;

	// 删除
	public void delete(Integer id) throws Exception {
		contentMapper.deleteByPrimaryKey(id);
	};


	// 通过用户名判断是否存在，（新增时不能重名）
//	public User existUserWithUserName(String userName) throws Exception {
//		UserExample example = new UserExample();
//		Criteria criteria = example.createCriteria();
//		criteria.andUsernameEqualTo(userName);
//		List<User> userList = userMapper.selectByExample(example);
//		return userList.isEmpty()?null:userList.get(0);
//	};

	public PageInfo<ContentVo> findUserPage(ContentVo contentVo, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<ContentVo> userList = contentMapper.findAll(contentVo);
		PageInfo<ContentVo> pageInfo = new PageInfo<ContentVo>(userList);
		return pageInfo;
	}


	public void add(Content content) {
		contentMapper.insert(content);
	}


	public void update(Content content) {
		contentMapper.updateByPrimaryKey(content);
	}


	public List<ContentVo> tongji() {
		return contentMapper.tongji();
	}

}
