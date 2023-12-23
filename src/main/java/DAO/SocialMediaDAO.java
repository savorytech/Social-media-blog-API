package DAO;
import Util.ConnectionUtil; 
import java.sql.*;

import Model.*; 
public class SocialMediaDAO {
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
