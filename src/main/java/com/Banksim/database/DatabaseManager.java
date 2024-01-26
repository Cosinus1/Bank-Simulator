package main.java.com.Banksim.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    // Update the path to the SQLite JDBC driver
    private static final String SQLITE_DRIVER_PATH = "/home/paul/Documents/Projet S7/Pour les élèves/Projet_type/Bank-Simulator/lib/sqlite-jdbc-3.34.0.jar";

    static {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading SQLite JDBC driver: " + e.getMessage());
        }
    }

    public static Connection connect() {
        try {
            // Include the SQLite JDBC driver in the classpath
            String jdbcUrl = "jdbc:sqlite:" + SQLITE_DRIVER_PATH;
            Connection connection = DriverManager.getConnection(jdbcUrl);
            initializeDatabase(connection); // Initialize tables if not exists
            return connection;
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void initializeDatabase(Connection connection) {
        try {
            // Initialize BankAccounts table
            String createBankAccountsTable = "CREATE TABLE IF NOT EXISTS BankAccounts (" +
                    "accountNumber VARCHAR(20) PRIMARY KEY, " +
                    "accountHolderName VARCHAR(50), " +
                    "balance DECIMAL(10, 2)" +
                    ")";
            connection.createStatement().executeUpdate(createBankAccountsTable);

            // Initialize Banks table
            String createBanksTable = "CREATE TABLE IF NOT EXISTS Banks (" +
                    "bankCode VARCHAR(3) PRIMARY KEY, " +
                    "bankName VARCHAR(50)" +
                    ")";
            connection.createStatement().executeUpdate(createBanksTable);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database connection: " + e.getMessage());
        }
    }
}
