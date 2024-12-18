package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import DAO.AccountDAO;
import DAO.MessageDAO;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public Javalin startAPI() {
        // Instantiate DAOs and Services
        AccountDAO accountDAO = new AccountDAO();
        MessageDAO messageDAO = new MessageDAO();

        accountService = new AccountService(accountDAO);
        messageService = new MessageService(messageDAO, accountDAO);

        Javalin app = Javalin.create();

        // User Registration Endpoint
        app.post("/register", this::handleRegister);

        // Login Endpoint
        app.post("/login", this::handleLogin);

        // Message Endpoint
        app.post("/messages", this::handleCreateMessage);

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

    private void handleLogin(Context ctx) {
        // Parse the incoming JSON into an Account object (without account_id)
        Account loginAttempt = ctx.bodyAsClass(Account.class);
    
        // Attempt login via the service
        Account loggedInAccount = accountService.login(loginAttempt);
    
        if (loggedInAccount != null) {
            // Login successful
            ctx.json(loggedInAccount); 
        } else {
            // Login failed
            ctx.status(401);
        }
    }

    private void handleCreateMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null) {
            ctx.json(createdMessage);
        } else {
            ctx.status(400);
        }
    }
    
}
