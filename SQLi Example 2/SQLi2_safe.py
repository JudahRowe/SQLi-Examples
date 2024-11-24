"""
    This code contains an SQL query string that is protected against a second-order
    SQL injection attack. It is not meant to be flagged by a SAST tool for possible SQL injection.

    The first SQL statement securely inserts user-inputted data into the database. 
    The second SQL statement protects against malicious code as well by using parameterized queries.
"""
import sqlite3

# Connect to the database
conn = sqlite3.connect('example.db')

# Securely store user data
malicious_username = "malicious' OR 1=1; DROP TABLE users; --"
with conn:
    conn.execute("INSERT INTO users (username, password) VALUES (?, ?)", (malicious_username, 'securepassword'))

# Retrieve users
cursor = conn.cursor()
cursor.execute("SELECT username FROM users")
usernames = [row[0] for row in cursor.fetchall()]

# Vulnerable batch operation
for username in usernames:
    conn.execute("UPDATE users SET password = ? WHERE username = ?", ('newpassword', username))

conn.close()
