package ru.kotikov.tictactoe_rest_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.kotikov.tictactoe_rest_app.exceptions.*;
import ru.kotikov.tictactoe_rest_app.model.GameHistory;
import ru.kotikov.tictactoe_rest_app.model.Player;
import ru.kotikov.tictactoe_rest_app.model.Step;
import ru.kotikov.tictactoe_rest_app.repository.GameHistoryRepo;
import ru.kotikov.tictactoe_rest_app.repository.PlayerRepo;
import ru.kotikov.tictactoe_rest_app.repository.StepRepo;
import ru.kotikov.tictactoe_rest_app.services.util.TicTacToeFields;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TicTacToeService implements GameService {

    @Autowired
    private GameHistoryRepo gameHistoryRepo;
    @Autowired
    private PlayerRepo playerRepo;
    @Autowired
    private StepRepo stepRepo;
    @Autowired
    private TicTacToeFields fields;

    // Запись имен игроков
    @Override
    public void setNamesForTwoPlayers(String nameX, String nameO) {

        Player temp = playerRepo.findByName(nameX);
        if (temp != null) {
            fields.setFirstPlayer(temp);
        } else {
            fields.setFirstPlayer(new Player(nameX, "X"));
            playerRepo.save(fields.getFirstPlayer());
        }
        temp = playerRepo.findByName(nameO);
        if (temp != null) {
            fields.setSecondPlayer(temp);
        } else {
            fields.setSecondPlayer(new Player(nameO, "O"));
            playerRepo.save(fields.getSecondPlayer());
        }
        fields.getGameHistory().setFirstPlayer(playerRepo.findByName(nameX));
        fields.getGameHistory().setSecondPlayer(playerRepo.findByName(nameO));
    }

    // получение игрока, который сейчас ходит
    @Override
    public Player getMovePlayer() {
        // Оповещение об очередности хода
        if (fields.getMovesCounter() % 2 == 1) {
            return fields.getFirstPlayer();
        } else {
            return fields.getSecondPlayer();
        }
    }

    // Отображение игрового поля в текущем состоянии
    @Override
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
    @Override
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
        if (x != 9) fields.setCurrentCoordinate(coordinate);
        if (x != 9 & (fields.getMovesCounter() % 2 == 1))
            fields.setCurrentPlayer(playerRepo.findByName(fields.getFirstPlayer().getName()));
        else if (x != 9 & (fields.getMovesCounter() % 2 == 0))
            fields.setCurrentPlayer(playerRepo.findByName(fields.getSecondPlayer().getName()));
        return new int[]{x, y};
    }

    // определяет свободно ли указанное поле для внесения символа игрока
    @Override
    public boolean freeField(int x, int y) {
        return Objects.equals(fields.getPLAYING_FIELD()[x][y], "|1|") || Objects.equals(fields.getPLAYING_FIELD()[x][y], "|2|")
                || Objects.equals(fields.getPLAYING_FIELD()[x][y], "|3|") || Objects.equals(fields.getPLAYING_FIELD()[x][y], "|4|")
                || Objects.equals(fields.getPLAYING_FIELD()[x][y], "|5|") || Objects.equals(fields.getPLAYING_FIELD()[x][y], "|6|")
                || Objects.equals(fields.getPLAYING_FIELD()[x][y], "|7|") || Objects.equals(fields.getPLAYING_FIELD()[x][y], "|8|")
                || Objects.equals(fields.getPLAYING_FIELD()[x][y], "|9|");
    }

    // отрисовка хода игрока на игровом поле
    @Override
    public void drawMove(int x, int y) {
        // В зависимости от счетчика ходов, определяется каким символом заполнять свободное поле
        if ((fields.getMovesCounter() % 2 == 1)) {
            fields.getPLAYING_FIELD()[x][y] = "|X|";
        } else {
            fields.getPLAYING_FIELD()[x][y] = "|O|";
        }
        Step step = new Step(fields.getCurrentCoordinate(), fields.getMovesCounter(), fields.getCurrentPlayer());
        fields.getSteps().add(step);
    }

    // подведение резальтатов матча
    @Override
    public String result(Model model) {
        // текст сообщения
        String message1, message2 = "Игра окончена! Общий счёт: \n", message3, message4;
        // определяем победителя
        if (!winnerSearch(fields.getPLAYING_FIELD())) {
            fields.getFirstPlayer().setNumberOfDraws(fields.getFirstPlayer().getNumberOfDraws() + 1);
            fields.getSecondPlayer().setNumberOfDraws(fields.getSecondPlayer().getNumberOfDraws() + 1);
            fields.setDraw(true);
            message1 = "Ничья! Не удивительно 8)\n";
            message3 = "У игрока - " + fields.getFirstPlayer().getName() + ": " + fields.getFirstPlayer().getNumberOfWins() + " побед, " + fields.getFirstPlayer().getNumberOfLoses() + " поражений, "
                    + fields.getFirstPlayer().getNumberOfDraws() + " игр сыграно в ничью; \n";
            message4 = "У игрока - " + fields.getSecondPlayer().getName() + ": "
                    + fields.getSecondPlayer().getNumberOfWins() + " побед, " + fields.getSecondPlayer().getNumberOfLoses() + " поражений, "
                    + fields.getSecondPlayer().getNumberOfDraws() + " игр сыграно в ничью.";
            fields.getGameHistory().setWinner(null);
        } else if (fields.getMovesCounter() % 2 == 0) {
            fields.getSecondPlayer().setNumberOfWins(fields.getSecondPlayer().getNumberOfWins() + 1);
            fields.getFirstPlayer().setNumberOfLoses(fields.getFirstPlayer().getNumberOfLoses() + 1);
            message1 = fields.getSecondPlayer().getName() + " (нолик) победил(-а)! :)\n";
            message3 = "У игрока - " + fields.getFirstPlayer().getName() + ": " + fields.getFirstPlayer().getNumberOfWins() + " побед, " + fields.getFirstPlayer().getNumberOfLoses() + " поражений, "
                    + fields.getFirstPlayer().getNumberOfDraws() + " игр сыграно в ничью; \n";
            message4 = "У игрока - " + fields.getSecondPlayer().getName() + ": "
                    + fields.getSecondPlayer().getNumberOfWins() + " побед, " + fields.getSecondPlayer().getNumberOfLoses() + " поражений, "
                    + fields.getSecondPlayer().getNumberOfDraws() + " игр сыграно в ничью.";
            fields.getGameHistory().setWinner(playerRepo.findByName(fields.getSecondPlayer().getName()));
        } else {
            fields.getFirstPlayer().setNumberOfWins(fields.getFirstPlayer().getNumberOfWins() + 1);
            fields.getSecondPlayer().setNumberOfLoses(fields.getSecondPlayer().getNumberOfLoses() + 1);
            message1 = fields.getFirstPlayer().getName() + " (крестик) победил(-а)! :)\n";
            message3 = "У игрока - " + fields.getFirstPlayer().getName() + ": " + fields.getFirstPlayer().getNumberOfWins() + " побед, " + fields.getFirstPlayer().getNumberOfLoses() + " поражений, "
                    + fields.getFirstPlayer().getNumberOfDraws() + " игр сыграно в ничью; \n";
            message4 = "У игрока - " + fields.getSecondPlayer().getName() + ": "
                    + fields.getSecondPlayer().getNumberOfWins() + " побед, " + fields.getSecondPlayer().getNumberOfLoses() + " поражений, "
                    + fields.getSecondPlayer().getNumberOfDraws() + " игр сыграно в ничью.";
            fields.getGameHistory().setWinner(playerRepo.findByName(fields.getFirstPlayer().getName()));
        }
        // сохраняем шаги игры в историю
//        gameHistory.setSteps(steps);
        // сохраняем игровое поле для отображения
        model.addAttribute("field", showPlayingField(fields.getPLAYING_FIELD()));
        // сохраняем сообщения в модели для вывода (разделил потому, что не знаю как в шаблоне разделить это на строки)
        // можно конечно было в массив превратить)
        model.addAttribute("message1", message1);
        model.addAttribute("message2", message2);
        model.addAttribute("message3", message3);
        model.addAttribute("message4", message4);

        playerRepo.saveAndFlush(fields.getFirstPlayer());
        playerRepo.saveAndFlush(fields.getSecondPlayer());
        return "result";
    }

    // обнуление всех необходимых для новой игры полей
    @Override
    public void clearAll() {
        refreshPlayingField(fields.getPLAYING_FIELD());
        fields.setMovesCounter((byte) 1);
        fields.setGameOver(false);
        fields.setGameOver(false);
        if (fields.getFirstPlayer() != null) {
            fields.setGameHistory(new GameHistory());
            fields.getGameHistory().setFirstPlayer(playerRepo.findByName(fields.getFirstPlayer().getName()));
            fields.getGameHistory().setSecondPlayer(playerRepo.findByName(fields.getSecondPlayer().getName()));
        } else fields.setGameHistory(new GameHistory());
        fields.getSteps().clear();
    }

    // проверяем введенные имена на валидность
    @Override
    public void validInputTwoNames(String nameX, String nameO, Model model)
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
    @Override
    public String getFieldViewTemplate(Model model) {
        model.addAttribute("field", showPlayingField(fields.getPLAYING_FIELD()));
        model.addAttribute("name", getMovePlayer().getName());
        getStringSymbol(model);
        return "field";
    }

    // определяем символ игрока в текстовом варианте для отображения в браузере
    @Override
    public void getStringSymbol(Model model) {
        if (getMovePlayer().equals(fields.getFirstPlayer())) {
            model.addAttribute("symbol", "крестики");
        } else model.addAttribute("symbol", "нолики");
    }

    // при неправильно указанных координатах выдает соответствующие сообщения
    @Override
    public void needRepeatField(String coordinate, Model model)
            throws CoordinatesNotValidException, FieldsCellOccupiedException {
        // вводим в модель данные об игроке и поле
        model.addAttribute("name", getMovePlayer().getName());
        getStringSymbol(model);
        model.addAttribute("field", showPlayingField(fields.getPLAYING_FIELD()));
        // проверяем валидность введенных координат
        if (coordinateValid(coordinate)[0] == 9) {
            throw new CoordinatesNotValidException("Ошибка! Введены неверные координаты! Введи нормальные свободные координаты " +
                    "(число от 1 до 9 включительно)");
        } else if (!freeField(coordinateValid(coordinate)[0],
                coordinateValid(coordinate)[1])) {
            throw new FieldsCellOccupiedException("Эта ячейка занята! Выбери другую!");
        }
    }

    @Override
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

    @Override
    public void refreshPlayingField(String[][] playingField) {
        int fieldCount = 1;
        for (int i = 0; i < playingField.length; i++) {
            for (int j = 0; j < playingField.length; j++) {
                playingField[i][j] = "|" + fieldCount + "|";
                fieldCount++;
            }
        }
    }

    @Override
    public void saveRating(Model model) {
        fields.getFirstPlayer().setRating((
                (fields.getFirstPlayer().getNumberOfWins() * 3)
                        - (fields.getFirstPlayer().getNumberOfLoses() * 2)
                        + (fields.getFirstPlayer().getNumberOfDraws())
        ));
        fields.getSecondPlayer().setRating((
                (fields.getSecondPlayer().getNumberOfWins() * 3)
                        - (fields.getSecondPlayer().getNumberOfLoses() * 2)
                        + (fields.getSecondPlayer().getNumberOfDraws())
        ));
        playerRepo.saveAndFlush(fields.getFirstPlayer());
        playerRepo.saveAndFlush(fields.getSecondPlayer());
        model.addAttribute("message", "Рейтинг успешно сохранен!");
        getRating(model);
    }

    @Override
    public void saveHistory() {
        fields.getGameHistory().setId();
        for (Step s : fields.getSteps()) {
            s.setGameHistory(fields.getGameHistory());
        }
//        fields.getGameHistory().setSteps(fields.getSteps());
        gameHistoryRepo.save(fields.getGameHistory());
        stepRepo.saveAll(fields.getSteps());
    }

    @Override
    public void getRating(Model model) {
        List<Player> rating = playerRepo.findAll().stream()
                .sorted((o1, o2) -> o2.getRating() - o1.getRating()).collect(Collectors.toList());
        if (rating.size() > 1) {
            model.addAttribute("rating", rating);
        }
    }

    @Override
    public void newPlayers() {
        fields.setFirstPlayer(new Player());
        fields.setSecondPlayer(new Player());
    }

    @Override
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

    @Override
    public void showGameHistory(String id, Model model) throws GameHistoryNotFoundException, InputIdIsNullStringException {
        if (id.trim().isEmpty()) {
            throw new InputIdIsNullStringException("Вы ввели пустую строку в поле для ввода id :(");
        } else if (gameHistoryRepo.findById(Long.parseLong(id)).isEmpty()) {
            throw new GameHistoryNotFoundException("История игры с таким id не найдена!");
        } else {
            GameHistory gameHistory = gameHistoryRepo.findById(Long.parseLong(id)).get();
            model.addAttribute("gameHistory", gameHistory);
            if (gameHistory.getWinner() == null) {
                model.addAttribute("draw", "Draw!");
            }
        }
    }
}

