package database;

import java.sql.*;
import javax.swing.*;

public class DatabaseManager {

    private Connection conn;

    public DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/expense_tracker", 
                "root", 
                ""
            );
            System.out.println("Database connected!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cannot connect to database!");
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void insertExpense(String desc, double amount, String date) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO expenses (description, amount, date) VALUES (?, ?, ?)"
            );
            stmt.setString(1, desc);
            stmt.setDouble(2, amount);
            stmt.setString(3, date);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBudget(String category, double allocated) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO budgets (category, allocated) VALUES (?, ?)"
            );
            stmt.setString(1, category);
            stmt.setDouble(2, allocated);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if(conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
