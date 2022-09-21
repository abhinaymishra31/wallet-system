package com.abhinay.walletsystem.exceptions;

public class SelfWalletSendException extends RuntimeException{
    public SelfWalletSendException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "Self Wallet Send Exception...";
    }
}
