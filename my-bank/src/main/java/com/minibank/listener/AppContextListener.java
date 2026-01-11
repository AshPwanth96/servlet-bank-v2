package com.minibank.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.minibank.config.LiquibaseRunner;
import com.minibank.util.DataSourceUtil;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("App started");
		LiquibaseRunner.run();
	}
    @Override
	   public void contextDestroyed(ServletContextEvent sce) {
	        DataSourceUtil.closeDataSource();  
	    }
}
