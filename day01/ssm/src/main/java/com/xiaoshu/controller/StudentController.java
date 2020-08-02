package com.xiaoshu.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
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
import com.xiaoshu.entity.Course;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.Student;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.StudentService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;
import com.xiaoshu.vo.StudentVo;

@Controller
@RequestMapping("student")
public class StudentController extends LogController{
	static Logger logger = Logger.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;
	
	@Autowired
	private RoleService roleService ;
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("studentIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Role> roleList = roleService.findRole(new Role());
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		List<Student> students = studentService.findGrade();
		List<Course> courses = studentService.findCourse();
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		request.setAttribute("sList", students);
		request.setAttribute("cList", courses);
		return "student";
	}
	
	
	@RequestMapping(value="studentList",method=RequestMethod.POST)
	public void userList(StudentVo student,HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<StudentVo> userList= studentService.findStudentPage(student, pageNum, pageSize, null, null);
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
	@RequestMapping("reserveStudent")
	public void reserveUser(HttpServletRequest request,Student student,HttpServletResponse response){
		Integer userId = student.getId();
		JSONObject result=new JSONObject();
		try {
			if (userId != null) {   // userId不为空 说明是修改
				Student s = studentService.existStudentWithSname(student.getSname());
				if(s==null){
					student.setId(userId);
					studentService.updateStudent(student);
					result.put("success", true);
				}else{
					if(s.getId()==userId){
						student.setId(userId);
						studentService.updateStudent(student);
						result.put("success", true);
					}else {
						result.put("success", true);
						result.put("errorMsg", "该用户名被使用");
					}
				}
				
			}else {   // 添加
				if(studentService.existStudentWithSname(student.getSname())==null){  // 没有重复可以添加
					studentService.addStudnet(student);
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
	
	
	@RequestMapping("deleteStudent")
	public void delUser(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			String[] ids=request.getParameter("ids").split(",");
			for (String id : ids) {
				studentService.deleteStudent(Integer.parseInt(id));
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
	public void exportStudent(HttpServletResponse response){
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow row = sheet.createRow(0);
		
		row.createCell(0).setCellValue("学生编号");
		row.createCell(1).setCellValue("学生姓名");
		row.createCell(2).setCellValue("学生年龄");
		row.createCell(3).setCellValue("年级");
		row.createCell(4).setCellValue("专业");
		row.createCell(5).setCellValue("入学时间");
		
		PageInfo<StudentVo> findStudentPage = studentService.findStudentPage(null, 0, 100, null, null);
		List<StudentVo> list = findStudentPage.getList();
		
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(list.get(i).getId());
			row.createCell(1).setCellValue(list.get(i).getSname());
			row.createCell(2).setCellValue(list.get(i).getAge());
			row.createCell(3).setCellValue(list.get(i).getGrade());
			row.createCell(4).setCellValue(list.get(i).getCname());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String date = format.format(list.get(i).getCreatetime());
			row.createCell(5).setCellValue(date);
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
	public String importStudent(MultipartFile file) throws ParseException{
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheetAt = workbook.getSheetAt(0);
			int lastRowNum = sheetAt.getLastRowNum();
			for (int i = 0; i < lastRowNum; i++) {
				String sname = sheetAt.getRow(i+1).getCell(1).toString();
				String a = sheetAt.getRow(i+1).getCell(2).toString();
				int age = (int) Double.parseDouble(a);
				String grade = sheetAt.getRow(i+1).getCell(3).toString();
				String zhuanye = sheetAt.getRow(i+1).getCell(4).toString();
				int courseid = studentService.findCourseidByCname(zhuanye);
				String date = sheetAt.getRow(i+1).getCell(5).toString();
				Date createtime = new SimpleDateFormat("yyyy-MM-dd").parse(date);
				Student student = new Student(null, courseid, sname, age, null, grade, null, createtime);
				studentService.addStudnet(student);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:studentIndex.htm?menuid=15";
	}
	
	@RequestMapping("tongji")
	public void tongji(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		List<StudentVo> tongji = studentService.tongji();
		try {
			result.put("tongji", tongji);
		} catch (Exception e) {
			e.printStackTrace();
		}
		WriterUtil.write(response, result.toString());
	}
	
}
