package DAO;
import Util.ConnectionUtil; 
import java.sql.*;

import Model.*; 
import java.util.List; 
import java.util.ArrayList; 
public class SocialMediaDAO {
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
