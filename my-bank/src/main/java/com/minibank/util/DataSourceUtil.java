package com.minibank.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceUtil {
	
	private static HikariDataSource dataSource;
	
	static {
		try {
			Properties props = new Properties();
			InputStream is = DataSourceUtil.class.getClassLoader().getResourceAsStream("db.properties");
			
			if(is == null) {
				throw new RuntimeException("db.properties not found in classpath");
			}
			props.load(is);
			
			HikariConfig config = new HikariConfig();
			
			config.setJdbcUrl(props.getProperty("jdbc.url"));
			config.setUsername(props.getProperty("jdbc.username"));
			config.setPassword(props.getProperty("jdbc.password"));
			config.setDriverClassName(props.getProperty("jdbc.driverClassName"));
			
			config.setMaximumPoolSize(10);
			
			dataSource = new  HikariDataSource(config);
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to initialize HikariCp", e);
		}
	}
	
	public static Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
	
	public static void closeDataSource() {
	    if (dataSource != null) {
	        dataSource.close();
	    }
	}


}
