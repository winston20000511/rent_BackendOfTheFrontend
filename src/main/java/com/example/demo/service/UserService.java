package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.model.UserRepository;

@Service
public class UserService {
	
	
	@Autowired
	private UserRepository userRepo;
	
	public User checkLogin(String userEmail, String loginPwd) {

		User dbUsers = userRepo.findByEmail(userEmail);
		
		if (dbUsers != null) {
			 if(dbUsers.getPassword().equals(loginPwd)) {
				 return dbUsers;
			 }
		}
		return null;
	}
	
	
	
}
