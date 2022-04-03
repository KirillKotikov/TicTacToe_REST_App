package ru.kotikov.tictactoe_rest_app.exceptions;

public class PlayersNamesNullException extends Exception{
    public PlayersNamesNullException(String message) {
        super(message);
    }
}
