package Controller;

import Model.Account;
import Service.AccountService;
import DAO.AccountDAO;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    private AccountService accountService;

    public Javalin startAPI() {
        // Instantiate DAOs and Services
        AccountDAO accountDAO = new AccountDAO();
        accountService = new AccountService(accountDAO);

        Javalin app = Javalin.create();

        // User Registration Endpoint
        app.post("/register", this::handleRegister);

        return app;
    }

    private void handleRegister(Context ctx) {
        // Parse the incoming JSON into an Account object
        Account account = ctx.bodyAsClass(Account.class);

        // Attempt registration via the service
        Account created = accountService.register(account);

        if (created != null) {
            // Registration successful
            ctx.json(created);
        } else {
            // Registration failed
            ctx.status(400);
        }
    }
}
