package com.minibank.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibank.dao.UserDao;
import com.minibank.dto.ApiResponse;
import com.minibank.dto.SignupRequest;
import com.minibank.model.User;
import com.minibank.util.DataSourceUtil;

public class SignupServlet extends HttpServlet {
	
	
	private UserDao userDao = new UserDao(DataSourceUtil.getDataSource());
	private ObjectMapper mapper = new ObjectMapper();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		
		SignupRequest signupRequest = mapper.readValue(req.getInputStream(), SignupRequest.class);
		
		String username = signupRequest.getUsername();
		String password = signupRequest.getPassword();
		String fullName = signupRequest.getFullName();
		String email = signupRequest.getEmail();
		
		if(username == null||password == null||fullName == null||email==null) {
			ApiResponse response = new ApiResponse(false, "All fields are required", null);
			mapper.writeValue(res.getWriter(), response);
			return;
		}
		
		if(userDao.getUserByUsername(username)!=null) {
			ApiResponse response = new ApiResponse(false, "Username already exists", null);
			mapper.writeValue(res.getWriter(), response);
			return;
		}
		
		
		if(password.length()<6){
			ApiResponse response = new ApiResponse(false, "Password should be atlease 6 characters", null);
			mapper.writeValue(res.getWriter(), response);
			return;
		}
		
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		
		User user = new User(username, hashedPassword, fullName, email, BigDecimal.ZERO);
		
		boolean created = userDao.addUser(user);
		
		ApiResponse response;
		
		if(created) {
			response = new ApiResponse(true, "User created successfully", null);
		}else {
			response = new ApiResponse(false, "Failed to create user", null);
		}
		
		mapper.writeValue(res.getWriter(), response);
	}

}
