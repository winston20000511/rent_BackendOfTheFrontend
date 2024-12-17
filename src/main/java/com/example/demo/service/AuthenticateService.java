
package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.model.UserTableBean;
import com.example.demo.repository.AuthenticateRepository;

@Service
	public class AuthenticateService {

	    @Autowired
	    private AuthenticateRepository authRepo;

	    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    
	    public void registerUser(UserRegisterDTO user) {
	    	UserTableBean  userRegister = new UserTableBean ();
	    	userRegister.setName(user.getName());
	    	userRegister.setEmail(user.getEmail());
	    	userRegister.setPassword(passwordEncoder.encode(user.getPassword()));
	        
	    	authRepo.save(userRegister);
	    }

	    public UserTableBean validateUser(String email, String rawPassword) {
	        UserTableBean user = authRepo.findByEmail(email).orElse(null);
	        
	        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
	            return user; 
	        }
	        return null; 
	    }

	    public void sendResetPasswordEmail(String email) {
	        // 發送忘記密碼邏輯 ******略******
	    }
	}


