package com.minibank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibank.dao.UserDao;
import com.minibank.dto.ApiResponse;
import com.minibank.dto.LoginRequest;
import com.minibank.model.User;
import com.minibank.util.JwtUtil;

public class LoginServlet extends HttpServlet {
	
	private UserDao userDao = new UserDao();
	private ObjectMapper mapper = new ObjectMapper();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException{
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		
		LoginRequest loginRequest = mapper.readValue(req.getInputStream(), LoginRequest.class);
		
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		
		if(username == null||password==null) {
			ApiResponse response = new ApiResponse(false, "Invalid Username or password", null);
			mapper.writeValue(res.getWriter(), response);
			return;
		}
		
		User user = userDao.getUserByUsername(username);
		if(user == null||!BCrypt.checkpw(password, user.getPassword())) {
			ApiResponse response = new ApiResponse(false, "Invalid username or password", null);
			mapper.writeValue(res.getWriter(), response);
			return;
			
		}
		
		String token = JwtUtil.generateTokens(username);
		
		ApiResponse response = new ApiResponse(true, "Login Successful", token);
		mapper.writeValue(res.getWriter(), response);
	}

}
