package com.nowcoder.community.Controller;

import com.nowcoder.community.Service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

//这两个注解都是SpringMVC的
@Controller
@RequestMapping("/alpha") //设置访问路径
public class AlphaController {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/helloWorld")
    @ResponseBody //默认返回的是网页，这里仅返回字符串，所以需要添加这个注解
    public String sayHello(){
        return "Hello World (Spring Boot)";
    }

    @RequestMapping("/test")
    @ResponseBody //默认返回的是网页，这里仅返回字符串，所以需要添加这个注解
    public String test(){
        return alphaService.find();
    }

    //这是比较底层的调用方式
    @RequestMapping("/http")
    public void http(HttpServletRequest request , HttpServletResponse response){
        //获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ":" + value);
        }
        //获取参数
        System.out.println(request.getParameter("code"));
        //返回响应数据，是返回网页，字符串还是图片等
        response.setContentType("text/html;charset=utf-8");
        try(
                PrintWriter writer = response.getWriter()
            ) {
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //这是封装之后的调用方式
    //get请求，/students?current=1&limit=20
    @RequestMapping(path = "/students" , method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current" , required = false , defaultValue = "1") int current ,
            @RequestParam(name = "limit" , required = false , defaultValue = "10") int limit){
        System.out.println(current + "------" + limit);
        return "some Students";
    }

    //get请求，/students/123
    @RequestMapping(path = "/student/{id}" , method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "a Student";
    }

    //post请求
    @RequestMapping(path = "/student" , method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name , int age){
        System.out.println(name + "------" + age);
        return "success";
    }

    //响应HTML数据
    //方式1
    @RequestMapping(path = "/teacher" , method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","张三");
        modelAndView.addObject("age","30");
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }

    //方式2
    @RequestMapping(path = "/school" , method = RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","发财大学");
        model.addAttribute("age","100");
        return "/demo/view";
    }

    //响应JSON数据（异步请求）
    //Java对象 -> JSON字符串 -> JS对象
    @RequestMapping(path = "/emp" , method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",30);
        emp.put("salary",8888);
        return emp;
    }

    @RequestMapping(path = "/emps" , method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",30);
        emp.put("salary",8888);
        list.add(emp);
        emp.put("name","李四");
        emp.put("age",45);
        emp.put("salary",88520);
        list.add(emp);
        emp.put("name","王五");
        emp.put("age",28);
        emp.put("salary",8520);
        list.add(emp);
        return list;
    }
}
