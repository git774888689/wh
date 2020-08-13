package com.xiaoshu.controller;

import java.io.IOException;
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

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.dao.MajorMapper;
import com.xiaoshu.entity.Major;
import com.xiaoshu.entity.MajorExample;
import com.xiaoshu.entity.MajorExample.Criteria;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.Stu;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.StuService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;
import com.xiaoshu.vo.StuVo;

@Controller
@RequestMapping("stu")
public class StuController extends LogController{
	static Logger logger = Logger.getLogger(StuController.class);

	@Autowired
	private StuService stuService;
	
	@Autowired
	private RoleService roleService ;
	
	@Autowired
	private OperationService operationService;
	
	@Autowired
	private MajorMapper majorMapper;
	
	
	@RequestMapping("stuIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Role> roleList = roleService.findRole(new Role());
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		List<Major> mList = majorMapper.selectAll();
		request.setAttribute("mList", mList);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "stu";
	}
	
	
	@RequestMapping(value="stuList",method=RequestMethod.POST)
	public void userList(StuVo stuVo,HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<StuVo> userList= stuService.findStuPage(stuVo, pageNum, pageSize);
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
	@RequestMapping("reserveStu")
	public void reserveUser(Stu stu,HttpServletRequest request,User user,HttpServletResponse response){
		Integer userId = stu.getSdId();
		JSONObject result=new JSONObject();
		try {
			if (userId != null) {   // userId不为空 说明是修改
					stu.setSdId(userId);
					stuService.update(stu);
					result.put("success", true);
			}else {   // 添加
					stuService.add(stu);
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
	
	
	@RequestMapping("deleteStu")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
				stuService.delete(Integer.parseInt(id));
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
		
		String[] handers = {"编号","学生姓名","学生性别","爱好","生日","专业"};
		for (int i = 0; i < handers.length; i++) {
			row.createCell(i).setCellValue(handers[i]);
		}
		
		List<StuVo> list = stuService.findStuPage(new StuVo(), 0, 100).getList();
		for (int i = 1; i < list.size(); i++) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(list.get(i).getSdId());
			row.createCell(1).setCellValue(list.get(i).getSdname());
			row.createCell(2).setCellValue(list.get(i).getSdsex());
			row.createCell(3).setCellValue(list.get(i).getSdhobby());
			String date = new SimpleDateFormat("yyyy-MM-dd").format(list.get(i).getSdbirth());
			row.createCell(4).setCellValue(date);
			row.createCell(5).setCellValue(list.get(i).getMname());
		}
		
		
		//导出
		try {
			response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("学生列表.xlsx", "UTF-8"));
			response.setHeader("Connection", "close");
			response.setHeader("Content-Type", "application/octet-stream");
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("import")
	public String importStu(MultipartFile file,HttpServletRequest request,HttpServletResponse response){
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheetAt = workbook.getSheetAt(0);
			int lastRowNum = sheetAt.getLastRowNum();
			for (int i = 1; i < lastRowNum; i++) {
				String sdname = sheetAt.getRow(i).getCell(1).toString();
				String sdsex = sheetAt.getRow(i).getCell(2).toString();
				String sdhobby = sheetAt.getRow(i).getCell(3).toString();
				String sdbirthTo = sheetAt.getRow(i).getCell(4).toString();
				Date sdbirth = DateUtil.parseYYYYMMDDDate(sdbirthTo);
				String mname = sheetAt.getRow(i).getCell(5).toString();
			//根据name查id
				MajorExample example = new MajorExample();
				Criteria criteria = example.createCriteria();
				criteria.andMdnameEqualTo(mname);
				Integer mid = majorMapper.selectByExample(example).get(0).getMdId();
				
				Stu stu = new Stu(null, sdname, sdsex, sdhobby, sdbirth, mid);
				stuService.add(stu);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:stuIndex.htm?menuid=10";
	}
	
	@RequestMapping("tongji")
	public void tongji(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			List<StuVo> tongji = stuService.tongji();
			result.put("tongji", tongji);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	@RequestMapping("addMajor")
	public String addMajor(Major major,HttpServletRequest request,HttpServletResponse response){
		majorMapper.insert(major);
		return "redirect:stuIndex.htm?menuid=10";
	}
	
}
