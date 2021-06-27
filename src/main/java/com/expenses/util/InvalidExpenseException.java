package com.expenses.util;

public class InvalidExpenseException extends RuntimeException{

    public InvalidExpenseException(String message) {
        super("Wystąpił bląd: " + message);
    }


}
