package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import DAO.AccountDAO;
import DAO.MessageDAO;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

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

        // Message Endpoints
        app.post("/messages", this::handleCreateMessage);
        app.get("/messages", this::handleGetAllMessages);
        app.get("/messages/{message_id}", this::handleGetMessageById);
        app.delete("/messages/{message_id}", this::handleDeleteMessageById);
        app.patch("/messages/{message_id}", this::handleUpdateMessage);
        app.get("/accounts/{account_id}/messages", this::handleGetAllMessagesFromUser);

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
    
    private void handleGetAllMessages(Context ctx) {
        List<Message> allMessages = messageService.getAllMessages();
        ctx.json(allMessages);
    }

    private void handleGetMessageById(Context ctx) {
        String messageIdStr = ctx.pathParam("message_id");
        int messageId = Integer.parseInt(messageIdStr);
    
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.result("");
        }
    }

    private void handleDeleteMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);
    
        if (deletedMessage != null) {
            // Message existed and now is deleted
            ctx.json(deletedMessage);
        } else {
            // Message did not exist
            ctx.result(""); 
        }
    }
    
    private void handleUpdateMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        
        Message updatedRequest = ctx.bodyAsClass(Message.class);
        String newText = updatedRequest.getMessage_text();
    
        Message updatedMessage = messageService.updateMessageText(messageId, newText);
        if (updatedMessage != null) {
            ctx.json(updatedMessage); 
        } else {
            ctx.status(400);
        }
    }
    
    private void handleGetAllMessagesFromUser(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> userMessages = messageService.getAllMessagesFromUser(accountId);
        ctx.json(userMessages);
    }
    

}
