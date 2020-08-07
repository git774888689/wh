package com.xiaoshu.controller;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.ExpressCompany;
import com.xiaoshu.entity.ExpressPerson;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.service.ExpressPersonService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;
import com.xiaoshu.vo.ExpressPersonVo;

@Controller
@RequestMapping("expressPerson")
public class ExpressPersonController extends LogController{
	static Logger logger = Logger.getLogger(ExpressPersonController.class);

	@Autowired
	private ExpressPersonService expressPersonService;
	
	@Autowired
	private RoleService roleService ;
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("expressPersonIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Role> roleList = roleService.findRole(new Role());
		List<ExpressCompany> cList = expressPersonService.findCompany(); 
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("cList", cList);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "expressPerson";
	}
	
	
	@RequestMapping(value="expressPersonList",method=RequestMethod.POST)
	public void userList(ExpressPersonVo expressPersonVo,HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<ExpressPersonVo> userList= expressPersonService.findUserPage(expressPersonVo, pageNum, pageSize, null, null);		
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
	@RequestMapping("reservePerson")
	public void reserveUser(ExpressPerson expressPerson,HttpServletRequest request,HttpServletResponse response){
		Integer userId = expressPerson.getId();
		JSONObject result=new JSONObject();
		try {
			if (userId != null) {   // userId不为空 说明是修改
					expressPerson.setId(userId);
					expressPersonService.update(expressPerson);
					result.put("success", true);
			}else {   // 添加
					expressPersonService.add(expressPerson);
					result.put("success", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存用户信息错误",e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	
	@RequestMapping("deletePerson")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
				expressPersonService.delete(Integer.parseInt(id));
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
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("员工编号");
		row.createCell(1).setCellValue("员工姓名");
		row.createCell(2).setCellValue("入职时间");
		row.createCell(3).setCellValue("性别");
		row.createCell(4).setCellValue("特点");
		row.createCell(5).setCellValue("快递公司");
		row.createCell(6).setCellValue("公司状态");
		
		List<ExpressPersonVo> list = expressPersonService.findUserPage(new ExpressPersonVo(), 0, 100, null, null).getList();
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(list.get(i).getId());
			row.createCell(1).setCellValue(list.get(i).getExpressName());
			String format = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getCreateTime());
			row.createCell(2).setCellValue(format);
			row.createCell(3).setCellValue(list.get(i).getSex());
			row.createCell(4).setCellValue(list.get(i).getExpressTrait());
			row.createCell(5).setCellValue(list.get(i).getcName());
			row.createCell(6).setCellValue(list.get(i).getStatus());
		}
		
		//导出
		try {
			response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("快递员工列表.xlsx", "UTF-8"));
			response.setHeader("Connection", "close");
			response.setHeader("Content-Type", "application/octet-stream");
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping("import")
	public String importPerson(MultipartFile file){
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheetAt = workbook.getSheetAt(0);
			int lastRowNum = sheetAt.getLastRowNum();
			for (int i = 0; i < lastRowNum; i++) {
				//名
				String expressName = sheetAt.getRow(i+1).getCell(1).toString();
				//时间
				String date = sheetAt.getRow(i+1).getCell(2).toString();
				//性别
				String sex = sheetAt.getRow(i+1).getCell(3).toString();
				//特点
				String expressTrait = sheetAt.getRow(i+1).getCell(4).toString();
				//快递公司
				String cName = sheetAt.getRow(i+1).getCell(5).toString();
				Integer expressTypeId = expressPersonService.findCompanyId(cName);
				Date createTime = new SimpleDateFormat("yyyy-MM-dd").parse(date);
				ExpressPerson expressPerson = new ExpressPerson(null, expressName, sex, expressTrait, createTime, expressTypeId);
				expressPersonService.add(expressPerson);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:expressPersonIndex.htm?menuid=10";
	}
	
}
