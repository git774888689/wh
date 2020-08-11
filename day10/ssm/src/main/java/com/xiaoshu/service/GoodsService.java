package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.GoodsMapper;
import com.xiaoshu.dao.GoodsTypeMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.GoodsType;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;
import com.xiaoshu.vo.GoodsVo;

@Service
public class GoodsService {

	@Autowired
	GoodsMapper goodsMapper;
	
	// 删除
	public void delete(Integer id) throws Exception {
		goodsMapper.deleteByPrimaryKey(id);
	};


	// 通过用户名判断是否存在，（新增时不能重名）
//	public User existUserWithUserName(String userName) throws Exception {
//		UserExample example = new UserExample();
//		Criteria criteria = example.createCriteria();
//		criteria.andUsernameEqualTo(userName);
//		List<User> userList = userMapper.selectByExample(example);
//		return userList.isEmpty()?null:userList.get(0);
//	};


	public PageInfo<GoodsVo> findUserPage(GoodsVo goodsVo, int pageNum, int pageSize, String ordername, String order) {
		PageHelper.startPage(pageNum, pageSize);
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"id";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		List<GoodsVo> userList = goodsMapper.findAll(goodsVo);
		PageInfo<GoodsVo> pageInfo = new PageInfo<GoodsVo>(userList);
		return pageInfo;
	}




}
