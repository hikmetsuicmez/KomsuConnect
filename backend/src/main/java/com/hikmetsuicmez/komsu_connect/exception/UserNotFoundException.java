package com.hikmetsuicmez.komsu_connect.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(){

    }

    public UserNotFoundException(String message){
        super(message);
    }
}
