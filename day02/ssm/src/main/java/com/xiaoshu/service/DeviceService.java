package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.DeviceMapper;
import com.xiaoshu.dao.DeviceTypeMapper;
import com.xiaoshu.entity.Device;
import com.xiaoshu.entity.DeviceExample;
import com.xiaoshu.entity.DeviceExample.Criteria;
import com.xiaoshu.entity.User;
import com.xiaoshu.vo.DeviceVo;

@Service
public class DeviceService {

	@Autowired
	DeviceMapper deviceMapper;
	
	@Autowired
	DeviceTypeMapper deviceTypeMapper;



	public PageInfo<DeviceVo> findDevicePage(DeviceVo deviceVo, int pageNum, int pageSize, String ordername, String order) {
		PageHelper.startPage(pageNum, pageSize);
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"deviceid";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		List<DeviceVo> userList = deviceMapper.finAll();
		PageInfo<DeviceVo> pageInfo = new PageInfo<DeviceVo>(userList);
		return pageInfo;
	}

	// 通过用户名判断是否存在，（新增时不能重名）
		public Device existUserWithDeviceName(String name) throws Exception {
			DeviceExample example = new DeviceExample();
			Criteria criteria = example.createCriteria();
			criteria.andDevicenameEqualTo(name);
			List<Device> userList = deviceMapper.selectByExample(example);
			return userList.isEmpty()?null:userList.get(0);
		}

		public void add(Device device) {
			deviceMapper.insert(device);
		}

		public void delete(Integer id) {
			deviceMapper.deleteByPrimaryKey(id);
		}

		public void update(Device device) {
			deviceMapper.updateByPrimaryKey(device);
		};


}
