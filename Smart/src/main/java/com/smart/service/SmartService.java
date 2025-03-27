package com.smart.service;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.entity.users;
import com.smart.repository.SmartRepository;

@Service
public class SmartService {

	@Autowired
	private SmartRepository smart;

	
	public void join(users st) {
		smart.save(st);
	}
	
	
	 public boolean idCheck(String email) {
	        
		users user = smart.existsByEmail(email);
		if (user != null) {
		return false;
	 }else {
		 return true;
	 }
		}
	 
	 
	public users login(users st) {
		String user_email = st.getUser_email();
		String user_pw = st.getUser_pw();
		users user = smart.login(user_email,user_pw);
			return user;
	}
	
	
	@Transactional
	public void updateUser(users user) {
	    smart.save(user);
	}
		
	 
			}

