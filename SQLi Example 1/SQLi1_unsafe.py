"""
    This code contains an SQL query string that could potentially suffer from an
    SQL injection attack. It is meant to be flagged by a SAST tool for possible SQL injection.

    Concatenating user input directly into the SQL query string creates this vulnerability.
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

        # Vulnerable query construction
        query = f"SELECT * FROM users WHERE username = '{username}' AND password = '{password}'"

        # Execute the query
        cursor.execute(query)
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
