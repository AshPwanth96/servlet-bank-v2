package com.minibank.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.minibank.model.User;
import com.minibank.util.DataSourceUtil;


public class UserDaoTest {
	
	@Test
	void testGetUserByUsername_found() throws Exception{
		
		Connection mockConnection = mock(Connection.class);
		PreparedStatement mockStatement = mock(PreparedStatement.class);
		ResultSet mockResultSet = mock(ResultSet.class);
		
		try(MockedStatic<DataSourceUtil> mockedStatic = mockStatic(DataSourceUtil.class)){
			
			mockedStatic.when(DataSourceUtil::getConnection)
			.thenReturn(mockConnection);
			
			
			when(mockConnection.prepareStatement(anyString()))
			.thenReturn(mockStatement);
			
			when(mockStatement.executeQuery())
		    .thenReturn(mockResultSet);
			
			when(mockResultSet.next()).thenReturn(true);
			
			
			when(mockResultSet.getInt("id")).thenReturn(1);
			when(mockResultSet.getString("username")).thenReturn("john");
			when(mockResultSet.getString("password")).thenReturn("hashed");
			when(mockResultSet.getString("full_name")).thenReturn("John Doe");
			when(mockResultSet.getString("email")).thenReturn("John@test.com");
			when(mockResultSet.getBigDecimal("balance")).thenReturn(new BigDecimal("1000"));
			when(mockResultSet.getTimestamp("created_at"))
			.thenReturn(new Timestamp(System.currentTimeMillis()));
			
			UserDao userDao = new UserDao();
			
			User user = userDao.getUserByUsername("john");
			
			assertNotNull(user);
		}
	}
	
	@Test
	void testGetUserByUsername_notFound() throws Exception{
		
		Connection mockConnection = mock(Connection.class);
		PreparedStatement mockStatement = mock(PreparedStatement.class);
		ResultSet mockResultSet = mock(ResultSet.class);
		
		try(MockedStatic<DataSourceUtil> mockedStatic = mockStatic(DataSourceUtil.class)){
			
			
			mockedStatic.when(DataSourceUtil::getConnection)
			.thenReturn(mockConnection);
			
			when(mockConnection.prepareStatement(anyString()))
			.thenReturn(mockStatement);
			
			when(mockStatement.executeQuery())
			.thenReturn(mockResultSet);
			
			when(mockResultSet.next()).thenReturn(false);
			
			
			UserDao userDao = new UserDao();
			
			User user = userDao.getUserByUsername("unknown");
			
			assertNull(user);
		}
	}
	
	
    @Test
	void testAddUser_success() throws Exception{
		 Connection mockConnection = mock(Connection.class);
		    PreparedStatement mockStatement = mock(PreparedStatement.class);

		    try (MockedStatic<DataSourceUtil> mockedStatic = mockStatic(DataSourceUtil.class)) {

		        mockedStatic.when(DataSourceUtil::getConnection)
		                    .thenReturn(mockConnection);

		        when(mockConnection.prepareStatement(anyString()))
		                .thenReturn(mockStatement);

		        when(mockStatement.executeUpdate())
		        .thenReturn(1);
		        
		        UserDao userDao = new UserDao();
		        
		        User user = new User("john", "hashed", "john doe", "john@test.com", new BigDecimal("1000"));
		        
		        boolean result = userDao.addUser(user);
		        
		        assertTrue(result);
		    }
	}
		    
		    @Test
		    void testAddUser_failure() throws Exception {

		        Connection mockConnection = mock(Connection.class);
		        PreparedStatement mockStatement = mock(PreparedStatement.class);

		        try (MockedStatic<DataSourceUtil> mockedStatic = mockStatic(DataSourceUtil.class)) {

		            mockedStatic.when(DataSourceUtil::getConnection)
		                        .thenReturn(mockConnection);

		            when(mockConnection.prepareStatement(anyString()))
		                    .thenReturn(mockStatement);

		            when(mockStatement.executeUpdate())
		                    .thenReturn(0);

		            UserDao userDao = new UserDao();

		            User user = new User(
		                    "john", "hashed", "John Doe", "john@test.com", new BigDecimal("1000"));

		            boolean result = userDao.addUser(user);

		            assertFalse(result);
		            
		        }
		    }
		    
		    @Test
		    void testUpdateUserBalance_success() throws Exception{
		    	 Connection mockConnection = mock(Connection.class);
		    	    PreparedStatement mockStatement = mock(PreparedStatement.class);

		    	    try (MockedStatic<DataSourceUtil> mockedStatic = mockStatic(DataSourceUtil.class)) {

		    	        mockedStatic.when(DataSourceUtil::getConnection)
		    	                    .thenReturn(mockConnection);

		    	        when(mockConnection.prepareStatement(anyString()))
		    	                .thenReturn(mockStatement);
		    	        
		    	        when(mockStatement.executeUpdate())
		    	        .thenReturn(1);
		    	        
		    	        UserDao userDao = new UserDao();

		    	        User user = new User(
		    	                1, "john", "hashed", "John Doe", "john@test.com", new BigDecimal("1500"), new Timestamp(System.currentTimeMillis()));

		    	        boolean result = userDao.updateUserBalance(user);

		    	        assertTrue(result);
		    	    }
		    }

		    @Test
		    void testUpdateUserBalance_failure() throws Exception {

		        Connection mockConnection = mock(Connection.class);
		        PreparedStatement mockStatement = mock(PreparedStatement.class);

		        try (MockedStatic<DataSourceUtil> mockedStatic = mockStatic(DataSourceUtil.class)) {

		            mockedStatic.when(DataSourceUtil::getConnection)
		                        .thenReturn(mockConnection);

		            when(mockConnection.prepareStatement(anyString()))
		                    .thenReturn(mockStatement);

		            when(mockStatement.executeUpdate())
		                    .thenReturn(0);

		            UserDao userDao = new UserDao();

		            User user = new User(
		                    1, "john", "hashed", "John Doe", "john@test.com", new BigDecimal("1500"), new Timestamp(System.currentTimeMillis()) );

		            boolean result = userDao.updateUserBalance(user);

		            assertFalse(result);
		        }
		    }

}
