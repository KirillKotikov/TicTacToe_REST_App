package ru.kotikov.tictactoe_rest_app.services.util;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.kotikov.tictactoe_rest_app.model.GameHistory;
import ru.kotikov.tictactoe_rest_app.model.Player;
import ru.kotikov.tictactoe_rest_app.model.Step;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class TicTacToeFields {

    // Массив игрового поля
    private final String[][] PLAYING_FIELD = new String[][]{
            {"|1|", "|2|", "|3|"},
            {"|4|", "|5|", "|6|"},
            {"|7|", "|8|", "|9|"}};
    // Счетчик ходов
    private byte movesCounter = 1;
    // Определяет игра продолжается или окончена (есть победитель или ничья)
    private boolean gameOver = false;
    //Первый игрок - х
    private Player firstPlayer;
    //Второй игрок - о
    private Player secondPlayer;
    // запись истории игры
    private GameHistory gameHistory;
    // флаг игры в ничью
    private boolean draw = false;
    // координата для записи истории игры
    private String currentCoordinate;
    // игрок для записи в историю ходов
    private Player currentPlayer;
    // список ходов
    private List<Step> steps = new ArrayList<>();
}
