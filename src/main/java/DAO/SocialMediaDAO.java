package DAO;
import Util.ConnectionUtil; 
import java.sql.*;

import Model.*; 
import java.util.List; 
import java.util.ArrayList; 
public class SocialMediaDAO {
    
    /**
     * Fetches all messages for a given user ID from the database.
     * 
     * @param account_id The ID of the user whose messages are to be retrieved.
     * @return A list of Message objects representing the user's messages.
     */
    public List<Message> getAllMessagesByUserId(int account_id){
        List<Message> messageToReturn = new ArrayList<>(); 
        try(Connection conn = ConnectionUtil.getConnection()){
            PreparedStatement ps = conn.prepareStatement("select * from message where posted_by = ?");
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery(); 


            while(rs.next()){
                Message m = new Message(rs.getInt("message_id"), account_id, rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messageToReturn.add(m); 
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return messageToReturn; 
    }
    /**
     * Updates the message text of a specific message in the database by its ID.
     * Establishes a database connection and executes a SQL UPDATE statement to modify the message text.
     * The update is considered successful if exactly one record is affected.
     *
     * @param message The Message object containing the updated information. Only the message text is updated.
     * @param message_id The ID of the message to be updated.
     * @return true if the update was successful and affected one row, false otherwise.
     */
    public boolean updateMessageTextById(Message message, int message_id){
        try(Connection conn = ConnectionUtil.getConnection()){
            PreparedStatement ps = conn.prepareStatement("UPDATE message SET message_text = ? WHERE message_id = ?");
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message_id);
            int result = ps.executeUpdate(); 
            
            if(result == 1){
                return true; 
            }
        }catch(SQLException e){
            e.printStackTrace(); 
        }

        return false; 
    }
    /**
     * Deletes a message from the database by its ID.
     * Establishes a database connection and executes a SQL DELETE statement to remove the message
     * with the specified ID. This method does not verify if the message exists before deletion and 
     * does not return the deleted message data. It's designed to be used in conjunction with 
     * another method that performs the existence check.
     * 
     * In case of an SQLException, the stack trace is printed. This method returns a boolean indicating 
     * whether the deletion was successful (true) or not (false).
     *
     * @param message_id The ID of the message to be deleted.
     * @return boolean indicating success (true) or failure (false) of the deletion.
     */
    public boolean deleteMessageById(int message_id){
        try(Connection conn = ConnectionUtil.getConnection()){
            PreparedStatement ps = conn.prepareStatement("DELETE FROM message where message_id = ?");
            ps.setInt(1, message_id);
            int affected = ps.executeUpdate();
            if(affected > 0)
                return true; 
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false; 
    }

    /**
     * Retrieves a specific message by its ID from the database.
     * Executes a SQL query to find the message with the specified ID.
     * Converts the query result into a {@code Message} object if found.
     * In case of a SQL exception, the stack trace is printed.
     *
     * @param message_id The ID of the message to retrieve.
     * @return The {@code Message} object if found, otherwise null.
     */
    public Message getMessageById(int message_id){
        try(Connection conn = ConnectionUtil.getConnection()){
            PreparedStatement ps = conn.prepareStatement("select * from message where message_id = ? "); 
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery(); 

            while(rs.next()){
                Message m = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return m; 
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return null; 
    }


    /**
     * Fetches all messages from the database.
     * Executes a SQL query to retrieve all messages, converting each result into a {@code Message} object.
     * Adds these objects to a list, which is returned.
     * In case of a SQL exception, the error message is printed to the console.
     *
     * @return List of {@code Message} objects representing each message in the database
     */
    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>(); 
        try(Connection conn = ConnectionUtil.getConnection()){
            PreparedStatement ps = conn.prepareStatement("select * from message;");
            ResultSet rs = ps.executeQuery(); 
            while(rs.next()){
                Message m = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(m);
            }
        }catch(SQLException e ){
            System.out.println(e.getMessage());
        }

        return messages; 
    }
    
    /**
     * Authenticates a user by matching the username and password in the database.
     *
     * This method searches the 'account' table for an entry matching the provided username
     * and password. If a match is found, it creates and returns an {@code Account} object with
     * the user's details. If no match is found, returns {@code null}.
     *
     * @param user The {@code Account} object with the username and password to authenticate.
     * @return An {@code Account} object with user details if authentication is successful;
     *         {@code null} if authentication fails.
     */
    public Account validUser(Account user){
        try(Connection conn = ConnectionUtil.getConnection()){
            PreparedStatement ps = conn.prepareStatement("select * from account where username=? and password=?");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery(); 
            while(rs.next()){
                int id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password"); 
                return new Account(id, username, password); 
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return null; 
    }

    /**
     * Registers a new account and returns it with an auto-generated ID.
     *
     * @param user The {@code Account} object with 'username' and 'password' for registration.
     * @return The registered {@code Account} with its ID, or {@code null} if registration fails.
     */
    public Account registerAnAccount(Account user){
        try(Connection conn = ConnectionUtil.getConnection()){
            PreparedStatement ps = conn.prepareStatement("INSERT INTO account(username, password) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword()); 

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys(); 

            while(rs.next()){
                int id = rs.getInt(1); 

                return new Account(id, user.getUsername(), user.getPassword()); 
            }

        }catch(SQLException e){
            e.printStackTrace(); 
        }

        return null; 

    }
    
    /**
     * Inserts a new message into the database and returns it with a generated ID.
     *
     * @param m The {@code Message} object to be inserted, containing 'posted_by', 'message_text',
     *          and 'time_posted_epoch' fields. The ID field should not be set as it is auto-generated.
     * @return The {@code Message} object with the generated ID, or {@code null} if insertion fails.
     * @throws SQLException If an SQL error occurs during the messages creation. 
     */
    public Message createMessage(Message m){
        try(Connection conn = ConnectionUtil.getConnection()){
            PreparedStatement ps = conn.prepareStatement("INSERT INTO message(posted_by, message_text, time_posted_epoch) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS); 
            ps.setInt(1, m.getPosted_by()); 
            ps.setString(2, m.getMessage_text());
            ps.setLong(3, m.getTime_posted_epoch());

            ps.executeUpdate(); 
            ResultSet rs = ps.getGeneratedKeys(); 
            while(rs.next()){
                int id = rs.getInt(1); 
                System.out.println(id + m.toString()); 
                return new Message(id, m.getPosted_by(), m.getMessage_text(), m.getTime_posted_epoch());
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return null; 
    }
}
