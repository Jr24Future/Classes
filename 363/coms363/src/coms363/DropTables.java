package coms363;

import java.sql.*;

public class DropTables {

    private static Connection connect = null;

    public static void main(String[] args) {
        try {
            // Set up connection parameters
            String userName = "test";  // Use the coms363 user
            String password = "password";
            String dbServer = "jdbc:mysql://localhost/project1_del_data";
            // Set up connection
            connect = DriverManager.getConnection(dbServer, userName, password);

            // Initialize statement
            Statement stmt = connect.createStatement();

            // Drop tables
            stmt.executeUpdate("DROP TABLE IF EXISTS major;");
            stmt.executeUpdate("DROP TABLE IF EXISTS minor;");
            stmt.executeUpdate("DROP TABLE IF EXISTS register;");
            stmt.executeUpdate("DROP TABLE IF EXISTS courses;");
            stmt.executeUpdate("DROP TABLE IF EXISTS degrees;");
            stmt.executeUpdate("DROP TABLE IF EXISTS departments;");
            stmt.executeUpdate("DROP TABLE IF EXISTS students;");
            System.out.println("All tables dropped successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connect != null) connect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
