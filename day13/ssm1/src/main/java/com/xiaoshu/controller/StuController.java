package com.xiaoshu.controller;

import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateFormatUtils;
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
	private MajorMapper majorMapper;
	
	@Autowired
	private RoleService roleService ;
	
	@Autowired
	private OperationService operationService;
	
	
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
			PageInfo<StuVo> userList= stuService.findUserPage(stuVo, pageNum, pageSize);
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
	public void reserveUser(HttpServletRequest request,Stu stu,HttpServletResponse response){
		Integer userId = stu.getsId();
		JSONObject result=new JSONObject();
		try {
			if (userId != null) {   // userId不为空 说明是修改
				if(stuService.existUserWithUserName(stu.getsName())==null){
					stu.setsId(userId);
					stuService.update(stu);
					result.put("success", true);
				}else{//判断修改的是否是自己
					if (stuService.existUserWithUserName(stu.getsName()).getsId()==stu.getsId()) {
						stu.setsId(userId);
						stuService.update(stu);
						result.put("success", true);
					} else {
						result.put("success", true);
						result.put("errorMsg", "该用户名被使用");
					}
					
				}
				
			}else {   // 添加
				if(stuService.existUserWithUserName(stu.getsName())==null){  // 没有重复可以添加
					stuService.add(stu);
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
		String[] a = {"编号","学生姓名","性别","爱好","生日","专业"};
		for (int i = 0; i < a.length; i++) {
			row.createCell(i).setCellValue(a[i]);
		}
		List<StuVo> list = stuService.findUserPage(new StuVo(), 0, 100).getList();
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(list.get(i).getsId());
			row.createCell(1).setCellValue(list.get(i).getsName());
			row.createCell(2).setCellValue(list.get(i).getsSex());
			row.createCell(3).setCellValue(list.get(i).getsHobby());
			String date = DateFormatUtils.format(list.get(i).getsBirth(), "yyyy-MM-dd");
			row.createCell(4).setCellValue(date);
			row.createCell(5).setCellValue(list.get(i).getmName());
		}
		try {
			//导出
			response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("学生列表.xlsx", "UTF-8"));
			response.setHeader("Connection", "close");
			response.setHeader("Content-Type", "application/octet-stream");
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出用户信息错误",e);
		}
	}
	
	@RequestMapping("import")
	public String importStu(MultipartFile file,HttpServletRequest request,HttpServletResponse response){
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheetAt = workbook.getSheetAt(0);
			int lastRowNum = sheetAt.getLastRowNum();
			for (int i = 1; i <= lastRowNum; i++) {
				 String sName = sheetAt.getRow(i).getCell(1).toString();
				 String sSex = sheetAt.getRow(i).getCell(2).toString();
				 String sHobby = sheetAt.getRow(i).getCell(3).toString();
				 String sBirthTo = sheetAt.getRow(i).getCell(4).toString();
				 Date sBirth = DateUtil.parseYYYYMMDDDate(sBirthTo);
				 String mName = sheetAt.getRow(i).getCell(5).toString();
				 Integer mId = findMidByMname(mName);
				 Stu stu = new Stu(null, sName, sSex, sHobby, sBirth, mId);
				 stuService.add(stu);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入用户信息错误",e);
		}
		return "redirect:stuIndex.htm?menuid=10";
	}
//	根据mName查到major里的mId
	private Integer findMidByMname(String mName) {
		MajorExample example = new MajorExample();
		Criteria criteria = example.createCriteria();
		criteria.andMNameEqualTo(mName);
		return majorMapper.selectByExample(example).get(0).getmId();
	}
	
	
	@RequestMapping("tongji")
	public void tongji(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			List<StuVo> tongji = stuService.tongji();
			result.put("tongji", tongji);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("统计用户信息错误",e);
			result.put("errorMsg", "对不起，统计失败");
		}
		WriterUtil.write(response, result.toString());
	}
}
