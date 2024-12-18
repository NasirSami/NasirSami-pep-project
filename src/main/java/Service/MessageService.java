package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

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
}
