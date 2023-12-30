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

    /**
     * Deletes a message from the database by its ID and returns the deleted message.
     * First, it fetches the message using its ID to verify if it exists in the database.
     * If the message exists, it proceeds to delete it. If the deletion is successful, the method
     * returns the deleted message. If the message does not exist or the deletion fails, it returns null.
     *
     * @param message_id The ID of the message to be deleted.
     * @return The deleted Message object if the deletion is successful, otherwise null.
     */
    public Message deleteMessageById(int message_id){
        Message m = socialMediaDAO.getMessageById(message_id);
        if(m == null){
            return null; 
        }

        boolean passed = socialMediaDAO.deleteMessageById(message_id); 
        if(passed){
            return m; 
        }
        else{
            return null;
        }
    }

    /**
     * Updates a message by its ID in the database.
     * Validates the input message before updating. The message text should not exceed 255 characters
     * and should not be empty after trimming whitespace. If the validation fails, the method returns null.
     * If the validation passes, it calls the DAO layer to update the message in the database.
     * If the update is successful, it fetches and returns the updated message using the message ID.
     * If the update fails, it returns null.
     *
     * @param message The Message object containing the updated information.
     * @param message_id The ID of the message to be updated.
     * @return The updated Message object if the update is successful and validation passes, otherwise null.
     */
    public Message updateMessageTextById(Message message, int message_id){
        if(message.getMessage_text().length() > 255 || message.getMessage_text().trim().equals(""))
            return null;

        boolean result = socialMediaDAO.updateMessageTextById(message, message_id);  
        if(result){
            return socialMediaDAO.getMessageById(message_id);
        }
        else{
            return null;
        }

    }
    /**
     * Retrieves all messages for a specific user from the DAO.
     * 
     * @param account_id The ID of the user for whom messages are being retrieved.
     * @return A list of Message objects representing the user's messages.
     */
    public List<Message> getAllMessagesByUserId(int account_id){
        return socialMediaDAO.getAllMessagesByUserId(account_id); 
    }

}
