package com.nextrow.login_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nextrow.login_api.configuration.Constants;
import com.nextrow.login_api.services.LoginAPIServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;
import java.util.Map;

@RestController
public class LoginAPIController {

    @Autowired
    private LoginAPIServices loginAPIServices;

    public final String SECRET_KEY= Constants.SECRET_KEY;
    public static ObjectMapper objectMapper=new ObjectMapper();


    @GetMapping("/homePage")
    public String homePage(){
        return "hello";
    }

    @GetMapping("/signup")
    public ObjectNode getRegister(@RequestBody Map<String,String> userDetails){

        String username=userDetails.get("username");
        String password=userDetails.get("password");

        return objectMapper.convertValue(loginAPIServices.signUp(username,password,SECRET_KEY), ObjectNode.class);

    }

    @GetMapping("/login")
    public ObjectNode getToken(@RequestBody Map<String,String> userDetails) throws ParseException {

        String username=userDetails.get("username");
        String password=userDetails.get("password");

        return objectMapper.convertValue(loginAPIServices.authenticateUser(username,password), ObjectNode.class);
    }
}