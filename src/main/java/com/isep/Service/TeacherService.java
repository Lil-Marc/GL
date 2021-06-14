package com.isep.Service;

import com.isep.entity.Course;
import com.isep.entity.Parent;
import com.isep.entity.Student;
import com.isep.entity.Teacher;

import java.util.List;

public interface TeacherService {
    Teacher findByEmail(String email);
    int save(Teacher teacher);
    boolean comparePassword(Teacher teacher, Teacher teacherInDataBase);
    Teacher findById(int id);
    int updateInfo(Teacher teacher);
    int courseEdit(Course course);
    List<Course> courseCheck(String teacher);
    List<Course> courseSearch(Course course);
}
