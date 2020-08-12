package com.xiaoshu.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Content;
import com.xiaoshu.entity.ContentCategory;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.service.ContentCategoryService;
import com.xiaoshu.service.ContentService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;
import com.xiaoshu.vo.ContentVo;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("content")
public class ContentController extends LogController{
	static Logger logger = Logger.getLogger(ContentController.class);

	@Autowired
	private ContentService contentService;
	@Autowired
	private ContentCategoryService ccs;
	
	@Autowired
	private RoleService roleService ;
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("contentIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Role> roleList = roleService.findRole(new Role());
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		List<ContentCategory> ccList = ccs.findCategory();
		request.setAttribute("ccList", ccList);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "content";
	}
	
	
	@RequestMapping(value="contentList",method=RequestMethod.POST)
	public void userList(ContentVo contentVo,HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<ContentVo> userList= contentService.findUserPage(contentVo, pageNum, pageSize);
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
	@RequestMapping("reserveContent")
	public void reserveUser(HttpServletRequest request,Content content,HttpServletResponse response){
		Integer userId = content.getContentid();
		JSONObject result=new JSONObject();
		try {
			if (userId != null) {   // userId不为空 说明是修改
					content.setContentid(userId);
					content.setCreatetime(new Date());
					contentService.update(content);
					result.put("success", true);
				
			}else {   // 添加
					content.setCreatetime(new Date());
					contentService.add(content);
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
	
	
	@RequestMapping("deleteContent")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
				contentService.delete(Integer.parseInt(id));
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
	
	
	@RequestMapping("tongji")
	public void tongji(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			List<ContentVo> tongji = contentService.tongji();
			result.put("tongji", tongji);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	@RequestMapping("export")
	public void export(HttpServletResponse response){
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("编号");
		row.createCell(1).setCellValue("广告标题");
		row.createCell(2).setCellValue("广告分类名称");
		row.createCell(3).setCellValue("图片路径");
		row.createCell(4).setCellValue("广告链接");
		row.createCell(5).setCellValue("费用");
		row.createCell(6).setCellValue("广告状态");
		row.createCell(7).setCellValue("创建时间");
		
		List<ContentVo> list = contentService.findUserPage(new ContentVo(), 0, 100).getList();
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(list.get(i).getContentid());
			row.createCell(1).setCellValue(list.get(i).getContenttile());
			row.createCell(2).setCellValue(list.get(i).getCategoryname());
			row.createCell(3).setCellValue(list.get(i).getPicpath());
			row.createCell(4).setCellValue(list.get(i).getContenturl());
			row.createCell(5).setCellValue(list.get(i).getPrice());
			row.createCell(6).setCellValue(list.get(i).getStatus());
			String date = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getCreatetime());
			row.createCell(7).setCellValue(date);
		}
		
		//导出
		try {
			response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("广告列表.xlsx", "UTF-8"));
			response.setHeader("Connection", "close");
			response.setHeader("Content-Type", "application/octet-stream");
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
