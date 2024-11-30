package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
=======
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
>>>>>>> origin/OK

import com.example.demo.model.UserTableBean;
import com.example.demo.service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

<<<<<<< HEAD
    // 註冊使用者的功能
    @PostMapping("/register")
    public ResponseEntity<String> registerUser( @RequestBody UserTableBean user) {
        // 呼叫 Service 來儲存使用者資料
        boolean isRegistered = userService.registerUser(user);
        
        if (isRegistered) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(400).body("User registration failed");
        }
    }

    // 根據 email 查詢使用者
    @GetMapping("/email/{email}")
    public ResponseEntity<UserTableBean> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)  // 若找到，返回 200 OK
                .orElseGet(() -> ResponseEntity.status(404).build());  // 若未找到，返回 404 Not Found
    }
}
=======
		UserTableBean result = userService.checkLogin(username, password);
		
		
		if (result != null) {
			httpSession.setAttribute("loginUserId", result.getUserId());
			httpSession.setAttribute("loginUsername", result.getName());
			httpSession.setAttribute("loginUserEmail", result.getEmail());
			
			model.addAttribute("loginOkMsg", "登入成功");

			return "redirect:/";
			
		} else {
			model.addAttribute("errorMsg", "帳密錯誤");
			return "loginView";
		}
		
		
		
		
	}
	
	
}
>>>>>>> origin/OK
