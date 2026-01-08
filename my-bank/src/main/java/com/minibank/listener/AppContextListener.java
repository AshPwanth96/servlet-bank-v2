package com.minibank.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import com.minibank.util.DataSourceUtil;

@WebListener
public class AppContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("App started");
	}
	
	   public void contextDestroyed(ServletContextEvent sce) {
	        DataSourceUtil.closeDataSource();  
	    }
}
