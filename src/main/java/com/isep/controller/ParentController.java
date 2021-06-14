package com.isep.controller;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.isep.Service.AuthenticationService;
import com.isep.Service.ParentService;
import com.isep.Service.TeacherService;
import com.isep.entity.Course;
import com.isep.entity.Parent;
import com.isep.entity.Teacher;
import com.isep.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin //前端接口为8080，后端接口为8989，这个参数使用可进行跨域访问
@RequestMapping("/GL/parent")  //类路由
public class ParentController {
    @Autowired
    private ParentService parentService;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value = "/save",produces = "application/json")
    public Object save(@RequestBody Parent parent){
        JSONObject jsonObject = new JSONObject();

        if (parentService.findByEmail(parent.getEmail()) != null){
            jsonObject.put("code","0");
            jsonObject.put("message","Email has used");
        }else {
            int save = parentService.save(parent);
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
    public Object findOne(@RequestBody Parent parent){
        Parent ParentInDataBase = parentService.findByEmail(parent.getEmail());
        JSONObject jsonObject = new JSONObject();
        if (ParentInDataBase == null){
            jsonObject.put("code", "0");
            jsonObject.put("msg", "Email mistake");
        }else if (!parentService.comparePassword(parent,ParentInDataBase)){
            jsonObject.put("code", "2");
            jsonObject.put("msg", "Password mistake");
        }else {
            String token = authenticationService.getToken(ParentInDataBase);
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
        Parent parent = parentService.findById(id);
        if (authenticationService.decodeToken(parent,token)){
            jsonObject.put("name", parent.getName());
            jsonObject.put("email", parent.getEmail());
            jsonObject.put("password", parent.getPassword());
            jsonObject.put("phoneNumber", parent.getPhoneNumber());
            jsonObject.put("residentialAddress", parent.getResidentialAddress());
            jsonObject.put("identify", parent.getIdentify());
            jsonObject.put("childName", parent.getChildName());
            jsonObject.put("childEmail", parent.getChildEmail());
        }else {
            jsonObject.put("code", "0");
            jsonObject.put("msg", "failed");
        }
        return jsonObject;
    }
    @PostMapping("/profileEdit")
    public Object profileEdit(@RequestBody Parent newParent,@RequestParam String token){
        JSONObject jsonObject = new JSONObject();
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("Verify token failed");
        }
        int id = Integer.parseInt(userId);
        Parent user = parentService.findById(id);
        if (authenticationService.decodeToken(user,token)){
            newParent.setId(id);
            int i = parentService.updateInfo(newParent);
            if (i == 1){
                String newToken = authenticationService.getToken(newParent);
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
    @PostMapping("/courseSearch")
    public Object courseSearch(@RequestBody Course course){
        JSONObject jsonObject = new JSONObject();
        List<Course> courses = parentService.courseSearch(course);
        if (courses.size() == 0){
            jsonObject.put("code", "0");
            jsonObject.put("msg", "There are no course");
        }else {
            jsonObject.put("code", "1");
            jsonObject.put("courses", courses);
        }
        return jsonObject;
    }



}