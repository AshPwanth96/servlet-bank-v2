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

public class WithdrawServlet extends HttpServlet {

    private final UserDao userDao = new UserDao(DataSourceUtil.getDataSource());
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            mapper.writeValue(res.getWriter(),
                    new ApiResponse(false, "Missing or invalid Authorization header", null));
            return;
        }

        String token = authHeader.substring(7);
        String username = JwtUtil.getUsernameFromToken(token);

        if (username == null) {
            mapper.writeValue(res.getWriter(),
                    new ApiResponse(false, "Invalid token", null));
            return;
        }

        String amountStr = req.getParameter("amount");
        if (amountStr == null) {
            mapper.writeValue(res.getWriter(),
                    new ApiResponse(false, "Amount is required", null));
            return;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                mapper.writeValue(res.getWriter(),
                        new ApiResponse(false, "Amount must be positive", null));
                return;
            }
        } catch (NumberFormatException e) {
            mapper.writeValue(res.getWriter(),
                    new ApiResponse(false, "Invalid amount format", null));
            return;
        }

        User user = userDao.getUserByUsername(username);
        if (user == null) {
            mapper.writeValue(res.getWriter(),
                    new ApiResponse(false, "User not found", null));
            return;
        }

        if (user.getBalance().compareTo(amount) < 0) {
            mapper.writeValue(res.getWriter(),
                    new ApiResponse(false, "Insufficient balance", null));
            return;
        }

        BigDecimal newBalance = user.getBalance().subtract(amount);
        user.setBalance(newBalance);

        boolean updated = userDao.updateUserBalance(user);

        ApiResponse response = updated
                ? new ApiResponse(true, "Withdrawal successful", newBalance)
                : new ApiResponse(false, "Withdrawal failed", null);

        mapper.writeValue(res.getWriter(), response);
    }
}
