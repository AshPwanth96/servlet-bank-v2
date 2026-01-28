package com.minibank.servlet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibank.dao.UserDao;
import com.minibank.dto.ApiResponse;
import com.minibank.model.User;
import com.minibank.util.JwtUtil;

public class DashboardServletTest {

    @Test
    void testDashboard_success() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer fake.jwt.token");

        UserDao mockDao = mock(UserDao.class);

        User user = new User(
                1,
                "john",
                "hashed",
                "John Doe",
                "john@test.com",
                new BigDecimal("1000"),
                new Timestamp(System.currentTimeMillis())
        );

        when(mockDao.getUserByUsername("john")).thenReturn(user);

        try (MockedStatic<JwtUtil> jwtMock = mockStatic(JwtUtil.class)) {

            jwtMock.when(() -> JwtUtil.getUsernameFromToken("fake.jwt.token"))
                   .thenReturn("john");

            DashboardServlet servlet = new DashboardServlet(mockDao);

            servlet.doGet(request, response);
        }

        writer.flush();

        ApiResponse apiResponse =
                new ObjectMapper().readValue(stringWriter.toString(), ApiResponse.class);

        assertTrue(apiResponse.isSuccess());
    }
}
