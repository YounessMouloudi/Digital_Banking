package com.projects.digital_bank_backend.exceptions;

public class BalanceNotSuffisantException extends Exception {
    public BalanceNotSuffisantException(String message) {
        super(message);
    }
}
