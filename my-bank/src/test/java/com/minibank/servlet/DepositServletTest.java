package com.minibank.servlet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.minibank.dao.UserDao;
import com.minibank.model.User;
import com.minibank.util.JwtUtil;

public class DepositServletTest {

    @Test
    void testDeposit_success() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer fake.jwt.token");

        when(request.getParameter("amount"))
                .thenReturn("500");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        UserDao mockUserDao = mock(UserDao.class);

        User mockUser = new User(
                1,
                "john",
                "hashed",
                "John Doe",
                "john@test.com",
                new BigDecimal("1000"),
                null
        );

        when(mockUserDao.getUserByUsername("john"))
                .thenReturn(mockUser);

        when(mockUserDao.updateUserBalance(any(User.class)))
                .thenReturn(true);

        try (MockedStatic<JwtUtil> jwtMock = mockStatic(JwtUtil.class)) {

            jwtMock.when(() -> JwtUtil.getUsernameFromToken("fake.jwt.token"))
                   .thenReturn("john");

            DepositServlet servlet = new DepositServlet(mockUserDao);

            servlet.doPost(request, response);

            writer.flush();
            String output = stringWriter.toString();

            assertTrue(output.contains("Deposit Successful"));
            assertTrue(output.contains("1500"));
        }
    }
}
