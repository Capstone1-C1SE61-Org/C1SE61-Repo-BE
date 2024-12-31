package com.example.systemp3l.error;

public class NotFoundById extends Exception {
    public NotFoundById(String error){
        super(error);
    }
}