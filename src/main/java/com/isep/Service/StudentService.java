package com.isep.Service;

import com.isep.entity.*;

import java.util.List;

public interface StudentService {
    Student findStudentByEmail(String email);
    int saveStudent(Student student);
    boolean comparePassword(Student student, Student studentInDataBase);
    Student findById(int id);
    int updateInfo(Student student);
    List<Course> courseSearch(Course course);
}
