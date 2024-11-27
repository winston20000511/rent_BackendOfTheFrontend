package com.rent189.springboot3demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        return "layout/navbar"; // 渲染 src/main/resources/templates/layout/navbar.html
    }
    
    @GetMapping("/login")
    public String login(Model model) {
        return "users/login"; // 渲染 src/main/resources/templates/users/login.html
    }
}
