package Controller;

import Service.SocialMediaService;
import Util.ConnectionUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    SocialMediaService socialMediaService;
    public SocialMediaController(){
        socialMediaService = new SocialMediaService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerAnAccount); 
        app.post("/login", this::validUser);
        
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    /* todo 
     * This is the function that will make an account when one does not already exist
     */
    private void registerAnAccount(Context ctx){
        Account registeringUser = ctx.bodyAsClass(Account.class); 
        Account user = socialMediaService.registerAnAccount(registeringUser); 
        if(user != null){
            ctx.json(user).status(200); 
        }
        else{
            ctx.status(400); 
        }
    }

    /*
     * this function will be used for loging a person in
     */
    private void validUser(Context ctx){
        Account attemptedUser = ctx.bodyAsClass(Account.class);
        System.out.println(attemptedUser.getUsername() + " " + attemptedUser.getPassword()); 
        Account user = socialMediaService.validUser(attemptedUser);
        if(user != null){
            ctx.json(user).status(200); 
        }
        else{
            ctx.status(401); 
        }

    }
    


}