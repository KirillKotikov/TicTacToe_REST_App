package ru.kotikov.tictactoe_rest_app.exceptions;

public class GameHistoryNotFoundException extends Exception{
    public GameHistoryNotFoundException(String message) {
        super(message);
    }
}
