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
        app.post("/messages", this::createMessage);
        
        return app;
    }
    /**
     * Handles the HTTP request to create a new message.
     *
     * Extracts a {@code Message} object from the request body and attempts to create a new message
     * using the service layer. If the message creation is successful, sends a 200 status code with
     * the message details in JSON format. If the creation fails, sends a 400 status code.
     *
     * @param ctx The Javalin context object for handling web requests and responses.
     */
    private void createMessage(Context ctx){
        Message m = ctx.bodyAsClass(Message.class); 
        Message r = socialMediaService.createMessage(m); 
        if(r != null){
            ctx.json(r).status(200); 
        }
        else{
            ctx.status(400); 
        }
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


    /**
     * Handles the HTTP request to register a new account.
     *
     * Parses an {@code Account} object from the request body and attempts to register it 
     * using the service layer. If the registration is successful, responds with a 200 status 
     * code and the account details in JSON format. If registration fails, responds with a 400 status code.
     *
     * @param ctx The Javalin context object for handling web requests and responses.
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

    /**
     * Handles the HTTP request for user authentication.
     *
     * Extracts an {@code Account} object from the request body, representing the user attempting to authenticate.
     * The method then uses the service layer to validate the user. If authentication is successful,
     * responds with a 200 status code and the authenticated user's details in JSON format. If authentication
     * fails, responds with a 401 (Unauthorized) status code.
     *
     * @param ctx The Javalin context object for web request and response handling.
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