"""
    This code contains an SQL query string that is protected against SQL injection
    attacks. It is not meant to be flagged by a SAST tool for possible SQL injection.

    Instead of concatenating user input directly into the SQL query string,
    we use parameterized queries.
"""


import sqlite3

def main():
    # Prompt the user for input
    username = input("Enter username: ")
    password = input("Enter password: ")

    try:
        # Connect to the database (for simplicity, using SQLite in this example)
        connection = sqlite3.connect("example.db")
        cursor = connection.cursor()

        # Safe query construction
        query = "SELECT * FROM users WHERE username = ? AND password = ?"

        # Execute the query
        cursor.execute(query, (username, password))
        result = cursor.fetchall()

        if result:
            print("Login successful!")
        else:
            print("Invalid username or password.")

        # Close the connection
        connection.close()

    except Exception as e:
        print(f"An error occurred: {e}")

if __name__ == "__main__":
    main()
