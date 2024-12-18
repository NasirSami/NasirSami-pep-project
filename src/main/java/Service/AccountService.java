package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account register(Account account) {
        // Validate username is not blank
        if(account.getUsername() == null || account.getUsername().trim().isEmpty()){
            return null;
        }

        // Validate password length >= 4
        if(account.getPassword() == null || account.getPassword().length() < 4){
            return null;
        }

        // Check if username already exists
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if(existingAccount != null){
            return null;
        }

        // If all checks pass, insert the account into the DB
        Account insertedAccount = accountDAO.insertAccount(account);
        return insertedAccount;
    }
}
