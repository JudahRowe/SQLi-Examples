/*
    This code contains an SQL query string that is protected against a second-order
    SQL injection attack. It is not meant to be flagged by a SAST tool for possible SQL injection.

    The first SQL statement securely inserts user-inputted data into the database. 
    The second SQL statement protects against malicious code as well by using parameterized queries.
*/

const mysql = require('mysql2/promise');

(async () => {
    const connection = await mysql.createConnection({
        host: 'localhost',
        user: 'root',
        password: 'password',
        database: 'exampledb'
    });

    // Securely store user data
    const maliciousUsername = "malicious' OR 1=1; DROP TABLE users; --";
    await connection.execute("INSERT INTO users (username, password) VALUES (?, ?)", [maliciousUsername, 'securepassword']);

    // Retrieve users
    const [users] = await connection.execute("SELECT username FROM users");
    for (const user of users) {
        const username = user.username;

        // Safe batch operation
        await connection.execute("UPDATE users SET password = ? WHERE username = ?", ['newpassword', username]);
    }

    await connection.end();
})();
