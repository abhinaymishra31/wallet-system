package com.abhinay.walletsystem.exceptions;

public class ReceiverDoesNotExistException extends RuntimeException{
    public ReceiverDoesNotExistException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "Receiver Does Not Exist Exception...";
    }
}
