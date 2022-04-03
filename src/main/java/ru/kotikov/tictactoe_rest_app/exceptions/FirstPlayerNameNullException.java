package ru.kotikov.tictactoe_rest_app.exceptions;

public class FirstPlayerNameNullException extends Exception {
    public FirstPlayerNameNullException(String message) {
        super(message);
    }
}
