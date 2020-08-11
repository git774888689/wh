package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.CourseMapper;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.entity.Course;
import com.xiaoshu.entity.Student;
import com.xiaoshu.vo.StudentVo;

@Service
public class StudentService {

	@Autowired
	StudentMapper studentMapper;
	
	@Autowired
	CourseMapper courseMapper;

	@Autowired
	private RedisTemplate redisTemplate;


	// 删除
	public void delete(Integer id) throws Exception {
		studentMapper.deleteByPrimaryKey(id);
	};

//	// 通过用户名判断是否存在，（新增时不能重名）
//	public User existUserWithUserName(String userName) throws Exception {
//		UserExample example = new UserExample();
//		Criteria criteria = example.createCriteria();
//		criteria.andUsernameEqualTo(userName);
//		List<User> userList = userMapper.selectByExample(example);
//		return userList.isEmpty()?null:userList.get(0);
//	};


	public PageInfo<StudentVo> findUserPage(StudentVo studentVo, int pageNum, int pageSize, String ordername, String order) {
		PageHelper.startPage(pageNum, pageSize);
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"id";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		List<StudentVo> userList = studentMapper.findAll(studentVo);
		PageInfo<StudentVo> pageInfo = new PageInfo<StudentVo>(userList);
		return pageInfo;
	}

	public List<Course> findCourse() {
		return courseMapper.selectAll();
	}

	public void update(Student student) {
		studentMapper.updateByPrimaryKey(student);
	}

	public void add(Student student) {
		studentMapper.insert(student);
	}

	public void addCourse(Course course) {
		courseMapper.insert(course);
		List<Course> list = courseMapper.selectAll();
		Course course2 = list.get(list.size()-1);
		System.out.println(course2.getId()+course2.getCname());
		redisTemplate.boundHashOps("courseList").put(course2.getId()+"", course2.getCname());
//		redisTemplate.boundValueOps("cList").set(course2.toString());
	}

	public List<StudentVo> tongji() {
		return studentMapper.tongji();
	}


}
