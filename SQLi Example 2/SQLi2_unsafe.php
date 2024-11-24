<?php

/*
    This code contains an SQL query string that could potentially suffer from a second-order
    SQL injection attack. It is meant to be flagged by a SAST tool for possible SQL injection.

    The first SQL statement securely inserts user-inputted data into the database. 
    Later, it is erroneously assumed to not hold malicious data, creating a vulnerability.
*/

// Database connection details
$host = "localhost";
$dbname = "exampledb";
$username = "root";
$password = "password";

try {
    // Create a new PDO connection
    $conn = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // User registration with parameterized query
    echo "Register a new user\n";
    echo "Enter username: ";
    $newUser = trim(fgets(STDIN)); // User input for username

    echo "Enter password: ";
    $newPassword = trim(fgets(STDIN)); // User input for password

    // Securely store user data
    $insertQuery = "INSERT INTO users (username, password) VALUES (:username, :password)";
    $stmt = $conn->prepare($insertQuery);
    $stmt->bindParam(':username', $newUser, PDO::PARAM_STR);
    $stmt->bindParam(':password', $newPassword, PDO::PARAM_STR);
    $stmt->execute();
    echo "User registered successfully!\n";

    // Simulate admin retrieving users from the database
    echo "\nAdmin: Fetching all users...\n";
    $selectQuery = "SELECT username FROM users";
    $users = $conn->query($selectQuery)->fetchAll(PDO::FETCH_COLUMN);

    // Print out the retrieved usernames (including malicious ones)
    foreach ($users as $user) {
        echo "Found user: $user\n";
    }

    // Simulate an admin operation using retrieved usernames
    echo "\nRunning batch operation on all users...\n";
    foreach ($users as $user) {
        // Vulnerable query using retrieved username directly
        $updateQuery = "UPDATE users SET password = 'newpassword' WHERE username = '$user'";
        $conn->exec($updateQuery); // Second-order SQL injection triggered here
        echo "Updated password for user: $user\n";
    }

} catch (PDOException $e) {
    echo "Database error: " . $e->getMessage() . PHP_EOL;
}
?>
