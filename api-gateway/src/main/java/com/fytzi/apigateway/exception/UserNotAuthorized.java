package com.fytzi.apigateway.exception;

public class UserNotAuthorized extends RuntimeException {


    public UserNotAuthorized(String messeage){
        super(messeage);
    }

}
