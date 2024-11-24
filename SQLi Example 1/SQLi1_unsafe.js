/*
    This code contains an SQL query string that could potentially suffer from an
    SQL injection attack. It is meant to be flagged by a SAST tool for possible SQL injection.

    Concatenating user input directly into the SQL query string creates this vulnerability.
*/

const readline = require('readline');
const mysql = require('mysql');

// Create a readline interface for user input
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

// Create a connection to the database
const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: 'password',
  database: 'exampledb'
});

connection.connect(err => {
  if (err) {
    console.error('Error connecting to the database:', err);
    process.exit(1);
  }
});

// Prompt user for input
rl.question('Enter username: ', username => {
  rl.question('Enter password: ', password => {
    // Vulnerable query construction
    const query = `SELECT * FROM users WHERE username = '${username}' AND password = '${password}'`;

    // Execute the query
    connection.query(query, (err, results) => {
      if (err) {
        console.error('Error executing query:', err);
      } else if (results.length > 0) {
        console.log('Login successful!');
      } else {
        console.log('Invalid username or password.');
      }

      // Close the connection and readline interface
      connection.end();
      rl.close();
    });
  });
});
