package SalonandSpa;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnector {
    
    private static final String DATABASE = "salon_and_spadb";
    private static final String PORT = "3306";
    private static final String URL = "jdbc:mysql://localhost:" + PORT + "/" + DATABASE;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

        // Method to establish a connection to the Access database
    public static Connection connect() {
        Connection connection = null;
        try {
            // Establish a connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            // Handle any errors
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return connection;
    }
    
    // Method to close the database connection
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            // Handle any errors
            System.err.println("Error closing the connection: " + e.getMessage());
        }
    }    
}