package com.minibank.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceUtil {

    private static HikariDataSource dataSource;
    private static boolean initialized = false;

    private static void init() {
        if (initialized) return;

        try {
            Properties props = new Properties();
            InputStream is = DataSourceUtil.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties");

            if (is == null) {
                return;
            }

            props.load(is);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("jdbc.url"));
            config.setUsername(props.getProperty("jdbc.username"));
            config.setPassword(props.getProperty("jdbc.password"));
            config.setDriverClassName(props.getProperty("jdbc.driverClassName"));
            config.setMaximumPoolSize(10);

            dataSource = new HikariDataSource(config);
            initialized = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize HikariCP", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        init();
        if (dataSource == null) {
            throw new SQLException("DataSource not initialized");
        }
        return dataSource.getConnection();
    }

    public static HikariDataSource getDataSource() {
        init();
        return dataSource;
    }

    public static void closeDataSource() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
