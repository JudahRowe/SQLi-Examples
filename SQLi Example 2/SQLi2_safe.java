/*
    This code contains an SQL query string that is protected against a second-order
    SQL injection attack. It is not meant to be flagged by a SAST tool for possible SQL injection.

    The first SQL statement securely inserts user-inputted data into the database. 
    The second SQL statement protects against malicious code as well by using parameterized queries.
*/

import java.sql.*;
import java.util.ArrayList;

public class SQLi2_safe {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/exampledb";
        String dbUser = "root";
        String dbPassword = "password";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            // Securely store user data
            String maliciousUsername = "malicious' OR 1=1; DROP TABLE users; --";
            String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, maliciousUsername);
                insertStmt.setString(2, "securepassword");
                insertStmt.executeUpdate();
            }

            // Retrieve users
            String selectQuery = "SELECT username FROM users";
            ArrayList<String> usernames = new ArrayList<>();
            try (Statement selectStmt = conn.createStatement();
                 ResultSet rs = selectStmt.executeQuery(selectQuery)) {
                while (rs.next()) {
                    usernames.add(rs.getString("username"));
                }
            }

            // Safe batch operation
            for (String username : usernames) {
                String updateQuery = "UPDATE users SET password = ? WHERE username = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, "newpassword");
                    updateStmt.setString(2, username);
                    updateStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
