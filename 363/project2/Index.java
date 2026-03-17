package coms363;

import java.sql.*;
import com.github.javafaker.Faker;
import java.util.Locale;
import java.util.Random;

public class Index {
    private static Connection connect = null;

    public static void main(String[] args) {
        try {
            String userName = "coms363";
            String password = "password";
            String dbServer = "jdbc:mysql://localhost:3306/project1";
            connect = DriverManager.getConnection(dbServer, userName, password);

            // Initialize statement
            Statement stmt = connect.createStatement();

            // Insert degree entries into degrees table to ensure foreign key constraint is met
            String[] degreeNames = {"Degree0", "Degree1", "Degree2", "Degree3", "Degree4", "Degree5", "Degree6", "Degree7", "Degree8", "Degree9"};
            String[] levels = {"Bach", "Mast"};
            PreparedStatement pstmtDegree = connect.prepareStatement("INSERT IGNORE INTO degrees (name, level) VALUES (?, ?)");
            for (String degreeName : degreeNames) {
                for (String level : levels) {
                    pstmtDegree.setString(1, degreeName);
                    pstmtDegree.setString(2, level);
                    pstmtDegree.addBatch();
                }
            }
            pstmtDegree.executeBatch();
            System.out.println("Inserted degrees into degrees table.");

            // Use Faker to insert 5000 students
            Faker faker = new Faker(new Locale("en-US"));
            Random random = new Random();
            PreparedStatement pstmt = connect.prepareStatement("INSERT INTO students (snum, ssn, name, gender, dob, c_addr, c_phone, p_addr, p_phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < 5000; i++) {
                pstmt.setInt(1, 2000 + i); // snum
                pstmt.setInt(2, 100000000 + i); // ssn
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String fullName = (firstName + " " + lastName);
                pstmt.setString(3, fullName.length() > 20 ? fullName.substring(0, 20) : fullName); // name limited to 20 characters
                pstmt.setString(4, faker.demographic().sex().substring(0, 1)); // gender
                pstmt.setDate(5, new Date(faker.date().birthday().getTime())); // dob
                String cAddr = faker.address().streetAddress();
                pstmt.setString(6, cAddr.length() > 45 ? cAddr.substring(0, 45) : cAddr); // c_addr limited to 45 characters
                String cPhone = faker.phoneNumber().cellPhone();
                pstmt.setString(7, cPhone.length() > 15 ? cPhone.substring(0, 15) : cPhone); // c_phone limited to 15 characters
                String pAddr = faker.address().streetAddress();
                pstmt.setString(8, pAddr.length() > 45 ? pAddr.substring(0, 45) : pAddr); // p_addr limited to 45 characters
                String pPhone = faker.phoneNumber().cellPhone();
                pstmt.setString(9, pPhone.length() > 15 ? pPhone.substring(0, 15) : pPhone); // p_phone limited to 15 characters
                pstmt.addBatch();
            }

            // Execute batch to insert students
            int[] updateCounts = pstmt.executeBatch();
            System.out.println("Inserted " + updateCounts.length + " students using Faker.");

            // Assign degrees to students (major, minor, or both)
            PreparedStatement pstmtMajor = connect.prepareStatement("INSERT INTO major (snum, name, level) VALUES (?, ?, ?)");
            PreparedStatement pstmtMinor = connect.prepareStatement("INSERT INTO minor (snum, name, level) VALUES (?, ?, ?)");
            for (int i = 0; i < 5000; i++) {
                int snum = 2000 + i;
                String degreeName = degreeNames[random.nextInt(degreeNames.length)]; // Random degree name from available degrees
                String level = levels[random.nextInt(levels.length)]; // Random level from available levels

                // Assign major to the student
                pstmtMajor.setInt(1, snum);
                pstmtMajor.setString(2, degreeName);
                pstmtMajor.setString(3, level);
                pstmtMajor.addBatch();

                // Randomly decide if the student also gets a minor
                if (random.nextBoolean()) {
                    String minorDegreeName = degreeNames[random.nextInt(degreeNames.length)];
                    String minorLevel = levels[random.nextInt(levels.length)];
                    pstmtMinor.setInt(1, snum);
                    pstmtMinor.setString(2, minorDegreeName);
                    pstmtMinor.setString(3, minorLevel);
                    pstmtMinor.addBatch();
                }
            }

            // Execute batch to assign degrees
            pstmtMajor.executeBatch();
            pstmtMinor.executeBatch();
            System.out.println("Assigned degrees to students (major and/or minor).");

            // Execute the query before creating an index on gender and measure execution time
            String query = "SELECT degrees.name, degrees.level " +
                    "FROM degrees " +
                    "JOIN major ON degrees.name = major.name AND degrees.level = major.level " +
                    "JOIN students ON major.snum = students.snum " +
                    "GROUP BY degrees.name, degrees.level " +
                    "HAVING SUM(CASE WHEN students.gender = 'M' THEN 1 ELSE 0 END) > SUM(CASE WHEN students.gender = 'F' THEN 1 ELSE 0 END);";

            long start = System.currentTimeMillis();
            ResultSet rs = stmt.executeQuery(query);
            long executionTimeBeforeIndex = System.currentTimeMillis() - start;
            System.out.println("Execution Time before Index: " + executionTimeBeforeIndex + "ms");

            // Create an index on gender
            stmt.executeUpdate("CREATE INDEX idx_gender ON students(gender);");
            System.out.println("Index created on gender.");

            // Execute the query again after creating the index and measure execution time
            start = System.currentTimeMillis();
            rs = stmt.executeQuery(query);
            long executionTimeAfterIndex = System.currentTimeMillis() - start;
            System.out.println("Execution Time after Index: " + executionTimeAfterIndex + "ms");

        } catch (BatchUpdateException e) {
            System.err.println("Batch update error: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
            e.printStackTrace();
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

/*
 * Maven dependency for Java Faker:
 * 
 * <dependency>
 *     <groupId>com.github.javafaker</groupId>
 *     <artifactId>javafaker</artifactId>
 *     <version>1.0.2</version>
 * </dependency>
 *
 * Add this dependency to your pom.xml file to use Java Faker with Maven.
 */
