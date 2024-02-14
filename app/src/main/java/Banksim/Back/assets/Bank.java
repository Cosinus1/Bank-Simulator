package Banksim.Back.assets;

import Banksim.Back.database.DatabaseManager;
import Banksim.Back.servers.InterbankNetwork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Bank {
    private static final Map<String, Bank> bankCache = new HashMap<>();

    private String DATABASE_PATH;
    private String ID;
    private String Name;

    private ArrayList<Log> Logs;
    

    private Bank(String databasePath, String ID) {
        this.ID = ID;
        this.DATABASE_PATH = databasePath;
        this.Logs = new ArrayList<>();
        // Initialize bank accounts from the database
        initializeDatabase();
    }

    public static Bank newBank(String DATABASE_PATH, String ID, String Name) {
        // Check if the bank is already in the cache
        if (bankCache.containsKey(ID)) {
            System.out.println("Error: This Bank already exists");
            return bankCache.get(ID);
        }
        // If not in the cache, create a new instance, cache it, and return it
        Bank newBank = new Bank(DATABASE_PATH, ID);
        newBank.setName(Name);
        bankCache.put(ID, newBank);
        return newBank;
    }
//=======================GETTERS==============================

    public static Bank getBank(String ID) {
        // Check if the bank is already in the cache
        return bankCache.get(ID);
    }

    public String getID(){
        return ID;
    }

    public String getName(){
        return this.Name;
    }

    public String getDatabasePath(){
        return DATABASE_PATH;
    }

    public ArrayList<Log> getLogs(){
        return Logs;
    }

    public BankAccount getBankAccount(String cardNumber){
        BankAccount bankAccount = BankAccount.getAccount(cardNumber);
         // Find the buyer's bank account based on the card information
         try (Connection connection = DatabaseManager.connect(DATABASE_PATH)) {
            String query = "SELECT * FROM BankAccounts WHERE cardNumber = ?";
    
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, cardNumber);
    
                ResultSet resultSet = preparedStatement.executeQuery();
    
                if (resultSet.next()) {
                    bankAccount.setAccountHolderName(resultSet.getString("AccountHolderName"));
                    bankAccount.setBalance(resultSet.getDouble("balance"));
                    
                } else {
                    System.err.println("Error: Retrieving informations of the bank account from the database");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving bank account from database: " + e.getMessage());
        }
    
        return bankAccount;
    }

//===========================SETTERS===================================

    public void setName(String Name){
        this.Name = Name;
    }

    public void setDatabasePath(String path){
        this.DATABASE_PATH = path;
    }

    public int routeRequest(Card card, String AccountID) {
        //Add log
        Log log1 = new Log("Payment Request from Terminal", card.getCardNumber());
        Logs.add(log1);
        // Redirect the transaction to GieCB (AcquisitionServer and AuthorizationServer)
        InterbankNetwork gieCB = InterbankNetwork.getInstance();
        int authorizationResult = gieCB.routeRequest(card, AccountID);
        Log log2 = new Log("Request transfer into " + AccountID, card.getCardNumber());
        Logs.add(log2);
        if(authorizationResult == 0){
            return processReceiverTransaction(card, AccountID);
        }else return authorizationResult;
    }

    public int processBuyerTransaction(Card card, String AccountID) {
        // Simulate processing a transaction on the buyer's account
        //add log
        Log log = new Log("Request transfer from GieCB", card.getCardNumber());
        Logs.add(log);
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
                                getBankAccount(card.getCardNumber()).addPayment(-amount, AccountID);
                                return 0;
                            } else {
                                System.err.println("Error updating balance in the database");
                                return 4;
                            }
                        }
                    } else {
                        System.out.println("Transaction failed: Insufficient balance");
                        return 3;
                    }
                } else {
                    System.err.println("Error: Retrieving bank account from the database");
                    return 2;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving bank account from database: " + e.getMessage());
        }
    
        return 2;
    }
    private int processReceiverTransaction(Card buyerCard, String accountID) {
        //add log
        Log log = new Log("transfer into " + accountID, buyerCard.getCardNumber());
        Logs.add(log);
        // Simulate processing a transaction on the buyer's account
        int amount = buyerCard.getTransaction();
    
        try (Connection connection = DatabaseManager.connect(DATABASE_PATH)) {
            // Find the receiver's bank account based on the card information
            String Query = "SELECT * FROM BankAccounts WHERE accountID = ?";
            try (PreparedStatement buyerStatement = connection.prepareStatement(Query)) {
                buyerStatement.setString(1, accountID);
    
                ResultSet resultSet = buyerStatement.executeQuery();
    
                if (resultSet.next()) {
                    int Balance = resultSet.getInt("balance");
                    String receiverCard = resultSet.getString("cardNumber");

                        // Update the receiver's balance in the database
                        int newBalance = Balance + amount;
                        String updateQuery = "UPDATE BankAccounts SET balance = ? WHERE accountID = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setDouble(1, newBalance);
                            updateStatement.setString(2, accountID);
        
                            System.out.println("Transaction processed successfully for Receiver. New balance: " + newBalance);
                            getBankAccount(receiverCard).addPayment(amount, buyerCard.getCardNumber());;
                            return 0;
                        }catch (SQLException e) {
                            System.err.println("Error Updating the Balance: " + e.getMessage());
                            return 6;
                        }
                } else {
                    System.err.println("Error: Retrieving receiver's bank account from the database");
                    return 5;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error processing transaction: " + e.getMessage());
            return 5;
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