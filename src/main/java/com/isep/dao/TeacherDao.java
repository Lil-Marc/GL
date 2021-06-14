package com.isep.dao;

import com.isep.entity.Course;
import com.isep.entity.Parent;
import com.isep.entity.Student;
import com.isep.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeacherDao {
    Teacher findByEmail(String email);
    int save(Teacher teacher);
    boolean comparePassword(Teacher teacher, Teacher teacherInDataBase);
    Teacher findById(int id);
    int updateInfo(Teacher teacher);
    int courseEdit(Course course);
    List<Course> courseCheck(String teacher);
    List<Course> courseSearch(Course course);
}
