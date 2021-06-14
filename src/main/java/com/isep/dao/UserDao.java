package com.isep.dao;

import com.isep.entity.House;
import com.isep.entity.Student;
import com.isep.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    List<User> findAll();
    int save(User user);
    int updateUserInfo(User user);
    User findOne(String email,String password);
    User findByEmail(String email);
    User findById(int id);
    int houseEdit(House houseinformation);
    int houseChange(House houseinformation);
    House findHouse(String possessor);
    House findHouseById(int id);
    List<House> searchHouse(House houseinformation);
    int bookHouse(House houseinformation);
}
