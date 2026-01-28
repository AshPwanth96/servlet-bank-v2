package com.minibank.servlet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import com.minibank.dao.UserDao;
import com.minibank.model.User;

class LoginServletTest {

    @Test
    void testLogin_success() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String jsonBody = "{ \"username\": \"john\", \"password\": \"password123\" }";
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(jsonBody.getBytes(StandardCharsets.UTF_8));

        ServletInputStream servletInputStream = new ServletInputStream() {

            @Override
            public int read() {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return inputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };

        when(request.getInputStream()).thenReturn(servletInputStream);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        UserDao mockUserDao = mock(UserDao.class);

        String hashedPassword = BCrypt.hashpw("password123", BCrypt.gensalt());

        User mockUser = new User(
                1,
                "john",
                hashedPassword,
                "John Doe",
                "john@test.com",
                new BigDecimal("1000"),
                null
        );

        when(mockUserDao.getUserByUsername("john")).thenReturn(mockUser);

        LoginServlet servlet = new LoginServlet(mockUserDao);

        servlet.doPost(request, response);

        writer.flush();
        String output = stringWriter.toString();

        assertTrue(output.contains("Login Successful"));
        assertTrue(output.contains("true"));
    }
}
