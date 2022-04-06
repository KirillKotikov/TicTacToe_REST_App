package ru.kotikov.tictactoe_rest_app.services;

import org.springframework.ui.Model;
import ru.kotikov.tictactoe_rest_app.exceptions.*;
import ru.kotikov.tictactoe_rest_app.model.Player;

public interface GameService {

    // Запись имен двих игроков
    void setNamesForTwoPlayers(String nameX, String nameO);

    // получение игрока, который сейчас ходит
    Player getMovePlayer();

    // Отображение игрового поля в текущем состоянии
    String[] showPlayingField(String[][] playingField);

    // проверка введенной координаты на валидность
    int[] coordinateValid(String coordinate);

    // определяет свободно ли указанное поле для внесения символа игрока
    boolean freeField(int x, int y);

    // отрисовка хода игрока на игровом поле
    void drawMove(int x, int y);

    // подведение резальтатов матча
    String result(Model model);

    // обнуление всех необходимых для новой игры полей
    void clearAll();

    // проверяем введенные имена на валидность
    void validInputTwoNames(String nameX, String nameO, Model model)
            throws FirstPlayerNameNullException, PlayersNamesNullException, SecondPlayerNameNullException;

    // возвращает страницу с ходом и игровым полем с определенными параметрами
    String getFieldViewTemplate(Model model);

    // определяем символ игрока в текстовом варианте для отображения в браузере
    void getStringSymbol(Model model);

    // при неправильно указанных координатах выдает соответствующие сообщения
    void needRepeatField(String coordinate, Model model)
            throws CoordinatesNotValidException, FieldsCellOccupiedException;

    boolean winnerSearch(String[][] playingField);

    void refreshPlayingField(String[][] playingField);

    void saveRating(Model model);

    void saveHistory();

    void getRating(Model model);

    void newPlayers();

    void gameHistoryIsEmpty(Model model);

    void showGameHistory(String id, Model model) throws GameHistoryNotFoundException, InputIdIsNullStringException;
}
