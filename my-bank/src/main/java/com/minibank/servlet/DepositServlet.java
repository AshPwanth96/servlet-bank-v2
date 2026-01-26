package com.minibank.servlet;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibank.dao.UserDao;
import com.minibank.dto.ApiResponse;
import com.minibank.model.User;
import com.minibank.util.DataSourceUtil;
import com.minibank.util.JwtUtil;

public class DepositServlet extends HttpServlet {
	
	private UserDao userDao = new UserDao(DataSourceUtil.getDataSource());
	
	private ObjectMapper mapper = new ObjectMapper();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException{
		
		res.setContentType("application/json");
		res.setCharacterEncoding("UTF-8");
		
		String authHeader = req.getHeader("Authorization");
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
		    mapper.writeValue(res.getWriter(), new ApiResponse(false, "Missing or invalid Authorization Header", null));
		    return;
		}
		String token = authHeader.substring(7);
		
		String username = JwtUtil.getUsernameFromToken(token);
		
		
		if(username ==  null) {
			mapper.writeValue(res.getWriter(), new ApiResponse(false, "Invalid Token", null));
			return;
		}
		
		String amountStr = req.getParameter("amount");
		
		if(amountStr == null) {
			mapper.writeValue(res.getWriter(), new ApiResponse(false, "Amount is required", null));
			return;
		}
		
		BigDecimal amount;
		
		try {
			amount = new BigDecimal(amountStr);
			if(amount.compareTo(BigDecimal.ZERO)<=0) {
				mapper.writeValue(res.getWriter(), new ApiResponse(false, "Amount must be positive", null));
				return;
			}
			}catch(NumberFormatException e) {
				mapper.writeValue(res.getWriter(), new ApiResponse(false, "Invalid amount format", null));
				return;
			}
			
			User user = userDao.getUserByUsername(username);
			
			if(user ==  null) {
				mapper.writeValue(res.getWriter(), new ApiResponse(false, "User not found", null));
				return;
			}
			
			BigDecimal newBalance = user.getBalance().add(amount);
			
			user.setBalance(newBalance);
			
			boolean updated = userDao.updateUserBalance(user);
			
			ApiResponse response = updated?new ApiResponse(true, "Deposit Successful", newBalance): new ApiResponse(false, "Deposit Failed", null);
			
			mapper.writeValue(res.getWriter(), response);
		
	}

}
