package com.xiaoshu.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.dao.BankMapper;
import com.xiaoshu.entity.Bank;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Person;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.PersonService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.UserService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;
import com.xiaoshu.vo.PersonVo;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("person")
public class PersonController extends LogController{
	static Logger logger = Logger.getLogger(PersonController.class);

	@Autowired
	private PersonService personService;
	
	@Autowired
	private BankMapper bankMapper;
	
	@Autowired
	private RoleService roleService ;
	
	@Autowired
	private OperationService operationService;
	
	
	@RequestMapping("personIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Role> roleList = roleService.findRole(new Role());
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		List<Bank> bList = bankMapper.selectAll();
		request.setAttribute("bList", bList);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "person";
	}
	
	
	@RequestMapping(value="personList",method=RequestMethod.POST)
	public void userList(PersonVo personVo,HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<PersonVo> userList= personService.findUserPage(personVo, pageNum, pageSize, null, null);
			
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
	public void reserveUser(Person person,HttpServletRequest request,User user,HttpServletResponse response){
		Integer userId = person.getpId();
		JSONObject result=new JSONObject();
		try {
			if(userId!=null){
				user.setUserid(userId);
				personService.update(person);
				result.put("success", true);
				
			}else {   // 添加
				personService.add(person);
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
				personService.delete(Integer.parseInt(id));
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
	
	@RequestMapping("import")
	public String importPerson(MultipartFile file,HttpServletRequest request,HttpServletResponse response){
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet sheetAt = workbook.getSheetAt(0);
			int lastRowNum = sheetAt.getLastRowNum();
			for (int i = 0; i <= lastRowNum; i++) {
				String pName = sheetAt.getRow(i).getCell(1).toString();
				String pSex = sheetAt.getRow(i).getCell(2).toString();
				String pLike = sheetAt.getRow(i).getCell(3).toString();
				String pCountTo = sheetAt.getRow(i).getCell(4).toString();
				Integer pCount = (int) Double.parseDouble(pCountTo);
				String pAgeTo = sheetAt.getRow(i).getCell(5).toString();
				Integer pAge = (int) Double.parseDouble(pAgeTo);
				String bName = sheetAt.getRow(i).getCell(6).toString();
				Integer bId = personService.existBankWithBName(bName).getbId();
				String createtimeTo = sheetAt.getRow(i).getCell(7).toString();
				Date createtime = DateUtil.parseYYYYMMDDDate(createtimeTo);
				Person person = new Person(null, bId, pName, pAge, pSex, createtime, pLike, pCount);
				personService.add(person);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:personIndex.htm?menuid=11";
	}
	
	
	@RequestMapping("tongji")
	public void tongji(HttpServletRequest request,HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			List<PersonVo> tongji = personService.tongji();
			result.put("tongji", tongji);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误",e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
}
