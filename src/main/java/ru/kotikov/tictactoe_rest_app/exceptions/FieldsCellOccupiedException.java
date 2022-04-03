package ru.kotikov.tictactoe_rest_app.exceptions;

public class FieldsCellOccupiedException extends Exception{
    public FieldsCellOccupiedException(String message) {
        super(message);
    }
}
