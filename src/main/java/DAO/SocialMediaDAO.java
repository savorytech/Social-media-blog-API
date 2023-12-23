package DAO;
import Util.ConnectionUtil; 
import java.sql.*;

import Model.Account; 
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
}
