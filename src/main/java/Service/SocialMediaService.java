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

}
