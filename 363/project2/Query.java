package coms363;

import java.sql.*;

public class Query {
    private static Connection connect = null;

    public static void main(String[] args) {
        try {
            String userName = "coms363";
            String password = "password";
            String dbServer = "jdbc:mysql://localhost:3306/project1";
            connect = DriverManager.getConnection(dbServer, userName, password);

            Statement stmt = connect.createStatement();

            // 1. Query: Numbers, names of courses, and their corresponding average grades
            System.out.println("Query 1: Courses and their average grades");
            String query1 = "SELECT courses.number, courses.name, AVG(register.grade) AS average_grade " +
                            "FROM courses " +
                            "JOIN register ON courses.number = register.course_number " +
                            "GROUP BY courses.number, courses.name;";
            ResultSet rs1 = stmt.executeQuery(query1);
            while (rs1.next()) {
                System.out.println("Course Number: " + rs1.getInt("number") +
                                   ", Course Name: " + rs1.getString("name") +
                                   ", Average Grade: " + rs1.getDouble("average_grade"));
            }

            System.out.println("\nQuery 2: Count of female students majoring or minoring in LAS departments");
            String query2 = "SELECT COUNT(DISTINCT students.snum) AS female_count " +
                            "FROM students " +
                            "JOIN major ON students.snum = major.snum " +
                            "JOIN degrees ON major.name = degrees.name AND major.level = degrees.level " +
                            "JOIN departments ON degrees.department_code = departments.code " +
                            "WHERE students.gender = 'F' AND departments.college = 'LAS' " +
                            "UNION ALL " +
                            "SELECT COUNT(DISTINCT students.snum) AS female_count " +
                            "FROM students " +
                            "JOIN minor ON students.snum = minor.snum " +
                            "JOIN degrees ON minor.name = degrees.name AND minor.level = degrees.level " +
                            "JOIN departments ON degrees.department_code = departments.code " +
                            "WHERE students.gender = 'F' AND departments.college = 'LAS';";
            ResultSet rs2 = stmt.executeQuery(query2);
            if (rs2.next()) {
                System.out.println("Count of female students: " + rs2.getInt("female_count"));
            }

            // 3. Query: Degrees that have more male students than female students
            System.out.println("\nQuery 3: Degrees with more male students than female students");
            String query3 = "SELECT degrees.name, degrees.level " +
                            "FROM degrees " +
                            "JOIN major ON degrees.name = major.name AND degrees.level = major.level " +
                            "JOIN students ON major.snum = students.snum " +
                            "GROUP BY degrees.name, degrees.level " +
                            "HAVING SUM(CASE WHEN students.gender = 'M' THEN 1 ELSE 0 END) > SUM(CASE WHEN students.gender = 'F' THEN 1 ELSE 0 END) " +
                            "UNION ALL " +
                            "SELECT degrees.name, degrees.level " +
                            "FROM degrees " +
                            "JOIN minor ON degrees.name = minor.name AND degrees.level = minor.level " +
                            "JOIN students ON minor.snum = students.snum " +
                            "GROUP BY degrees.name, degrees.level " +
                            "HAVING SUM(CASE WHEN students.gender = 'M' THEN 1 ELSE 0 END) > SUM(CASE WHEN students.gender = 'F' THEN 1 ELSE 0 END);";
            ResultSet rs3 = stmt.executeQuery(query3);
            while (rs3.next()) {
                System.out.println("Degree Name: " + rs3.getString("name") +
                                   ", Degree Level: " + rs3.getString("level"));
            }

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
