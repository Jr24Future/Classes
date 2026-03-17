package coms363;

import java.sql.*;

public class ModifyRecords {

    private static Connection connect = null;

    public static void main(String[] args) {
        try {
            // Set up connection parameters
            String userName = "coms363";
            String password = "password";
            String dbServer = "jdbc:mysql://localhost:3306/project1";
            connect = DriverManager.getConnection(dbServer, userName, password);

            // Initialize statement
            Statement stmt = connect.createStatement();

            // Update student name with ssn = 144673371 to "Scott"
            String updateName = "UPDATE students SET name = 'Scott' WHERE ssn = 144673371;";
            stmt.executeUpdate(updateName);
            System.out.println("Updated student name to 'Scott'.");

            // Update major for student with ssn = 144673371 to "Computer Science, MS"
            String updateMajor = "UPDATE major SET name = 'Computer Science', level = 'MS' " // Assuming 'MS' fits your schema
                              + "WHERE snum = (SELECT snum FROM students WHERE ssn = 144673371);";
            stmt.executeUpdate(updateMajor);
            System.out.println("Updated student's major to 'Computer Science, MS'.");

            // Delete all registrations for "Summer2024"
            String deleteRegister = "DELETE FROM register WHERE regtime = 'Summer2024';";
            stmt.executeUpdate(deleteRegister);
            System.out.println("Deleted registrations from Summer2024.");

        } catch (Exception e) {
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
