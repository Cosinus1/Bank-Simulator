package main.java.com.Banksim.assets;

import main.java.com.Banksim.database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import main.java.com.Banksim.servers.InterbankNetwork;

public class Bank {
    private Map<String, BankAccount> bankAccounts; // Assuming the key is the account number

    public Bank() {
        this.bankAccounts = new HashMap<>();
        // Initialize bank accounts from the database
        initializeBankAccounts();
    }

    public boolean routeRequest(Card card) {
        // Redirect the transaction to GieCB (AcquisitionServer and AuthorizationServer)
        InterbankNetwork gieCB = InterbankNetwork.getInstance();
        return gieCB.routeRequest(card);
    }

    public boolean processTransaction(Card card) {
        // Simulate processing a transaction on the buyer's account
        // For simplicity, assume a fixed amount for the transaction
        double transactionAmount = 100.0;

        // Find the buyer's bank account based on the card information
        BankAccount buyerAccount = bankAccounts.get(card.getCardNumber());

        if (buyerAccount != null) {
            // Process the transaction on the buyer's account
            return buyerAccount.processTransaction(transactionAmount);
        } else {
            System.out.println("Error: Buyer's bank account not found.");
            return false;
        }
    }

    private void initializeBankAccounts() {
        // Initialize bank accounts from the database
        Connection connection = null;
        try {
            connection = DatabaseManager.connect();

            String query = "SELECT account_number, account_holder, balance FROM bank_accounts";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String accountNumber = resultSet.getString("account_number");
                String accountHolder = resultSet.getString("account_holder");
                double balance = resultSet.getDouble("balance");

                BankAccount account = new BankAccount(accountNumber, accountHolder, balance);
                bankAccounts.put(account.getAccountNumber(), account);
            }
        } catch (SQLException e) {
            System.err.println("Error initializing bank accounts: " + e.getMessage());
        } finally {
            DatabaseManager.closeConnection(connection);
        }
    }
}
