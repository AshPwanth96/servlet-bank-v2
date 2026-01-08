package com.minibank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibank.dao.UserDao;

public class WithdrawServlet {
	
	private UserDao userDao = new UserDao();
	
	private ObjectMapper mapper = new ObjectMapper();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		res.setContentType("application.json");
		res.setCharacterEncoding("UTF-8");
		
		String authHeader = req.getHeader("Authorization");
		
		
	}

}
