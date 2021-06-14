package com.isep.dao;

import com.isep.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentDao {
    Student findStudentByEmail(String email);
    int saveStudent(Student student);
    boolean comparePassword(Student student, Student studentInDataBase);
    Student findById(int id);
    int updateInfo(Student student);
    List<Course> courseSearch(Course course);
}
