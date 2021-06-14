package com.isep.Service;

import com.isep.entity.Course;
import com.isep.entity.Parent;
import com.isep.entity.User;

import java.util.List;

public interface ParentService {
    Parent findByEmail(String email);
    int save(Parent parent);
    boolean comparePassword(Parent parent, Parent parentInDataBase);
    Parent findById(int id);
    int updateInfo(Parent parent);
    List<Course> courseSearch(Course course);
}
