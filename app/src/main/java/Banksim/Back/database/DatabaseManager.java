package Banksim.Back.database;

import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseManager {
    private static final String SQLITE_DRIVER_PATH = "org.sqlite.JDBC";

    static {
        // Explicitly load the SQLite JDBC driver
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            classLoader.loadClass(SQLITE_DRIVER_PATH);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading SQLite JDBC driver: " + e.getMessage());
        }
    }

    public static Connection connect(String DATABASE_PATH) {
        try {
            // Explicitly load the SQLite JDBC driver (optional, try with and without)
            Class.forName(SQLITE_DRIVER_PATH);
    
            String jdbcUrl = "jdbc:sqlite:" + DATABASE_PATH;
            Connection connection = DriverManager.getConnection(jdbcUrl);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    

    public static void initializeBankDatabase(Connection connection, String ID) {
        try {
            // Drop BankAccounts table if it exists
            String dropBankAccountsTable = "DROP TABLE IF EXISTS BankAccounts";
            connection.createStatement().executeUpdate(dropBankAccountsTable);

            // Drop Card table if it exists
            String dropCardTable = "DROP TABLE IF EXISTS Card";
            connection.createStatement().executeUpdate(dropCardTable);

            // Initialize BankAccounts table
            String createBankAccountsTable = "CREATE TABLE IF NOT EXISTS BankAccounts (" +
                    "accountID VARCHAR(20) PRIMARY KEY, " +
                    "accountHolderName VARCHAR(50), " +
                    "balance DECIMAL(10, 2), " +
                    "cardNumber VARCHAR(16)" +  // Add cardNumber column
                    ")";
            connection.createStatement().executeUpdate(createBankAccountsTable);
            System.out.println("BankAccounts table initialized");

            // Initialize Card table
            String createCardTable = "CREATE TABLE IF NOT EXISTS Card (" +
                    "cardNumber VARCHAR(16) PRIMARY KEY, " +
                    "expirationDate VARCHAR(5), " +
                    "pin INT" +
                    ")";
            connection.createStatement().executeUpdate(createCardTable);
            System.out.println("Card table initialized");

            // Insert random bank accounts and associate cards
            Random random = new Random();
            for (int i = 1; i <= 10; i++) {
                String accountID = ID + i;
                String accountHolder = "Account Holder " + i;
                double balance = 1000.0 + random.nextDouble() * 9000.0; // Random balance between 1000 and 10000

                String insertQuery = "INSERT INTO BankAccounts (accountID, accountHolderName, balance, cardNumber) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, accountID);
                preparedStatement.setString(2, accountHolder);
                preparedStatement.setDouble(3, balance);

                // Generate and associate a random card with each bank account
                String cardNumber = generateRandomCardNumber(ID);
                String expirationDate = "09/24";  // Replace with your logic for expiration date
                int pin = 1234;  // Replace with your logic for generating a pin

                preparedStatement.setString(4, cardNumber);

                // Insert card details into Card table
                insertCardDetails(connection, cardNumber, expirationDate, pin);

                preparedStatement.executeUpdate();
            }
            // Retrieve and display the bank accounts
            String selectQuery = "SELECT accountID, accountHolderName, balance, cardNumber FROM BankAccounts";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("\n\n----------------------Random Bank Accounts Added to the Database:-------------------------------\n");
            System.out.println("------------------------------------------------------------------------------------------------");
            while (resultSet.next()) {
                String accountID = resultSet.getString("accountID");
                String accountHolder = resultSet.getString("accountHolderName");
                double balance = resultSet.getDouble("balance");
                String cardNumber = resultSet.getString("cardNumber");

                System.out.println("| Account ID: " + accountID + ", Holder: " + accountHolder + ", Balance: " + balance + ", CardNumber: " + cardNumber + " |");
            }
            System.out.println("-----------------------------------------------------------------------------------------------");
    
        } catch (SQLException e) {
            System.err.println("Error initializing database for bank : " + e.getMessage());
        }
    }

    public static void initializeGIECBDatabase(Connection connection) {
        try {
            // Drop Banks table if it exists
            String dropBanksTable = "DROP TABLE IF EXISTS Banks";
            connection.createStatement().executeUpdate(dropBanksTable);

            // Initialize Banks table
            String createBanksTable = "CREATE TABLE IF NOT EXISTS Banks (" +
                    "bankID VARCHAR(3) PRIMARY KEY, " +
                    "bankName VARCHAR(50)" +
                    ")";
            connection.createStatement().executeUpdate(createBanksTable);
            System.out.println("Banks table initialized");
        } catch (SQLException e) {
            System.err.println("Error initializing database for bank : " + e.getMessage());
        }
    }
    
    // Helper method to insert card details into Card table
    private static void insertCardDetails(Connection connection, String cardNumber, String expirationDate, int pin) throws SQLException {
        String insertCardQuery = "INSERT INTO Card (cardNumber, expirationDate, pin) VALUES (?, ?, ?)";
        PreparedStatement cardStatement = connection.prepareStatement(insertCardQuery);
        cardStatement.setString(1, cardNumber);
        cardStatement.setString(2, expirationDate);
        cardStatement.setInt(3, pin);
        cardStatement.executeUpdate();
    }

    // Helper method to generate a random 16-digit card number
    private static String generateRandomCardNumber(String bankID) {
        Random random = new Random();
        StringBuilder cardNumberBuilder = new StringBuilder(bankID);
        for (int i = 1; i <= 5; i++) {
            cardNumberBuilder.append(random.nextInt(10));
        }
        return cardNumberBuilder.toString();
    }
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing the database connection: " + e.getMessage());
        }
    }
}
