package com.minibank.dto;

import java.math.BigDecimal;

public class ResponseDto {
	
	private String username;
	
	private String fullName;
	
	private String email;
	
	private BigDecimal balance;
	
	
	public ResponseDto() {}


	public ResponseDto(String username, String fullName, String email, BigDecimal balance) {
		super();
		this.username = username;
		this.fullName = fullName;
		this.email = email;
		this.balance = balance;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public BigDecimal getBalance() {
		return balance;
	}


	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	
	

}
