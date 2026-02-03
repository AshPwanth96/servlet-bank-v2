package com.minibank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.minibank.model.User;

public class UserDao {

    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean addUser(User user) {
        String sql = "insert into users(username, password, full_name, email, balance) values (?, ?, ?, ?, ?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setBigDecimal(
            	    5,
            	    user.getBalance() != null ? user.getBalance() : java.math.BigDecimal.ZERO
            	);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
        	e.printStackTrace(); 
            return false;
            
        }
    }

    public User getUserByUsername(String username) {
        String sql = "select * from users where username = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getBigDecimal("balance"),
                        rs.getTimestamp("created_at")
                );
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean updateUserBalance(User user) {
        String sql = "update users set balance = ? where id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, user.getBalance());
            ps.setInt(2, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
