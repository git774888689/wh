package com.xiaoshu.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

public class Student implements Serializable {
    @Id
    private Integer id;

    private Integer courseid;

    private String sname;

    private Integer age;

    private Integer code;

    private String grade;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date entrytime;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createtime;

    
    
    
public Student() {
		super();
	}

    public Student(Integer id, Integer courseid, String sname, Integer age, Integer code, String grade, Date entrytime,
			Date createtime) {
		super();
		this.id = id;
		this.courseid = courseid;
		this.sname = sname;
		this.age = age;
		this.code = code;
		this.grade = grade;
		this.entrytime = entrytime;
		this.createtime = createtime;
	}


	private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return courseid
     */
    public Integer getCourseid() {
        return courseid;
    }

    /**
     * @param courseid
     */
    public void setCourseid(Integer courseid) {
        this.courseid = courseid;
    }

    /**
     * @return sname
     */
    public String getSname() {
        return sname;
    }

    /**
     * @param sname
     */
    public void setSname(String sname) {
        this.sname = sname == null ? null : sname.trim();
    }

    /**
     * @return age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return grade
     */
    public String getGrade() {
        return grade;
    }

    /**
     * @param grade
     */
    public void setGrade(String grade) {
        this.grade = grade == null ? null : grade.trim();
    }

    /**
     * @return entrytime
     */
    public Date getEntrytime() {
        return entrytime;
    }

    /**
     * @param entrytime
     */
    public void setEntrytime(Date entrytime) {
        this.entrytime = entrytime;
    }

    /**
     * @return createtime
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", courseid=").append(courseid);
        sb.append(", sname=").append(sname);
        sb.append(", age=").append(age);
        sb.append(", code=").append(code);
        sb.append(", grade=").append(grade);
        sb.append(", entrytime=").append(entrytime);
        sb.append(", createtime=").append(createtime);
        sb.append("]");
        return sb.toString();
    }
}