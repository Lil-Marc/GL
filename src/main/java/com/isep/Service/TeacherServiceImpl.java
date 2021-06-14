package com.isep.Service;

import com.isep.dao.StudentDao;
import com.isep.dao.TeacherDao;
import com.isep.entity.Course;
import com.isep.entity.Student;
import com.isep.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService{
    @Autowired
    private TeacherDao teacherDao;

    @Override
    public Teacher findByEmail(String email) {
        return teacherDao.findByEmail(email);
    }

    @Override
    public int save(Teacher teacher) {
        return teacherDao.save(teacher);
    }

    @Override
    public boolean comparePassword(Teacher teacher, Teacher teacherInDataBase) {
        return teacher.getPassword().equals(teacherInDataBase.getPassword());
    }

    @Override
    public Teacher findById(int id) {
        return teacherDao.findById(id);
    }

    @Override
    public int updateInfo(Teacher teacher) {
        return teacherDao.updateInfo(teacher);
    }

    @Override
    public int courseEdit(Course course) {
        return teacherDao.courseEdit(course);
    }

    @Override
    public List<Course> courseCheck(String teacher) {
        return teacherDao.courseCheck(teacher);
    }

    @Override
    public List<Course> courseSearch(Course course) {
        return teacherDao.courseSearch(course);
    }


}
