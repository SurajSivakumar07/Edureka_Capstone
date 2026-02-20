package com.fytzi.orderservice.exception;

public class InvalidProductIdException extends RuntimeException {

    public InvalidProductIdException(String messeage){

        super(messeage);
    }
}
