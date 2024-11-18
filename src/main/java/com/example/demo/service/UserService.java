package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserRepository;
import com.example.demo.model.UserTableBean;

@Service
public class UserService {
	
	
	@Autowired
	private UserRepository userRepo;
	
	public UserTableBean checkLogin(String userEmail, String loginPwd) {
		System.out.println(userEmail+"  "+loginPwd);
		UserTableBean dbUsers = userRepo.findByEmail(userEmail);

		
		if (dbUsers != null) {
			 if(dbUsers.getPassword().equals(loginPwd)) {
				 return dbUsers;
			 }
		}
		return null;
	}
	
	
	
}
