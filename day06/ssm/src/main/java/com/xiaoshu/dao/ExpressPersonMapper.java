package com.xiaoshu.dao;

import java.util.List;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.ExpressPerson;
import com.xiaoshu.vo.ExpressPersonVo;

public interface ExpressPersonMapper extends BaseMapper<ExpressPerson> {

	List<ExpressPersonVo> findAll(ExpressPersonVo expressPersonVo);
}