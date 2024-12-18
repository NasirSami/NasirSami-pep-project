package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO) {
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public Message createMessage(Message message) {
        // Validate message_text not blank
        if(message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
            return null;
        }

        // Validate message_text under 255 chars
        if(message.getMessage_text().length() > 255) {
            return null;
        }

        // Validate posted_by is a real user
        Account existingAccount = accountDAO.getAccountById(message.getPosted_by());
        if(existingAccount == null) {
            return null;
        }


        // If all validations pass, insert message into DB
        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    public Message deleteMessageById(int message_id) {
        // Get the message first
        Message message = messageDAO.getMessageById(message_id);
        if (message == null) {
            // No message with that id
            return null;
        }
    
        boolean deleted = messageDAO.deleteMessageById(message_id);
        if (deleted) {
            return message;
        } else {
            return null;
        }
    }
    
    public Message updateMessageText(int message_id, String newText) {
        // Validate newText
        if(newText == null || newText.trim().isEmpty() || newText.length() > 255) {
            return null;
        }
    
        // Check if message exists
        Message existingMessage = messageDAO.getMessageById(message_id);
        if (existingMessage == null) {
            // No message with this id
            return null;
        }
    
        // Attempt update
        boolean updated = messageDAO.updateMessageText(message_id, newText);
        if (updated) {
            // Retrieve and return the updated message
            return messageDAO.getMessageById(message_id);
        } else {
            return null;
        }
    }

    public List<Message> getAllMessagesFromUser(int account_id) {
        Account user = accountDAO.getAccountById(account_id);
        if (user == null) {
            // If user doesn't exist, return empty list
            return new ArrayList<>();
        }
        return messageDAO.getAllMessagesFromUser(account_id);
    }
    
    
}
