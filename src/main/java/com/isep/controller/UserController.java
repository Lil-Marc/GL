package com.isep.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.isep.Service.AuthenticationService;
import com.isep.Service.UserService;
import com.isep.entity.House;
import com.isep.entity.Student;
import com.isep.entity.User;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin //前端接口为8080，后端接口为8989，这个参数使用可进行跨域访问
@RequestMapping("/class")  //类路由
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/findAll") //设置请求方式和路由地址
    public List<User> findAll(){
        return userService.findAll();
    }



    @PostMapping(value = "/save",produces = "application/json")
    public Object save(@RequestBody User user) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if (userService.findByEmail(user.getEmail()) != null){
            jsonObject.put("code","0");
            jsonObject.put("message","Email has used");
        }else {
            int save = userService.save(user);
            if (save == 1){
                jsonObject.put("code","1");
                jsonObject.put("message","sign up successfully");
            }else {
                jsonObject.put("code","2");
                jsonObject.put("message","failed");
            }
        }
        return jsonObject;
    }

    @PostMapping("/login")
    public Object findOne(@RequestBody User u){
        User userInDataBase = userService.findByEmail(u.getEmail());
        JSONObject jsonObject = new JSONObject();
        if (userInDataBase == null){
            jsonObject.put("code", "0");
            jsonObject.put("msg", "Email mistake");
        }else if (!userService.comparePassword(u,userInDataBase)){
            jsonObject.put("code", "2");
            jsonObject.put("msg", "Password mistake");
        }else {
            String token = authenticationService.getToken(userInDataBase);
            jsonObject.put("code", "1");
            jsonObject.put("token", token);
            jsonObject.put("msg", "sign in successfully");
        }
        return jsonObject;
    }

    @PostMapping("/profileCheck")
    public Object profileCheck(@RequestParam String token){
        JSONObject jsonObject = new JSONObject();
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("Verify token failed");
        }
        int id = Integer.parseInt(userId);
        User user = userService.findById(id);
        if (authenticationService.decodeToken(user,token)){
            jsonObject.put("name", user.getName());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("phoneNumber", user.getPhoneNumber());
            jsonObject.put("residentialAddress", user.getResidentialAddress());
        }else {
            jsonObject.put("code", "0");
            jsonObject.put("msg", "failed");
        }
        return jsonObject;
    }

    @PostMapping("/profileEdit")
    public Object profileEdit(@RequestBody User newUser,@RequestParam String token){
        JSONObject jsonObject = new JSONObject();
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("Verify token failed");
        }
        int id = Integer.parseInt(userId);
        User user = userService.findById(id);
        if (authenticationService.decodeToken(user,token)){
            newUser.setId(id);
            int i = userService.updateUserInfo(newUser);
            if (i == 1){
                String newToken = authenticationService.getToken(newUser);
                jsonObject.put("code", "1");
                jsonObject.put("token", newToken);
                jsonObject.put("msg", "Edit successfully");
            }else{
                jsonObject.put("code", "0");
                jsonObject.put("msg", "Edit failed");
            }
        }else{
            jsonObject.put("code", "0");
            jsonObject.put("msg", "token mistake");
        }
        return jsonObject;
    }
    @PostMapping("/houseEdit")
    public Object houseEdit(@RequestBody House houseInformation,@RequestParam String token){
        JSONObject jsonObject = new JSONObject();
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("Verify token failed");
        }
        int id = Integer.parseInt(userId);
        User user = userService.findById(id);
        if (authenticationService.decodeToken(user,token)) {
            String name = houseInformation.getPossessor();
            House house = userService.findHouse(name);
            if (house == null){
                int i = userService.houseEdit(houseInformation);
                if (i == 1){
                    jsonObject.put("code", "1");
                    jsonObject.put("msg", "Initial successfully");
                }else{
                    jsonObject.put("code", "0");
                    jsonObject.put("msg", "Initial failed");
                }
            }else {
                houseInformation.setId(house.getId());
                int i = userService.houseChange(houseInformation);
                if (i == 1){
                    jsonObject.put("code", "2");
                    jsonObject.put("msg", "Edit successfully");
                }else{
                    jsonObject.put("code", "0");
                    jsonObject.put("msg", "Edit failed");
                }
            }
        }else {
            jsonObject.put("code", "0");
            jsonObject.put("msg", "failed");
        }
        return jsonObject;
    }

    @PostMapping("/houseCheck")
    public Object houseCheck(@RequestParam String token){
        JSONObject jsonObject = new JSONObject();
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("Verify token failed");
        }
        int id = Integer.parseInt(userId);
        User user = userService.findById(id);
        if (authenticationService.decodeToken(user,token)){
            String name = user.getName();
            House house = userService.findHouse(name);
            if (house != null){
                jsonObject.put("code", "1");
                jsonObject.put("id", house.getId());
                jsonObject.put("description", house.getDescription());
                jsonObject.put("restriction", house.getRestriction());
                jsonObject.put("service", house.getService());
                jsonObject.put("address", house.getAddress());
                jsonObject.put("img",house.getImg());
                jsonObject.put("houseSize", house.getHouseSize());
                jsonObject.put("ifBooked", house.getIfBooked());
                jsonObject.put("possessor", house.getPossessor());
            }else {
                jsonObject.put("code", "2");
                jsonObject.put("msg", "please set your house information");
            }
        }else {
            jsonObject.put("code", "0");
            jsonObject.put("msg", "failed");
        }
        return jsonObject;
    }

    @PostMapping("/search")
    public Object search(@RequestBody House house){
        //前端非空判断
        JSONArray service = house.getService();
        JSONArray restriction = house.getRestriction();
        String houseSize = house.getHouseSize();
        String address = house.getAddress();

        if (service.isEmpty()){
            house.setService(null);
        }
        if (restriction.isEmpty()){
            house.setRestriction(null);
        }
        if (houseSize.equals("")){
            house.setHouseSize(null);
        }
        if (address.equals("")){
            house.setAddress(null);
        }

        JSONObject jsonObject = new JSONObject();

        List<House> houses = userService.searchHouse(house);
        if (houses.size()>0){
            jsonObject.put("code", "1");
//            for (int i = 0; i < houses.size(); i++) {
//            }
            jsonObject.put("houses",houses);
        }else {
            jsonObject.put("code", "0");
            jsonObject.put("msg", "failed");
        }

        return jsonObject;
    }
    @PostMapping("/bookHouse")
    public Object bookHouse(@RequestParam String token,@RequestBody House house){
        JSONObject jsonObject = new JSONObject();
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("Verify token failed");
        }
        int id = Integer.parseInt(userId);
        User tenant = userService.findById(id);
        if (authenticationService.decodeToken(tenant,token)){
            int houseId = house.getId();
            House bookhouse = userService.findHouseById(houseId);
            if (bookhouse!=null){
                if (bookhouse.getIfBooked()){
                    jsonObject.put("code", "2");
                    jsonObject.put("msg", "House has been booked");
                }else{
                    bookhouse.setTenantId(id);
                    bookhouse.setIfBooked(true);
                    int i = userService.bookHouse(bookhouse);
                    if (i == 1){
                        jsonObject.put("code", "1");
                        jsonObject.put("msg", "Book successfully");
                    }else {
                        jsonObject.put("code", "0");
                        jsonObject.put("msg", "Failed");
                    }
                }
            }else {
                jsonObject.put("code", "3");
                jsonObject.put("msg", "House not exist");
            }
        }else {
            jsonObject.put("code", "0");
            jsonObject.put("msg", "Failed");
        }

        return jsonObject;
    }

}