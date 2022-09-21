package com.abhinay.walletsystem.exceptions;

public class InsufficientAccountBalanceException extends RuntimeException{
    public InsufficientAccountBalanceException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "Insufficient Account Balance Exception. Please deposit balance in account first and try again...";
    }
}
