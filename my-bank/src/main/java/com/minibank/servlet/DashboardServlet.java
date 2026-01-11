package com.minibank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibank.dao.UserDao;
import com.minibank.dto.ApiResponse;
import com.minibank.dto.ResponseDto;
import com.minibank.model.User;
import com.minibank.util.JwtUtil;

public class DashboardServlet extends HttpServlet {
	
	private UserDao userDao = new UserDao();
	
	private ObjectMapper mapper = new ObjectMapper();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException{
		
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		
		String authHeader = req.getHeader("Authorization");
		
		if(authHeader==null||!authHeader.startsWith("Bearer ")) {
			mapper.writeValue(res.getWriter(), new ApiResponse(false, "Missing Authorization Header", null));
			return;
		}
		
		String token = authHeader.substring(7);
		String username = JwtUtil.getUsernameFromToken(token);
		
		if(username == null) {
			mapper.writeValue(res.getWriter(), new ApiResponse(false, "Invalid Token", null));
			return;
		}
		
		User user = userDao.getUserByUsername(username);
		
		if(user == null) {
			mapper.writeValue(res.getWriter(), new ApiResponse(false, "User not found", null));
			return;
		}
		
		ResponseDto dashboard = new ResponseDto(user.getUsername(), user.getFullName(), user.getEmail(), user.getBalance());
		
		mapper.writeValue(res.getWriter(), new ApiResponse(true, "Dashboard Fetched Succesfully", dashboard));
	}

}
