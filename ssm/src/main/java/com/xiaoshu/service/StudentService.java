package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.CourseMapper;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.entity.Course;
import com.xiaoshu.entity.CourseExample;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentExample;
import com.xiaoshu.entity.StudentExample.Criteria;
import com.xiaoshu.vo.StudentVo;

@Service
public class StudentService {

	@Autowired
	StudentMapper studentMapper;
	
	@Autowired
	CourseMapper courseMapper;
	
	public PageInfo<StudentVo> findStudentPage(StudentVo student, int pageNum, int pageSize, String ordername, String order) {
		PageHelper.startPage(pageNum, pageSize);
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"id";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		
		List<StudentVo> userList = studentMapper.findAll(student);
		PageInfo<StudentVo> pageInfo = new PageInfo<StudentVo>(userList);
		return pageInfo;
	}

	public List<Student> findGrade() {
		// TODO Auto-generated method stub
		return studentMapper.selectAll();
	}

	public List<Course> findCourse() {
		
		return courseMapper.selectAll();
	}
	
	// 通过用户名判断是否存在，（新增时不能重名）
		public Student existStudentWithSname(String sname) throws Exception {
			StudentExample example = new StudentExample();
			Criteria criteria = example.createCriteria();
			criteria.andSnameEqualTo(sname);
			List<Student> userList = studentMapper.selectByExample(example);
			return userList.isEmpty()?null:userList.get(0);
		}

		public void addStudnet(Student student) {
			studentMapper.insert(student);
		}

		public void deleteStudent(Integer id) {
			studentMapper.deleteByPrimaryKey(id);
		}

		public void updateStudent(Student student) {
			studentMapper.updateByPrimaryKey(student);
		}

		public int findCourseidByCname(String zhuanye) {
			CourseExample example = new CourseExample();
			com.xiaoshu.entity.CourseExample.Criteria criteria = example.createCriteria();
			criteria.andCnameEqualTo(zhuanye);
			return courseMapper.selectAll().get(0).getId();
		}

		public List<StudentVo> tongji() {
			return studentMapper.tongji();
		};


}
