package com.fytzi.productservice.exception;



public class InsufficientStock extends RuntimeException {

    public InsufficientStock(String messeage){
        super(messeage);
    }
}
