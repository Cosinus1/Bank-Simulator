package main.java.com.Banksim.Back.assets;

import main.java.com.Banksim.Back.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import main.java.com.Banksim.Back.servers.InterbankNetwork;

public class Bank {
    private static final Map<String, Bank> bankCache = new HashMap<>();

    private String DATABASE_PATH;
    private String ID;

    private Bank(String DATABASE_PATH, String ID) {
        this.ID = ID;
        // Initialize bank accounts from the database
        initializeDatabase();
    }

    public static Bank newBank(String DATABASE_PATH, String ID) {
        // Check if the bank is already in the cache
        if (bankCache.containsKey(ID)) {
            System.out.println("Error: This Bank already exists");
            return bankCache.get(ID);
        }
        // If not in the cache, create a new instance, cache it, and return it
        Bank newBank = new Bank(DATABASE_PATH, ID);
        bankCache.put(ID, newBank);
        return newBank;
    }

    public static Bank getBank(String ID) {
        // Check if the bank is already in the cache
        return bankCache.get(ID);
    }

    public boolean routeRequest(Card card, String AccountID) {
        // Redirect the transaction to GieCB (AcquisitionServer and AuthorizationServer)
        InterbankNetwork gieCB = InterbankNetwork.getInstance();
        boolean authorizationResult = gieCB.routeRequest(card);
        if(authorizationResult){
            return processReceiverTransaction(card,AccountID);
        }else return false;
    }

    public boolean processBuyerTransaction(Card card) {
        // Simulate processing a transaction on the buyer's account
        String cardNumber = card.getCardNumber();
        int amount = card.getTransaction();
        // Find the buyer's bank account based on the card information
        try (Connection connection = DatabaseManager.connect(DATABASE_PATH)) {
            String query = "SELECT * FROM BankAccounts WHERE cardNumber = ?";
    
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, cardNumber);
    
                ResultSet resultSet = preparedStatement.executeQuery();
    
                if (resultSet.next()) {
                    double balance = resultSet.getDouble("balance");
                    if (balance >= amount) {
                        // Sufficient balance, process the transaction
                        balance -= amount;
    
                        // Update the balance in the database
                        String updateQuery = "UPDATE BankAccounts SET balance = ? WHERE cardNumber = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setDouble(1, balance);
                            updateStatement.setString(2, cardNumber);
    
                            int rowsAffected = updateStatement.executeUpdate();
    
                            if (rowsAffected > 0) {
                                System.out.println("Transaction processed successfully for buyer. New balance: " + balance);
                                return true;
                            } else {
                                System.err.println("Error updating balance in the database");
                                return false;
                            }
                        }
                    } else {
                        System.out.println("Transaction failed: Insufficient balance");
                        return false;
                    }
                } else {
                    System.err.println("Error: Retrieving bank account from the database");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving bank account from database: " + e.getMessage());
        }
    
        return false;
    }
    private boolean processReceiverTransaction(Card card, String accountID) {
        // Simulate processing a transaction on the buyer's account
        int amount = card.getTransaction();
    
        try (Connection connection = DatabaseManager.connect(DATABASE_PATH)) {
            // Find the receiver's bank account based on the card information
            String Query = "SELECT * FROM BankAccounts WHERE accountID = ?";
            try (PreparedStatement buyerStatement = connection.prepareStatement(Query)) {
                buyerStatement.setString(1, accountID);
    
                ResultSet resultSet = buyerStatement.executeQuery();
    
                if (resultSet.next()) {
                    int Balance = resultSet.getInt("balance");

                        // Update the receiver's balance in the database
                        int newBalance = Balance + amount;
                        String updateQuery = "UPDATE BankAccounts SET balance = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setDouble(1, newBalance);
        
                            System.out.println("Transaction processed successfully for Receiver. New balance: " + newBalance);
                            return true;
                        }catch (SQLException e) {
                            System.err.println("Error Updating the Balance: " + e.getMessage());
                            return false;
                        }
                } else {
                    System.err.println("Error: Retrieving receiver's bank account from the database");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error processing transaction: " + e.getMessage());
            return false;
        }
    }
    
    

    public Card getRandomCard() {
        try (Connection connection = DatabaseManager.connect(DATABASE_PATH)) {
            String query = "SELECT * FROM Card ORDER BY RANDOM() LIMIT 1";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String cardNumber = resultSet.getString("cardNumber");
                    String expirationDate = resultSet.getString("expirationDate");
                    String pin = resultSet.getString("pin");

                    return new Card(cardNumber, expirationDate, pin);
                } else {
                    System.err.println("Error: No bank accounts found in the database");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving random card from database: " + e.getMessage());
            return null;
        }
    }

    public String getRandomAccountID() {
        try (Connection connection = DatabaseManager.connect(DATABASE_PATH)) {
            String query = "SELECT * FROM BankAccounts ORDER BY RANDOM() LIMIT 1";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String accountID = resultSet.getString("accountID");
                   
                    return accountID;
                } else {
                    System.err.println("Error: No bank accounts found in the database");
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving random account from database: " + e.getMessage());
            return null;
        }
    }

    private void initializeDatabase() {
        Connection connection = null;
        // Connect to the Database
        connection = DatabaseManager.connect(DATABASE_PATH);
        // Initialize Bank Database with Random BankAccounts
        DatabaseManager.initializeBankDatabase(connection, ID);

        DatabaseManager.closeConnection(connection);
    }
}
