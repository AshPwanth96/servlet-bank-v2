package com.minibank.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;





public class UserTest {
	
	@Test
	void testUserConstructionWithoutId() {
		User user = new User(
				"John",
				"secret",
				"John Doe",
				"John@test.com",
				new BigDecimal(1000));
		
		assertEquals("John", user.getUsername());
		assertEquals("secret", user.getPassword());
		assertEquals("John Doe", user.getFullName());
		assertEquals("John@test.com", user.getEmail());
		assertEquals(new BigDecimal("1000"), user.getBalance());
	}
	
	
	@Test
	void testUserConstructorWithId() {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		User user = new User(1, "ana", "pass", "ana armas", "ana@gmail.com", new BigDecimal(500), now);
		
		 assertEquals(1, user.getId());
	        assertEquals("ana", user.getUsername());
	        assertEquals("pass", user.getPassword());
	        assertEquals("ana armas", user.getFullName());
	        assertEquals("ana@gmail.com", user.getEmail());
	        assertEquals(new BigDecimal("500"), user.getBalance());
	        assertEquals(now, user.getCreatedAt());
	}
	
	void testSetters() {
		User user = new User(
				"sam",
				"pwd",
				"Sam",
				"sam@test.com",
				BigDecimal.ZERO);
		
		  user.setUsername("samuel");
	        user.setBalance(new BigDecimal("300"));

	        assertEquals("samuel", user.getUsername());
	        assertEquals(new BigDecimal("300"), user.getBalance());
		
	}

}
