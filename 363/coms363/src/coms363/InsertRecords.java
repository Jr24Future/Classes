package coms363;

import java.sql.*;

public class InsertRecords {

    private static Connection connect = null;

    public static void main(String[] args) {
        try {
            // Set up connection parameters
            String userName = "coms363";
            String password = "password";
            String dbServer = "jdbc:mysql://localhost:3306/project1?allowLoadLocalInfile=true";            
            // Set up connection
            connect = DriverManager.getConnection(dbServer, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initiate SQL statement
        Statement stmt = null;
        try {
            stmt = connect.createStatement();

            // Load data into 'students' table
            String load_students = "LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/students.csv' "
                    + "INTO TABLE students "
                    + "FIELDS TERMINATED BY ',' "
                    + "ENCLOSED BY '\"' "
                    + "LINES TERMINATED BY '\\r\\n' "
                    + "IGNORE 1 ROWS "
                    + "(snum, ssn, name, gender, dob, c_addr, c_phone, p_addr, p_phone);";
            stmt.executeUpdate(load_students);
            System.out.println("Loaded data into students table");

            // Load data into 'departments' table
            String load_departments = "LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/departments.csv' "
                    + "INTO TABLE departments "
                    + "FIELDS TERMINATED BY ',' "
                    + "ENCLOSED BY '\"' "
                    + "LINES TERMINATED BY '\\r\\n' "
                    + "IGNORE 1 ROWS (code, name, phone, college);";
            stmt.executeUpdate(load_departments);
            System.out.println("Loaded data into departments table");

            // Load data into 'degrees' table
            String load_degrees = "LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/degrees.csv' "
                    + "INTO TABLE degrees "
                    + "FIELDS TERMINATED BY ',' "
                    + "ENCLOSED BY '\"' "
                    + "LINES TERMINATED BY '\\r\\n' "
                    + "IGNORE 1 ROWS (name, level);";
            stmt.executeUpdate(load_degrees);
            System.out.println("Loaded data into degrees table");

            // Load data into 'courses' table
            String load_courses = "LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/courses.csv' "
                    + "INTO TABLE courses "
                    + "FIELDS TERMINATED BY ',' "
                    + "ENCLOSED BY '\"' "
                    + "LINES TERMINATED BY '\\r\\n' "
                    + "IGNORE 1 ROWS (number, name, description, credithours, level);";
            stmt.executeUpdate(load_courses);
            System.out.println("Loaded data into courses table");

            // Load data into 'register' table
            String load_register = "LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/register.csv' "
                    + "INTO TABLE register "
                    + "FIELDS TERMINATED BY ',' "
                    + "ENCLOSED BY '\"' "
                    + "LINES TERMINATED BY '\\r\\n' "
                    + "IGNORE 1 ROWS (snum, course_number, regtime, grade);";
            stmt.executeUpdate(load_register);
            System.out.println("Loaded data into register table");

            // Load data into 'major' table
            String load_major = "LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/major.csv' "
                    + "INTO TABLE major "
                    + "FIELDS TERMINATED BY ',' "
                    + "ENCLOSED BY '\"' "
                    + "LINES TERMINATED BY '\\r\\n' "
                    + "IGNORE 1 ROWS (snum, name, level);";
            stmt.executeUpdate(load_major);
            System.out.println("Loaded data into major table");

            // Load data into 'minor' table
            String load_minor = "LOAD DATA LOCAL INFILE 'H:/errol/Documents/363/project1/data/minor.csv' "
                    + "INTO TABLE minor "
                    + "FIELDS TERMINATED BY ',' "
                    + "ENCLOSED BY '\"' "
                    + "LINES TERMINATED BY '\\r\\n' "
                    + "IGNORE 1 ROWS (snum, name, level);";
            stmt.executeUpdate(load_minor);
            System.out.println("Loaded data into minor table");

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
