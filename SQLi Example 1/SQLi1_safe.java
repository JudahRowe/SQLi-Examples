/*
    This code contains an SQL query string that is protected against SQL injection
    attacks. It is not meant to be flagged by a SAST tool for possible SQL injection.

    Instead of concatenating user input directly into the SQL query string,
    we use prepared statements with parameterized queries.
*/

import java.sql.*;
import java.util.Scanner;

public class SQLi1_safe {
    public static void main(String[] args) {
        // Set up a Scanner to read user input
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter username: ");
        String username = scanner.nextLine(); // Read username from user input

        System.out.println("Enter password: ");
        String password = scanner.nextLine(); // Read password from user input

        try {
            // Establish a database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "password");

            // Safe query construction
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";

            // Create a statement and execute it
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Invalid username or password.");
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
            connection.close();
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
