/*
    This code contains an SQL query string that could potentially suffer from an
    SQL injection attack. It is meant to be flagged by a SAST tool for possible SQL injection.

    Concatenating user input directly into the SQL query string creates this vulnerability.
*/

import java.sql.*;
import java.util.Scanner;

public class SQLi1_unsafe {
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

            // Vulnerable query construction
            String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";

            // Create a statement and execute it
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Invalid username or password.");
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
