package com.isep.Service;

import com.isep.dao.ParentDao;
import com.isep.dao.TeacherDao;
import com.isep.entity.Course;
import com.isep.entity.Parent;
import com.isep.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ParentServiceImpl implements ParentService{
    @Autowired
    private ParentDao parentDao;


    @Override
    public Parent findByEmail(String email) {
        return parentDao.findByEmail(email);
    }

    @Override
    public int save(Parent parent) {
        return parentDao.save(parent);
    }

    @Override
    public boolean comparePassword(Parent parent, Parent parentInDataBase) {
        return parent.getPassword().equals(parentInDataBase.getPassword());
    }

    @Override
    public Parent findById(int id) {
        return parentDao.findById(id);
    }

    @Override
    public int updateInfo(Parent parent) {
        return parentDao.updateInfo(parent);
    }

    @Override
    public List<Course> courseSearch(Course course) {
        return parentDao.courseSearch(course);
    }

}
