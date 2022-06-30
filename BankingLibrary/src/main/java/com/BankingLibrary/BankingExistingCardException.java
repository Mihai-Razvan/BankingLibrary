package com.BankingLibrary;

public class BankingExistingCardException extends Throwable {

    private String message;

    public BankingExistingCardException(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
