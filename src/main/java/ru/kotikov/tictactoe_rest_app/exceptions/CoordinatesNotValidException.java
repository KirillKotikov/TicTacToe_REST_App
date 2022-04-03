package ru.kotikov.tictactoe_rest_app.exceptions;

public class CoordinatesNotValidException extends Exception{
    public CoordinatesNotValidException(String message) {
        super(message);
    }
}
