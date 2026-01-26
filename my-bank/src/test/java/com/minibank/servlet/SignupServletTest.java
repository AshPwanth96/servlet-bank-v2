package com.minibank.servlet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import com.minibank.dao.UserDao;

class SignupServletTest {

    @Test
    void testSignup_success() throws Exception {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String jsonBody =
                "{ \"username\": \"john\", " +
                "\"password\": \"password123\", " +
                "\"fullName\": \"John Doe\", " +
                "\"email\": \"john@test.com\" }";

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

        when(mockUserDao.getUserByUsername("john")).thenReturn(null);

        when(mockUserDao.addUser(any())).thenReturn(true);

        SignupServlet servlet = new SignupServlet();

        Field userDaoField = SignupServlet.class.getDeclaredField("userDao");
        userDaoField.setAccessible(true);
        userDaoField.set(servlet, mockUserDao);

        servlet.doPost(request, response);

        writer.flush();
        String output = stringWriter.toString();

        assertTrue(output.contains("User created successfully"));
        assertTrue(output.contains("true"));
    }
}
