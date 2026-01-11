package com.minibank.config;

import java.sql.Connection;

import javax.sql.DataSource;

import com.minibank.util.DataSourceUtil;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

public class LiquibaseRunner {
	
	public static void run() {
		try(Connection connection = DataSourceUtil.getConnection()){
			
			Database database = DatabaseFactory.getInstance()
					.findCorrectDatabaseImplementation(
							new JdbcConnection(connection));
			
			Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);
			
			liquibase.update(new Contexts(), new LabelExpression());
			
			System.out.println("LiquiBase executed successfully");
		}catch(Exception e) {
			throw new RuntimeException("Liquibase Execution failed", e);
		}
	}

}
