package com.xiaoshu.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Device;
import com.xiaoshu.entity.DeviceType;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.service.DeviceService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;
import com.xiaoshu.vo.DeviceVo;

@Controller
@RequestMapping("device")
public class DeviceController extends LogController{
	static Logger logger = Logger.getLogger(DeviceController.class);

	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private RoleService roleService ;
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("deviceIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Role> roleList = roleService.findRole(new Role());
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		List<DeviceVo> deviceTypes = deviceService.findDevicePage(new DeviceVo(), 0, 100, null, null).getList();
		request.setAttribute("tList", deviceTypes);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "device";
	}
	
	
	@RequestMapping(value="deviceList",method=RequestMethod.POST)
	public void userList(DeviceVo deviceVo,HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<DeviceVo> userList= deviceService.findDevicePage(deviceVo, pageNum, pageSize, null, null);
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",userList.getTotal() );
			jsonObj.put("rows", userList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户展示错误",e);
			throw e;
		}
	}
	
	
	// 新增或修改
	@RequestMapping("reserveDevice")
	public void reserveDevice(HttpServletRequest request,Device device,HttpServletResponse response){
		Integer userId = device.getDeviceid();
		JSONObject result=new JSONObject();
		try {
			if (userId != null) {   // userId不为空 说明是修改
				if(deviceService.existUserWithDeviceName(device.getDevicename())==null){
					device.setDeviceid(userId);
					deviceService.update(device);
					result.put("success", true);
				}else{
					if(deviceService.existUserWithDeviceName(device.getDevicename()).getDeviceid()==userId){
						deviceService.update(device);
						result.put("success", true);
					}else {
						result.put("success", true);
						result.put("errorMsg", "该用户名被使用");
					}
				}
				
			}else {   // 添加
				if(deviceService.existUserWithDeviceName(device.getDevicename())==null){  // 没有重复可以添加
					deviceService.add(device);
					result.put("success", true);
				} else {
					result.put("success", true);
					result.put("errorMsg", "该用户名被使用");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存用户信息错误",e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	
	@RequestMapping("deleteDevice")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
				deviceService.delete(Integer.parseInt(id));
			}
			result.put("success", true);
			result.put("delNums", ids.length);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	
	@RequestMapping("export")
	public void export(HttpServletRequest request,HttpServletResponse response){
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet Sheet = workbook.createSheet();
		XSSFRow row = Sheet.createRow(0);
		
		row.createCell(0).setCellValue("设备编号");
		row.createCell(1).setCellValue("设备名称");
		row.createCell(2).setCellValue("设备类型名称");
		row.createCell(3).setCellValue("内存");
		row.createCell(4).setCellValue("机身颜色");
		row.createCell(5).setCellValue("价格");
		row.createCell(6).setCellValue("状态");
		row.createCell(7).setCellValue("创建时间");
		
		List<DeviceVo> list = deviceService.findDevicePage(new DeviceVo(), 0, 100, null, null).getList();
		
		for (int i = 0; i < list.size(); i++) {
			row = Sheet.createRow(i+1);
			row.createCell(0).setCellValue(list.get(i).getDeviceid());
			row.createCell(1).setCellValue(list.get(i).getDevicename());
			row.createCell(2).setCellValue(list.get(i).getTypename());
			row.createCell(3).setCellValue(list.get(i).getDeviceram());
			row.createCell(4).setCellValue(list.get(i).getColor());
			row.createCell(5).setCellValue(list.get(i).getPrice());
			row.createCell(6).setCellValue(list.get(i).getStatus());
			String format = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getCreatetime());
			row.createCell(7).setCellValue(format);
		}
		
		//导出
		try {
			response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("设备列表.xlsx", "UTF-8"));
			response.setHeader("Connection", "close");
			response.setHeader("Content-Type", "application/octet-stream");
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
		}
		
	}
	
}
