package com.nextrow.login_api.services;

import com.nextrow.login_api.utilities.MongoUtilities;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class LoginAPIServices {

    @Autowired
    private MongoUtilities mongoUtilities;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    public Map<String,String> signUp(String username,String password,String SECRET_KEY){

        String encodedPassword = passwordEncoder.encode(password);

        Map<String,String> response=new LinkedHashMap<>();

        Document userdata=new Document();
        userdata.put("username",username);
        userdata.put("password",encodedPassword);

        try{
            mongoUtilities.registerUser(userdata);
            response.put("status","registered successfully");
            return response;
        }

        catch (Exception exception){
            response.put("status","registration unsuccessful. User already exists.");
            return response;
        }
    }

    public Map<String,String> authenticateUser(String username,String password){

        // query db to get user

        Query query=new Query();
        query.addCriteria(Criteria.where("username").is(username));

        Document userDatabaseData=mongoUtilities.getUser(query);

        Map<String,String> response=new LinkedHashMap<>();

        //checking whether the password and token are valid or not
        if (userDatabaseData!=null){

            response.put("user-status","exists");

            String encodedPassword = userDatabaseData.get("password").toString();

            // validate password
            if (passwordEncoder.matches(password,encodedPassword)){

                response.put("user-authentication","success");

                    // else generate new jwt token and update details in db
                    String generatedToken= jwtService.generateJWTToken(username);

                    response.put("JWT Token",generatedToken);

                    return response;
                }
            response.put("user-authentication","failed");
            response.put("errorMessage","invalid password. Please try again!");
            return response;
            }
        response.put("user-authentication","failed");
        response.put("errorMessage","no user found. Please create an account!");
        return response;
    }
}