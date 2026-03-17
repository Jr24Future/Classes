package coms363;

import java.sql.*;

public class CreateTables {

    private static Connection connect = null;

    public static void main(String[] args) {
        try {
            // Set up connection parameters
            String userName = "coms363";
            String password = "password";
            String dbServer = "jdbc:mysql://localhost:3306/project1";
            
            // Set up connection
            connect = DriverManager.getConnection(dbServer, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initiate SQL statement
        Statement stmt = null;
        try {
            stmt = connect.createStatement();

            // Drop the tables in reverse order to avoid foreign key constraint issues
            stmt.executeUpdate("DROP TABLE IF EXISTS major;");
            stmt.executeUpdate("DROP TABLE IF EXISTS minor;");
            stmt.executeUpdate("DROP TABLE IF EXISTS register;");
            stmt.executeUpdate("DROP TABLE IF EXISTS courses;");
            stmt.executeUpdate("DROP TABLE IF EXISTS degrees;");
            stmt.executeUpdate("DROP TABLE IF EXISTS departments;");
            stmt.executeUpdate("DROP TABLE IF EXISTS students;");
            
            System.out.println("Dropped all tables in correct order");

            // Create 'students' table
            String create_students = "CREATE TABLE students ("
                    + "snum INT, "
                    + "ssn INT PRIMARY KEY, "
                    + "name VARCHAR(20), "
                    + "gender VARCHAR(1), "
                    + "dob VARCHAR(10), "
                    + "c_addr VARCHAR(50), " // Increased length for c_addr
                    + "c_phone VARCHAR(15), "
                    + "p_addr VARCHAR(50), " // Increased length for p_addr
                    + "p_phone VARCHAR(15), "
                    + "UNIQUE(snum)"
                    + ");";
            stmt.executeUpdate(create_students);
            System.out.println("Created students table");

            // Create 'departments' table
            String create_departments = "CREATE TABLE departments ("
                    + "code INT PRIMARY KEY, "
                    + "name VARCHAR(50) UNIQUE, "
                    + "phone VARCHAR(10), "
                    + "college VARCHAR(20)"
                    + ");";
            stmt.executeUpdate(create_departments);
            System.out.println("Created departments table");

            // Create 'degrees' table
            String create_degrees = "CREATE TABLE degrees ("
                    + "name VARCHAR(50), "
                    + "level VARCHAR(5), "
                    + "department_code INT, "
                    + "PRIMARY KEY(name, level), "
                    + "FOREIGN KEY(department_code) REFERENCES departments(code)"
                    + ");";
            stmt.executeUpdate(create_degrees);
            System.out.println("Created degrees table");

            // Create 'courses' table
            String create_courses = "CREATE TABLE courses ("
                    + "number INT PRIMARY KEY, "
                    + "name VARCHAR(50), "
                    + "description VARCHAR(50), "
                    + "credithours INT, "
                    + "level VARCHAR(20), "
                    + "department_code INT, "
                    + "FOREIGN KEY(department_code) REFERENCES departments(code)"
                    + ");";
            stmt.executeUpdate(create_courses);
            System.out.println("Created courses table");

            // Create 'register' table
            String create_register = "CREATE TABLE register ("
                    + "snum INT, "
                    + "course_number INT, "
                    + "regtime VARCHAR(20), "
                    + "grade INT, "
                    + "PRIMARY KEY(snum, course_number), "
                    + "FOREIGN KEY(snum) REFERENCES students(snum), "
                    + "FOREIGN KEY(course_number) REFERENCES courses(number)"
                    + ");";
            stmt.executeUpdate(create_register);
            System.out.println("Created register table");

            // Create 'major' table
            String create_major = "CREATE TABLE major ("
                    + "snum INT, "
                    + "name VARCHAR(50), "
                    + "level VARCHAR(5), "
                    + "PRIMARY KEY(snum, name, level), "
                    + "FOREIGN KEY(snum) REFERENCES students(snum), "
                    + "FOREIGN KEY(name, level) REFERENCES degrees(name, level)"
                    + ");";
            stmt.executeUpdate(create_major);
            System.out.println("Created major table");

            // Create 'minor' table
            String create_minor = "CREATE TABLE minor ("
                    + "snum INT, "
                    + "name VARCHAR(50), "
                    + "level VARCHAR(5), "
                    + "PRIMARY KEY(snum, name, level), "
                    + "FOREIGN KEY(snum) REFERENCES students(snum), "
                    + "FOREIGN KEY(name, level) REFERENCES degrees(name, level)"
                    + ");";
            stmt.executeUpdate(create_minor);
            System.out.println("Created minor table");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close connection
                if (stmt != null) {
                    stmt.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
