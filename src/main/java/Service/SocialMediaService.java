package Service;

import DAO.SocialMediaDAO;
import Model.*; 
public class SocialMediaService {
    private SocialMediaDAO socialMediaDAO; 
    public SocialMediaService(){
        socialMediaDAO = new SocialMediaDAO();
    }

    public Account validUser(Account user){
        return socialMediaDAO.validUser(user);
    }

    public Account registerAnAccount(Account user){
        if(user.getUsername() == null ||user.getUsername().replaceAll(" ", "") == "" || user.getPassword().length() < 4)
            return null;     
        return socialMediaDAO.registerAnAccount(user); 
    }

}
