package com.minibank.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibank.dto.ApiResponse;
import com.minibank.util.DataSourceUtil;
import com.minibank.util.JwtUtil;

public class JwtFilter implements javax.servlet.Filter {

	private List<String> excludeUrls = Arrays.asList("/login", "/signup");
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public void init(FilterConfig filterConfig) throws ServletException{
		
	}
	
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	        throws IOException, ServletException {

	    HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) res;

	    String uri = request.getRequestURI();   

	
	    System.out.println("JWT FILTER URI = " + uri);

	    if (uri.equals("/login") || uri.equals("/signup")) {
	        chain.doFilter(request, response);
	        return;
	    }

	    String authHeader = request.getHeader("Authorization");

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        sendError(response, "Missing or invalid Authorization header");
	        return;
	    }

	    String token = authHeader.substring(7);

	    if (!JwtUtil.validateToken(token)) {
	        sendError(response, "Invalid or expired token");
	        return;
	    }

	    chain.doFilter(request, response);
	}

	private void sendError(HttpServletResponse res, String message) throws IOException{
		res.setContentType("application/json");
		res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		objectMapper.writeValue(res.getWriter(), new ApiResponse(false, message, null));
	}
	
	public void destroy(){
		 
	}
}
