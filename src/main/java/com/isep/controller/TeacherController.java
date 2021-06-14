package com.isep.controller;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.isep.Service.AuthenticationService;
import com.isep.Service.StudentService;
import com.isep.Service.TeacherService;
import com.isep.entity.Course;
import com.isep.entity.Parent;
import com.isep.entity.Student;
import com.isep.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RestController
@CrossOrigin //前端接口为8080，后端接口为8989，这个参数使用可进行跨域访问
@RequestMapping("/GL/teacher")  //类路由
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value = "/save",produces = "application/json")
    public Object save(@RequestBody Teacher teacher){
        JSONObject jsonObject = new JSONObject();

        if (teacherService.findByEmail(teacher.getEmail()) != null){
            jsonObject.put("code","0");
            jsonObject.put("message","Email has used");
        }else {
            int save = teacherService.save(teacher);
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
    public Object findOne(@RequestBody Teacher teacher){
        Teacher TeacherInDataBase = teacherService.findByEmail(teacher.getEmail());
        JSONObject jsonObject = new JSONObject();
        if (TeacherInDataBase == null){
            jsonObject.put("code", "0");
            jsonObject.put("msg", "Email mistake");
        }else if (!teacherService.comparePassword(teacher,TeacherInDataBase)){
            jsonObject.put("code", "2");
            jsonObject.put("msg", "Password mistake");
        }else {
            String token = authenticationService.getToken(TeacherInDataBase);
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
        Teacher teacher = teacherService.findById(id);
        if (authenticationService.decodeToken(teacher,token)){
            jsonObject.put("name", teacher.getName());
            jsonObject.put("email", teacher.getEmail());
            jsonObject.put("password", teacher.getPassword());
            jsonObject.put("phoneNumber", teacher.getPhoneNumber());
            jsonObject.put("residentialAddress", teacher.getResidentialAddress());
            jsonObject.put("identify", teacher.getIdentify());
            jsonObject.put("specialty", teacher.getSpecialty());
            jsonObject.put("certificate", teacher.getCertificate());
        }else {
            jsonObject.put("code", "0");
            jsonObject.put("msg", "failed");
        }
        return jsonObject;
    }
    @PostMapping("/profileEdit")
    public Object profileEdit(@RequestBody Teacher newTeacher,@RequestParam String token){
        JSONObject jsonObject = new JSONObject();
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("Verify token failed");
        }
        int id = Integer.parseInt(userId);
        Teacher user = teacherService.findById(id);
        if (authenticationService.decodeToken(user,token)){
            newTeacher.setId(id);
            int i = teacherService.updateInfo(newTeacher);
            if (i == 1){
                String newToken = authenticationService.getToken(newTeacher);
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

    @Value("${file.upload.url}")
    private String uploadFilePath;

    @PostMapping("/courseEdit")
    public Object courseEdit(@RequestParam("files") MultipartFile files[],Course course,@RequestParam String token){
        JSONObject jsonObject = new JSONObject();

        //token
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("Verify token failed");
        }
        int id = Integer.parseInt(userId);
        Teacher user = teacherService.findById(id);
        if (authenticationService.decodeToken(user,token)){
            jsonObject.put("token", "token is good");
        }else{
            jsonObject.put("code", "0");
            jsonObject.put("msg", "token mistake");
        }

        String dests = "";
        for(int i=0;i<files.length;i++){
            String fileName = files[i].getOriginalFilename();  // 文件名
            File dest = new File(uploadFilePath +'/'+ fileName);
//            System.out.println(dest);
            dests = dests + fileName + "";
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                files[i].transferTo(dest);
            } catch (Exception e) {
                jsonObject.put("code",2);
                jsonObject.put("result","Upload failed");
            }
        }
        course.setFileName(dests);
//        System.out.println(course);
        int i = teacherService.courseEdit(course);
        if (i == 1){
            jsonObject.put("code",1);
            jsonObject.put("msg","edit successfully");
        }else {
            jsonObject.put("code",0);
            jsonObject.put("msg","edit failed");
        }

        return jsonObject;
    }
    @Value("${file.upload.url}")
    private String downloadFilePath;

    @RequestMapping("/download")
    public String fileDownLoad(HttpServletResponse response, @RequestParam("fileName") String fileName){
        File file = new File(downloadFilePath +'/'+ fileName);
        if(!file.exists()){
            return "下载文件不存在";
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName );

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buff = new byte[1024];
            OutputStream os  = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            return "下载失败";
        }
        return "下载成功";
    }



    @PostMapping("/courseCheck")
    public Object courseCheck(@RequestParam String token){
        JSONObject jsonObject = new JSONObject();
        //token
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("Verify token failed");
        }
        int id = Integer.parseInt(userId);
        Teacher user = teacherService.findById(id);
        if (authenticationService.decodeToken(user,token)){
            String name = user.getName();
            List<Course> courses = teacherService.courseCheck(name);
            jsonObject.put("code", "1");
            jsonObject.put("courses", courses);
        }else{
            jsonObject.put("code", "0");
            jsonObject.put("msg", "token mistake");
        }
        return jsonObject;
    }
    @PostMapping("/courseSearch")
    public Object courseSearch(@RequestBody Course course){
        JSONObject jsonObject = new JSONObject();
        String teacher = course.getTeacher();
        String specialty = course.getSpecialty();
        if (teacher.equals("")){
            course.setTeacher(null);
        }if (specialty.equals("")){
            course.setSpecialty(null);
        }
        List<Course> courses = teacherService.courseSearch(course);
        System.out.println(courses);
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



