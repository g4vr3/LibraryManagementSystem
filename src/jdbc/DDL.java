package jdbc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * The type DDL (Data Definition Language) handles database connection,
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

            // Check if the database exists
            if (!databaseExists()) {
                System.out.println("La base de datos no existe. Creando base de datos...");
                createDatabase();  // Create the database
                System.out.println("Base de datos creada.");

                // Execute the SQL script to set up the database schema and insert initial data
                executeSQLScript();
            }

            // Use the specified database
            Statement st = conn.createStatement();
            st.execute("USE " + DB);
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver de base de datos no encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error al conectar o ejecutar SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error al leer el archivo SQL: " + e.getMessage());
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
                    // ignore empty lines and comments
                    continue;
                }
                sql.append(line).append(" ");  // add line
                if (line.endsWith(";")) {
                    // execute sentence
                    stmt.execute(sql.toString());
                    sql.setLength(0);  // clear buffer
                }
            }
        } catch (IOException | SQLException e) {
            System.err.println("Error al ejecutar el script SQL: " + e.getMessage());
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
