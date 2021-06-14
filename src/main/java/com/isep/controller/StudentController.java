package com.isep.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.isep.Service.AuthenticationService;
import com.isep.Service.StudentService;
import com.isep.Service.UserService;
import com.isep.entity.*;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin //前端接口为8080，后端接口为8989，这个参数使用可进行跨域访问
@RequestMapping("/GL/student")  //类路由
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value = "/save",produces = "application/json")
    public Object save(@RequestBody Student student){
        JSONObject jsonObject = new JSONObject();

        if (studentService.findStudentByEmail(student.getEmail()) != null){
            jsonObject.put("code","0");
            jsonObject.put("message","Email has used");
        }else {
            int save = studentService.saveStudent(student);
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
    public Object findOne(@RequestBody Student student){
        Student StudentInDataBase = studentService.findStudentByEmail(student.getEmail());
        JSONObject jsonObject = new JSONObject();
        if (StudentInDataBase == null){
            jsonObject.put("code", "0");
            jsonObject.put("msg", "Email mistake");
        }else if (!studentService.comparePassword(student,StudentInDataBase)){
            jsonObject.put("code", "2");
            jsonObject.put("msg", "Password mistake");
        }else {
            String token = authenticationService.getToken(StudentInDataBase);
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
        Student student = studentService.findById(id);
        if (authenticationService.decodeToken(student,token)){
            jsonObject.put("name", student.getName());
            jsonObject.put("email", student.getEmail());
            jsonObject.put("password", student.getPassword());
            jsonObject.put("phoneNumber", student.getPhoneNumber());
            jsonObject.put("residentialAddress", student.getResidentialAddress());
            jsonObject.put("identify", student.getIdentify());
            jsonObject.put("niveau", student.getLevel());
            jsonObject.put("school", student.getSchool());
        }else {
            jsonObject.put("code", "0");
            jsonObject.put("msg", "failed");
        }
        return jsonObject;
    }
    @PostMapping("/profileEdit")
    public Object profileEdit(@RequestBody Student newStudent,@RequestParam String token){
        JSONObject jsonObject = new JSONObject();
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("Verify token failed");
        }
        int id = Integer.parseInt(userId);
        Student user = studentService.findById(id);
        if (authenticationService.decodeToken(user,token)){
            newStudent.setId(id);
            int i = studentService.updateInfo(newStudent);
            if (i == 1){
                String newToken = authenticationService.getToken(newStudent);
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
        List<Course> courses = studentService.courseSearch(course);
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