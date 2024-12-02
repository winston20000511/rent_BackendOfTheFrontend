package com.example.demo.Controller;

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
        return "layout/login"; // src/main/resources/templates/layout/login.html
    }
    
    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {
        return "layout/forgot-password"; // 對應 src/main/resources/templates/layout/forgot-password.html
    }
}
