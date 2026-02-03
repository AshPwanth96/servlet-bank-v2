package com.minibank.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import com.minibank.model.User;

public class UserDaoTest {

    @Test
    void testGetUserByUsername_found() throws Exception {
        DataSource ds = mock(DataSource.class);
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(ds.getConnection()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("username")).thenReturn("john");
        when(rs.getString("password")).thenReturn("hashed");
        when(rs.getString("full_name")).thenReturn("John Doe");
        when(rs.getString("email")).thenReturn("john@test.com");
        when(rs.getBigDecimal("balance")).thenReturn(new BigDecimal("1000"));
        when(rs.getTimestamp("created_at")).thenReturn(new Timestamp(System.currentTimeMillis()));

        UserDao dao = new UserDao(ds);
        User user = dao.getUserByUsername("john");

        assertNotNull(user);
    }

    @Test
    void testAddUser_success() throws Exception {
        DataSource ds = mock(DataSource.class);
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(ds.getConnection()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);

        UserDao dao = new UserDao(ds);
        User user = new User("john", "hashed", "John Doe", "john@test.com", new BigDecimal("1000"));

        assertTrue(dao.addUser(user));
    }

    @Test
    void testUpdateUserBalance_failure() throws Exception {
        DataSource ds = mock(DataSource.class);
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);

        when(ds.getConnection()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(0);

        UserDao dao = new UserDao(ds);
        User user = new User(1, "john", "hashed", "John Doe", "john@test.com", new BigDecimal("1500"), new Timestamp(System.currentTimeMillis()));

        assertFalse(dao.updateUserBalance(user));
    }
}
