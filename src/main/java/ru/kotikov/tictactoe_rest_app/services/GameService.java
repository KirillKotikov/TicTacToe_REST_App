package ru.kotikov.tictactoe_rest_app.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.kotikov.tictactoe_rest_app.exceptions.*;
import ru.kotikov.tictactoe_rest_app.model.GameHistory;
import ru.kotikov.tictactoe_rest_app.model.Player;
import ru.kotikov.tictactoe_rest_app.repository.GameHistoryRepo;
import ru.kotikov.tictactoe_rest_app.repository.PlayerRepo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Data
public class GameService {
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
    private Player firstPlayer = new Player();
    //Второй игрок - о
    private Player secondPlayer = new Player();
    // запись истории игры
    private GameHistory gameHistory = new GameHistory();
    // флаг игры в ничью
    private boolean draw = false;
    // Список ходов
    private StringBuilder steps = new StringBuilder();

    @Autowired
    private GameHistoryRepo gameHistoryRepo;

    @Autowired
    private PlayerRepo playerRepo;

    // Запись имен игроков
    public void setNames(String nameX, String nameO) {
        firstPlayer.setName(nameX);
        secondPlayer.setName(nameO);
        gameHistory.setFirstPlayer(nameX + " - первый игрок (ходил крестиками)");
        gameHistory.setSecondPlayer(nameO + " - второй игрок (ходил ноликами)");
    }

    // получение игрока, который сейчас ходит
    public Player getMovePlayer() {
        // Оповещение об очередности хода
        if (movesCounter % 2 == 1) {
            return firstPlayer;
        } else {
            return secondPlayer;
        }
    }

    // Отображение игрового поля в текущем состоянии
    public String[] showPlayingField(String[][] playingField) {
        StringBuilder temp = new StringBuilder();
        for (String[] charsX : playingField) {
            for (String charsY : charsX) {
                temp.append(charsY).append(" ");
            }
            temp.append("\n");
        }
        return temp.toString().split("\n");
    }

    // проверка введенной координаты на валидность
    public int[] coordinateValid(String coordinate) {
        // Переменные для координат хода на поле
        int x = 9, y = 9;
        // Проверка введенных координат на соответствие валидным
        switch (coordinate) {
            case "1" -> {
                x = 0;
                y = 0;
            }
            case "2" -> {
                x = 0;
                y = 1;
            }
            case "3" -> {
                x = 0;
                y = 2;
            }
            case "4" -> {
                x = 1;
                y = 0;
            }
            case "5" -> {
                x = 1;
                y = 1;
            }
            case "6" -> {
                x = 1;
                y = 2;
            }
            case "7" -> {
                x = 2;
                y = 0;
            }
            case "8" -> {
                x = 2;
                y = 1;
            }
            case "9" -> {
                x = 2;
                y = 2;
            }
        }
        return new int[]{x, y};
    }

    // определяет свободно ли указанное поле для внесения символа игрока
    public boolean freeField(int x, int y) {
        return Objects.equals(PLAYING_FIELD[x][y], "|1|") || Objects.equals(PLAYING_FIELD[x][y], "|2|")
                || Objects.equals(PLAYING_FIELD[x][y], "|3|") || Objects.equals(PLAYING_FIELD[x][y], "|4|")
                || Objects.equals(PLAYING_FIELD[x][y], "|5|") || Objects.equals(PLAYING_FIELD[x][y], "|6|")
                || Objects.equals(PLAYING_FIELD[x][y], "|7|") || Objects.equals(PLAYING_FIELD[x][y], "|8|")
                || Objects.equals(PLAYING_FIELD[x][y], "|9|");
    }

    // отрисовка хода игрока на игровом поле
    public void drawMove(int x, int y) {
        // В зависимости от счетчика ходов, определяется каким символом заполнять свободное поле
        if ((movesCounter % 2 == 1)) {
            PLAYING_FIELD[x][y] = "|X|";
        } else {
            PLAYING_FIELD[x][y] = "|O|";
        }
        steps.append("Xод №").append(movesCounter).append(":\n");
        for (String[] charsX : PLAYING_FIELD) {
            for (String charsY : charsX) {
                steps.append(charsY).append(" ");
            }
            steps.append("\n");
        }
        steps.append("\n");
    }

    // подведение резальтатов матча
    public String result(Model model) {
        // текст сообщения
        String message1, message2, message3, message4;
        // определяем победителя
        if (!winnerSearch(PLAYING_FIELD)) {
            firstPlayer.setNumberOfDraws(firstPlayer.getNumberOfDraws() + 1);
            secondPlayer.setNumberOfDraws(secondPlayer.getNumberOfDraws() + 1);
            draw = true;
            message1 = "Ничья! Не удивительно 8)\n";
            message2 = "Игра окончена! Общий счёт: \n";
            message3 = "У игрока - " + firstPlayer.getName() + ": " + firstPlayer.getNumberOfWins() + " побед, " + firstPlayer.getNumberOfLoses() + " поражений, "
                    + firstPlayer.getNumberOfDraws() + " игр сыграно в ничью; \n";
            message4 = "У игрока - " + secondPlayer.getName() + ": "
                    + secondPlayer.getNumberOfWins() + " побед, " + secondPlayer.getNumberOfLoses() + " поражений, "
                    + secondPlayer.getNumberOfDraws() + " игр сыграно в ничью.";
            gameHistory.setGameResult("Ничья!");
        } else if (movesCounter % 2 == 0) {
            secondPlayer.setNumberOfWins(secondPlayer.getNumberOfWins() + 1);
            firstPlayer.setNumberOfLoses(firstPlayer.getNumberOfLoses() + 1);
            message1 = secondPlayer.getName() + " (нолик) победил(-а)! :)\n";
            message2 = "Игра окончена! Общий счёт: \n";
            message3 = "У игрока - " + firstPlayer.getName() + ": " + firstPlayer.getNumberOfWins() + " побед, " + firstPlayer.getNumberOfLoses() + " поражений, "
                    + firstPlayer.getNumberOfDraws() + " игр сыграно в ничью; \n";
            message4 = "У игрока - " + secondPlayer.getName() + ": "
                    + secondPlayer.getNumberOfWins() + " побед, " + secondPlayer.getNumberOfLoses() + " поражений, "
                    + secondPlayer.getNumberOfDraws() + " игр сыграно в ничью.";
            gameHistory.setGameResult(secondPlayer.getName() + " (ходил(-a) крестиками) - победитель!");
        } else {
            firstPlayer.setNumberOfWins(firstPlayer.getNumberOfWins() + 1);
            secondPlayer.setNumberOfLoses(secondPlayer.getNumberOfLoses() + 1);
            message1 = firstPlayer.getName() + " (крестик) победил(-а)! :)\n";
            message2 = "Игра окончена! Общий счёт: \n";
            message3 = "У игрока - " + firstPlayer.getName() + ": " + firstPlayer.getNumberOfWins() + " побед, " + firstPlayer.getNumberOfLoses() + " поражений, "
                    + firstPlayer.getNumberOfDraws() + " игр сыграно в ничью; \n";
            message4 = "У игрока - " + secondPlayer.getName() + ": "
                    + secondPlayer.getNumberOfWins() + " побед, " + secondPlayer.getNumberOfLoses() + " поражений, "
                    + secondPlayer.getNumberOfDraws() + " игр сыграно в ничью.";
            gameHistory.setGameResult(firstPlayer.getName() + " (ходил(-a) ноликами) - победитель!");
        }
        // сохраняем шаги игры в историю
        gameHistory.setSteps(steps.toString());
        // сохраняем игровое поле для отображения
        model.addAttribute("field", showPlayingField(PLAYING_FIELD));
        // сохраняем сообщения в модели для вывода (разделил потому, что не знаю как в шаблоне разделить это на строки)
        // можно конечно было в массив превратить)
        model.addAttribute("message1", message1);
        model.addAttribute("message2", message2);
        model.addAttribute("message3", message3);
        model.addAttribute("message4", message4);
        return "result";
    }

    // обнуление всех необходимых для новой игры полей
    public void clearAll() {
        steps = new StringBuilder();
        refreshPlayingField(PLAYING_FIELD);
        movesCounter = 1;
        gameOver = false;
        draw = false;
        gameHistory = new GameHistory(firstPlayer.getName() + " - первый игрок (ходил крестиками)",
                secondPlayer.getName() + " - второй игрок (ходил ноликами)");
    }

    // проверяем введенные имена на валидность
    public void validInputNames(String nameX, String nameO, Model model)
            throws FirstPlayerNameNullException, PlayersNamesNullException, SecondPlayerNameNullException {
        if (nameO.trim().isEmpty() && nameX.trim().isEmpty()) {
            throw new PlayersNamesNullException("Ты ввёл пустые строки в поля для имени первого и второго игрока :(");
        }
        if (nameX.trim().isEmpty()) {
            model.addAttribute("nameO", nameO);
            throw new FirstPlayerNameNullException("Ты ввёл пустую строку в поле для имени первого игрока :(");
        }
        if (nameO.trim().isEmpty()) {
            model.addAttribute("nameX", nameX);
            throw new SecondPlayerNameNullException("Ты ввёл пустую строку в поле для имени первого игрока :(");
        }
    }

    // возвращает страницу с ходом и игровым полем с определенными параметрами
    public String getFieldViewTemplate(Model model) {
        model.addAttribute("field", showPlayingField(PLAYING_FIELD));
        model.addAttribute("name", getMovePlayer().getName());
        getStringSymbol(model);
        return "field";
    }

    // определяем символ игрока в текстовом варианте для отображения в браузере
    public void getStringSymbol(Model model) {
        if (getMovePlayer().equals(firstPlayer)) {
            model.addAttribute("symbol", "крестики");
        } else model.addAttribute("symbol", "нолики");
    }

    // при неправильно указанных координатах выдает соответствующие сообщения
    public void needRepeatField(String coordinate, Model model)
            throws CoordinatesNotValidException, FieldsCellOccupiedException {
        // вводим в модель данные об игроке и поле
        model.addAttribute("name", getMovePlayer().getName());
        getStringSymbol(model);
        model.addAttribute("field", showPlayingField(PLAYING_FIELD));
        // проверяем валидность введенных координат
        if (coordinateValid(coordinate)[0] == 9) {
            throw new CoordinatesNotValidException("Ошибка! Введены неверные координаты! Введи нормальные свободные координаты " +
                    "(число от 1 до 9 включительно)");
        } else if (!freeField(coordinateValid(coordinate)[0],
                coordinateValid(coordinate)[1])) {
            throw new FieldsCellOccupiedException("Эта ячейка занята! Выбери другую!");
        }
    }

    public boolean winnerSearch(String[][] playingField) {
        boolean haveWinner = (playingField[0][0].equals(playingField[0][1])) && (playingField[0][1].equals(playingField[0][2]));

        if ((playingField[1][0].equals(playingField[1][1])) && (playingField[1][1].equals(playingField[1][2]))) {
            haveWinner = true;
        }
        if ((playingField[2][0].equals(playingField[2][1])) && (playingField[2][1].equals(playingField[2][2]))) {
            haveWinner = true;
        }

        if ((playingField[0][0].equals(playingField[1][0])) && (playingField[1][0].equals(playingField[2][0]))) {
            haveWinner = true;
        }
        if ((playingField[0][1].equals(playingField[1][1])) && (playingField[1][1].equals(playingField[2][1]))) {
            haveWinner = true;
        }
        if ((playingField[0][2].equals(playingField[1][2])) && (playingField[1][2].equals(playingField[2][2]))) {
            haveWinner = true;
        }

        if ((playingField[0][0].equals(playingField[1][1])) && (playingField[1][1].equals(playingField[2][2]))) {
            haveWinner = true;
        }
        if ((playingField[0][2].equals(playingField[1][1])) && (playingField[1][1].equals(playingField[2][0]))) {
            haveWinner = true;
        }
        return haveWinner;
    }

    public void refreshPlayingField(String[][] playingField) {
        int fieldCount = 1;
        for (int i = 0; i < playingField.length; i++) {
            for (int j = 0; j < playingField.length; j++) {
                playingField[i][j] = "|" + fieldCount + "|";
                fieldCount++;
            }
        }
    }

    public void saveRating(Model model) {
        firstPlayer.setRating((
                (firstPlayer.getNumberOfWins() * 3)
                        - (firstPlayer.getNumberOfLoses() * 2)
                        + (firstPlayer.getNumberOfDraws())
        ));
        secondPlayer.setRating((
                (secondPlayer.getNumberOfWins() * 3)
                        - (secondPlayer.getNumberOfLoses() * 2)
                        + (secondPlayer.getNumberOfDraws())
        ));
        playerRepo.save(firstPlayer);
        playerRepo.save(secondPlayer);
        model.addAttribute("message", "Рейтинг успешно сохранен!");
        getRating(model);
    }

    public void saveHistory() {
        gameHistoryRepo.save(gameHistory);
    }

    public void getRating(Model model) {
        List<Player> rating = playerRepo.findAll().stream()
                .sorted((o1, o2) -> o2.getRating() - o1.getRating()).collect(Collectors.toList());
        if (rating.size() > 1) {
            model.addAttribute("rating", rating);
        }
    }

    public void newPlayers() {
        firstPlayer = new Player();
        secondPlayer = new Player();
    }

    public void gameHistoryIsEmpty(Model model) {
        List<GameHistory> gameHistories = gameHistoryRepo.findAll();
        if (gameHistories.size() < 1) {
            model.addAttribute("gameHistoryIsEmpty", "В базе данных нет историй игр для просмотра");
        } else {
            model.addAttribute(
                    "gameHistoryNotEmpty",
                    "Возможен просмотр историй игр с id от 1 по " + (gameHistories.size()));
        }
    }
    public void showGameHistory(String id, Model model) throws GameHistoryNotFoundException, InputIdIsNullStringException {
        if (id.trim().isEmpty()) {
            throw new InputIdIsNullStringException("Вы ввели пустую строку в поле для ввода id :(");
        } else if (gameHistoryRepo.findById(Long.parseLong(id)).isEmpty()) {
            throw new GameHistoryNotFoundException("История игры с таким id не найдена!");
        } else {
            GameHistory gameHistory = gameHistoryRepo.findById(Long.parseLong(id)).get();
            model.addAttribute("gameHistory", gameHistory);
            String[] steps = gameHistory.getSteps().split("\n");
            model.addAttribute("steps", steps);
        }
    }
}

