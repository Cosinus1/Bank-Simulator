package Banksim.Back.servers;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Banksim.Back.assets.Bank;
import Banksim.Back.assets.Card;
import Banksim.Back.database.DatabaseManager;

public class InterbankNetwork {
    private static InterbankNetwork instance;
    private String DATABASE_PATH;
    private int CurrentCode;

    private InterbankNetwork() {
        // Singleton
        DATABASE_PATH = "src/main/resources/sql/database_giecb.db";
        CurrentCode = 001;
        initializeDatabase();
    }
    
    public static InterbankNetwork getInstance() {
        if (instance == null) {
            instance = new InterbankNetwork();
        }
        return instance;
    }

    public int routeRequest(Card card) {
        String buyerBankID = extractBankID(card);
        Bank buyerBank = findBuyerBank(buyerBankID);

        if (buyerBank != null) {
            return buyerBank.processBuyerTransaction(card);
        } else {
            System.out.println("Error: Buyer's bank not found.");
            return 1;
        }
    }

    private String extractBankID(Card card) {
        return card.getCardNumber().substring(0, 3);
    }

    private Bank findBuyerBank(String bankID) {
        System.out.println("retrieving bank : " + bankID + " from GieCB Database...");
        Bank bank = retrieveBankFromDatabase(bankID);

        if (bank == null) {
            System.out.println("Bank not found in the GIECB Database");
        }

        return bank;
    }

    private Bank retrieveBankFromDatabase(String bankID) {
        // Check if the bank is already in the cache
        Bank cachedBank = Bank.getBank(bankID);
        if (cachedBank != null) {
            return cachedBank;
        }
    
        try (Connection connection = DatabaseManager.connect(DATABASE_PATH)) {
            String query = "SELECT * FROM Banks WHERE bankID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, bankID);
    
                ResultSet resultSet = preparedStatement.executeQuery();
    
                if (resultSet.next()) {
                    // Retrieve bank details
                    String retrievedBankID = resultSet.getString("bankID");
    
                    Bank retrievedBank = Bank.getBank(retrievedBankID);
    
                    return retrievedBank;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving bank from database: " + e.getMessage());
        }
    
        return null;
    }
    

    public Bank createBank(String bankName) {

        String bankID = String.format("%03d", CurrentCode);        
        String Path = generatePath(bankName);

        try (Connection connection = DatabaseManager.connect(DATABASE_PATH)) {
            String query = "INSERT INTO Banks (bankID, bankName) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                //Replace with data
                preparedStatement.setString(1, bankID);
                preparedStatement.setString(2, bankName);

                //DISPLAY BANKS
                    int rowsAffected = preparedStatement.executeUpdate();
                    String selectQuery = "SELECT bankID from Banks";
                    PreparedStatement preparedStatement2 = connection.prepareStatement(selectQuery);
                    ResultSet resultSet = preparedStatement2.executeQuery();

                    System.out.println("\n\n----------------BankIDs in the Database: --------------\n");
                    System.out.println("-------------------------------------------------------");
                    while (resultSet.next()) {
                        String ID = resultSet.getString("bankID");

                        System.out.println("|                     BANK ID : " + ID + "                   |");
                    }
                    System.out.println("-------------------------------------------------------");

                if (rowsAffected > 0) {
                    //increment CurrentCode
                    CurrentCode++;
                    //Close connection
                    DatabaseManager.closeConnection(connection);
                    //Create the new Bank
                    return Bank.newBank(Path, bankID, bankName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating bank in the database: " + e.getMessage());
        }

        return null;
    }

    public String generatePath(String Name){
        String generatedPath = "src/main/resources/sql/database_" + Name + ".db";
        System.out.println("Generated Path for " + Name + " : " + generatedPath);
        return generatedPath;
    }

    public void initializeDatabase(){
        Connection connection = null;
        //Connect to the Database
        connection = DatabaseManager.connect(DATABASE_PATH);
        // Initialize Bank Database with Random BankAccounts
        DatabaseManager.initializeGIECBDatabase(connection);

        DatabaseManager.closeConnection(connection);
    }
}
