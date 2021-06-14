package com.isep.Service;

import com.isep.dao.StudentDao;
import com.isep.dao.UserDao;
import com.isep.entity.Course;
import com.isep.entity.House;
import com.isep.entity.Student;
import com.isep.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentDao studentDao;
    @Override
    public Student findStudentByEmail(String email) {
        return studentDao.findStudentByEmail(email);
    }

    @Override
    public int saveStudent(Student student) {
        return studentDao.saveStudent(student);
    }

    @Override
    public boolean comparePassword(Student student, Student studentInDataBase) {
        return student.getPassword().equals(studentInDataBase.getPassword());
    }

    @Override
    public Student findById(int id) {
        return studentDao.findById(id);
    }

    @Override
    public int updateInfo(Student student) {
        return studentDao.updateInfo(student);
    }

    @Override
    public List<Course> courseSearch(Course course) {
        return studentDao.courseSearch(course);
    }

}
