package com.isep.Service;

import com.isep.dao.UserDao;
import com.isep.entity.House;
import com.isep.entity.Student;
import com.isep.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userdao;

    @Override
    public List<User> findAll() {
        return userdao.findAll();
    }

    @Override
    public int save(User user) {
        return userdao.save(user);
    }

    @Override
    public int updateUserInfo(User user) {
        return userdao.updateUserInfo(user);
    }

    @Override
    public User findOne(String email,String password) {
        return userdao.findOne(email,password);
    }

    @Override
    public User findByEmail(String email) {
        return userdao.findByEmail(email);
    }

    @Override
    public User findById(int id) {
        return userdao.findById(id);
    }

    @Override
    public boolean comparePassword(User user, User userInDataBase) {
        return user.getPassword()
                .equals(userInDataBase.getPassword());
    }
    @Override
    public int houseEdit(House houseinformation) {
        return userdao.houseEdit(houseinformation);
    }

    @Override
    public int houseChange(House houseinformation) {
        return userdao.houseChange(houseinformation);
    }

    @Override
    public House findHouse(String possessor) {
        return userdao.findHouse(possessor);
    }

    @Override
    public House findHouseById(int id) {
        return userdao.findHouseById(id);
    }

    @Override
    public List<House> searchHouse(House houseinformation) {
        return userdao.searchHouse(houseinformation);
    }

    @Override
    public int bookHouse(House houseinformation) {
        return userdao.bookHouse(houseinformation);
    }

}
