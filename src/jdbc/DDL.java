package jdbc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * The DDL (Data Definition Language) class handles database connection,
 * checking if the database exists and executing the SQL script to create it if necessary.
 *
 * @version 1.0
 */
public class DDL {
    // Connection details
    static final String URL = "jdbc:mariadb://localhost:3306/";
    static final String USER = "root";
    static final String PASS = "root";

    // Database name
    static final String DB = "Biblioteca";

    // SQL script file path
    static final String SQL_SCRIPT_PATH = "resources/sql/Biblioteca.sql";

    // Database connection
    static Connection conn;

    // Private constructor to handle database setup
    private DDL() {
        try {
            Class.forName("org.mariadb.jdbc.Driver"); // Load MariaDB driver
            conn = DriverManager.getConnection(URL, USER, PASS); // Connect without specifying a database
            conn.setAutoCommit(true); // Enable auto-commit

            // Check if the database exists
            if (!databaseExists()) {
                System.out.println("The database does not exist. Creating database...");
                createDatabase();  // Create the database
                System.out.println("Database created.");

                // Execute the SQL script to set up the database schema and insert initial data
                executeSQLScript();
            }

            // Use the specified database
            Statement st = conn.createStatement();
            st.execute("USE " + DB);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Database driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error connecting or executing SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading the SQL file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create the Database.
     *
     * @throws SQLException the SQL exception
     */
    private void createDatabase() throws SQLException {
        String createDbQuery = "CREATE DATABASE " + DB;
        Statement stmt = conn.createStatement();
        stmt.execute(createDbQuery);
    }

    /**
     * Check if the database exists.
     *
     * @return true if the database exists, false otherwise
     * @throws SQLException the SQL exception
     */
    private boolean databaseExists() throws SQLException {
        String checkDbQuery = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
        PreparedStatement checkDbStmt = conn.prepareStatement(checkDbQuery);
        checkDbStmt.setString(1, DB);
        ResultSet rs = checkDbStmt.executeQuery();
        return rs.next(); // Returns true if the database exists
    }

    /**
     * Execute the SQL script to create the database and its tables.
     *
     * @throws IOException  if an I/O error occurs while reading the file
     * @throws SQLException if an SQL error occurs while executing the script
     */
    private void executeSQLScript() throws IOException, SQLException {
        try (BufferedReader reader = new BufferedReader(new FileReader(DDL.SQL_SCRIPT_PATH));
             Statement stmt = conn.createStatement()) {

            StringBuilder sql = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("--") || line.startsWith("/*")) {
                    // Ignore empty lines and full-line comments
                    continue;
                }

                // Remove any inline comments starting with "--"
                int commentIndex = line.indexOf("--");
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex).trim();
                }

                // Accumulate lines until a command ends with a semicolon
                sql.append(line).append(" ");
                if (line.endsWith(";")) {
                    // Execute the complete command
                    try {
                        stmt.execute(sql.toString().trim()); // Execute the full SQL command
                        sql.setLength(0); // Clear the buffer for the next command
                    } catch (SQLException e) {
                        System.err.println("Error executing SQL line: " + sql.toString().trim());
                        System.err.println("Error: " + e.getMessage());
                        throw e; // Re-throw the exception for further handling
                    }
                }
            }

            // In case there's any remaining SQL command that wasn't terminated by a semicolon
            if (!sql.isEmpty()) {
                try {
                    stmt.execute(sql.toString().trim());
                } catch (SQLException e) {
                    System.err.println("Error executing remaining SQL line: " + sql.toString().trim());
                    System.err.println("Error: " + e.getMessage());
                    throw e;
                }
            }
        }
    }


    /**
     * Gets the database connection.
     *
     * @return the connection
     */
    public static Connection getConnection() {
        if (conn == null)
            new DDL(); // If connection is null, initialize the DDL class to set it up
        return conn;
    }

    /**
     * Closes the database connection.
     *
     * @throws SQLException if an SQL error occurs during connection closure
     */
    public static void closeConnection() throws SQLException {
        if (conn != null) {
            conn.close();
            conn = null;
        }
    }
}
