package com.fytzi.orderservice.exception;

public class InsufficeintQuantityException extends  RuntimeException {
    public InsufficeintQuantityException(String messegae){
        super(messegae);
    }
}
