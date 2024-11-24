<?php

/*
    This code contains an SQL query string that is protected against SQL injection
    attacks. It is not meant to be flagged by a SAST tool for possible SQL injection.

    Instead of concatenating user input directly into the SQL query string,
    we use prepared statements with placeholders.
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

    // Prompt user for input (via CLI for simplicity)
    echo "Enter username: ";
    $user = trim(fgets(STDIN)); // Read username from input

    echo "Enter password: ";
    $pass = trim(fgets(STDIN)); // Read password from input

    // Safe query construction
    $query = "SELECT * FROM users WHERE username = :username AND password = :password";

    // Execute the query
    $stmt = $conn->prepare($query);
    $stmt->bindParam(':username', $user, PDO::PARAM_STR);
    $stmt->bindParam(':password', $pass, PDO::PARAM_STR);
    $stmt->execute();
    
    $results = $stmt->fetchAll();
    
    if (count($results) > 0) {
        echo "Login successful!" . PHP_EOL;
    } else {
        echo "Invalid username or password." . PHP_EOL;
    }

} catch (PDOException $e) {
    echo "Database error: " . $e->getMessage() . PHP_EOL;
}
?>
