package com.isep.dao;

import com.isep.entity.Course;
import com.isep.entity.Parent;
import com.isep.entity.Student;
import com.isep.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ParentDao {
    Parent findByEmail(String email);
    int save(Parent parent);
    boolean comparePassword(Parent parent, Parent parentInDataBase);
    Parent findById(int id);
    int updateInfo(Parent parent);
    List<Course> courseSearch(Course course);
}
