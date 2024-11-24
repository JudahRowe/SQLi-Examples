/*
    This code contains an SQL query string that could potentially suffer from a second-order
    SQL injection attack. It is meant to be flagged by a SAST tool for possible SQL injection.

    The first SQL statement securely inserts user-inputted data into the database. 
    Later, it is erroneously assumed to not hold malicious data, creating a vulnerability.
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

        // Vulnerable batch operation
        const query = `UPDATE users SET password = 'newpassword' WHERE username = '${username}'`;
        await connection.query(query);
    }

    await connection.end();
})();
