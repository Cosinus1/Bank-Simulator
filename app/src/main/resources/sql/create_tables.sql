-- Table creation for BankAccounts
CREATE TABLE IF NOT EXISTS BankAccounts (
    accountNumber VARCHAR(20) PRIMARY KEY,
    accountHolderName VARCHAR(50),
    balance DECIMAL(10, 2)
);

-- Table creation for Banks
CREATE TABLE IF NOT EXISTS Banks (
    bankCode VARCHAR(3) PRIMARY KEY,
    bankName VARCHAR(50)
);
