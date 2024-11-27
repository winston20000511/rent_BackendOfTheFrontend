package com.rent189.springboot3demo.Controller;

import com.rent189.springboot3demo.model.User;
import com.rent189.springboot3demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 註冊使用者的功能
    @PostMapping("/register")
    public ResponseEntity<String> registerUser( @RequestBody User user) {
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
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)  // 若找到，返回 200 OK
                .orElseGet(() -> ResponseEntity.status(404).build());  // 若未找到，返回 404 Not Found
    }
}