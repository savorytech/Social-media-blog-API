package Service;

import DAO.SocialMediaDAO;
import Model.*; 
import java.util.List;
public class SocialMediaService {
    private SocialMediaDAO socialMediaDAO; 
    /**
     * No-arg constructor for a SocialMediaService to instantiate a plain SocialMediaDAO
     */
    public SocialMediaService(){
        socialMediaDAO = new SocialMediaDAO();
    }

    /**
     * Checks if a user is valid using DAO layer validation.
     *
     * @param user The {@code Account} object with username and password to validate.
     * @return Validated {@code Account} object, or {@code null} if invalid.
     */
    public Account validUser(Account user){
        return socialMediaDAO.validUser(user);
    }
    /**
     * Registers a new account if validation criteria are met.
     *
     * This method first checks if the provided {@code Account} object has a non-empty username
     * and a password of at least 4 characters. If the validation passes, it proceeds to register
     * the account using the DAO layer. If the validation fails, it returns {@code null}.
     *
     * @param user The {@code Account} object containing the username and password for the new account.
     * @return The registered {@code Account} object, or {@code null} if validation fails or registration is unsuccessful.
     */
    public Account registerAnAccount(Account user){
        if(user.getUsername() == null ||user.getUsername().replaceAll(" ", "") == "" || user.getPassword().length() < 4)
            return null;     
        return socialMediaDAO.registerAnAccount(user); 
    }
    /**
     * Creates a new message after validating the message text.
     *
     * Validates the message text to ensure it is not null, not solely whitespace,
     * and does not exceed 255 characters. If the message passes these checks,
     * it is passed to the DAO layer for creation. If it fails any check, returns {@code null}.
     *
     * @param m The {@code Message} object containing the message text and other details.
     * @return The newly created {@code Message} object, or {@code null} if validation fails.
     */
    public Message createMessage(Message m){
        if(m.getMessage_text() == null)
            return null;
        if(m.getMessage_text().length() > 255)
            return null; 
        if(m.getMessage_text().replaceAll(" ", "") == "")
            return null;
        return socialMediaDAO.createMessage(m); 
    }

    /**
     * Retrieves all messages from the DAO layer.
     * This method simply delegates the task to the {@code socialMediaDAO}.
     *
     * @return List of {@code Message} objects from the database
     */
    public List<Message> getAllMessages(){
        return socialMediaDAO.getAllMessages(); 
    }
    
    /**
     * Retrieves a message by its ID from the DAO layer.
     * Delegates the task of fetching the specific message to the {@code socialMediaDAO}.
     *
     * @param message_id The ID of the message to be retrieved.
     * @return Message object corresponding to the given ID, or null if no message is found.
     */
    public Message getMessageById(int message_id){
        return socialMediaDAO.getMessageById(message_id); 
    }

}
