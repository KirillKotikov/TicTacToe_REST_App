package ru.kotikov.tictactoe_rest_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kotikov.tictactoe_rest_app.exceptions.*;
import ru.kotikov.tictactoe_rest_app.services.GameService;
import ru.kotikov.tictactoe_rest_app.services.util.TicTacToeFields;

@Controller
public class GameController {

    @Autowired
    private GameService ticTacToeService;

    @Autowired
    private TicTacToeFields fields;

    // Отображает страницу с вводом имен игроков
    @GetMapping("/gameplay")
    public String startGame() {
        ticTacToeService.clearAll();
        ticTacToeService.newPlayers();
        return "start";
    }

    // Проверяет валидность введенных имен и переносит на страницу для хода
    @PostMapping("/gameplay")
    public String registration(
            @RequestParam(name = "nameX") String nameX,
            @RequestParam(name = "nameO") String nameO,
            Model model
    ) {
        // проверяем имена на валидность
        try {
            ticTacToeService.validInputTwoNames(nameX, nameO, model);
            // сохраняем имена
            ticTacToeService.setNamesForTwoPlayers(nameX, nameO);
            // выдаем ответ из шаблона /gameplay/field
            return ticTacToeService.getFieldViewTemplate(model);
        } catch (FirstPlayerNameNullException e) {
            model.addAttribute("nameXNull", e.getMessage());
            return "start";
        } catch (PlayersNamesNullException e) {
            model.addAttribute("nameXNull", e.getMessage());
            model.addAttribute("nameONull", e.getMessage());
            return "start";
        } catch (SecondPlayerNameNullException e) {
            model.addAttribute("nameONull", e.getMessage());
            return "start";
        }
    }

    // Обновляет страницу с полем и ходом игрока
    @PostMapping("/gameplay/field")
    public String move(
            @RequestParam(name = "coordinate") String coordinate,
            Model model
    ) {
        // проверяем не занято ли поле или введеные неверные координаты
        try {
            ticTacToeService.needRepeatField(coordinate, model);
        } catch (CoordinatesNotValidException | FieldsCellOccupiedException e) {
            model.addAttribute("message", e.getMessage());
            return "field";
        }
        // внесение хода на поле
        ticTacToeService.drawMove(ticTacToeService.coordinateValid(coordinate)[0],
                ticTacToeService.coordinateValid(coordinate)[1]);
        // ищем победителя или ничью
        if (fields.getMovesCounter()> 4 && fields.getMovesCounter() < 9) {
            fields.setGameOver(ticTacToeService.winnerSearch(fields.getPLAYING_FIELD()));
        } else if (fields.getMovesCounter() > 8) fields.setGameOver(true);
        // Выводим результаты если игра окончена
        if (fields.isGameOver()) {
            return ticTacToeService.result(model);
        } else { // продолжаем игру если она не окончена
            fields.setMovesCounter((byte) (fields.getMovesCounter() + 1));
            return ticTacToeService.getFieldViewTemplate(model);
        }
    }

    // получаем ответы игрока по поводу сохранения истории игры и начала новой
    @PostMapping("/gameplay/result")
    public String result(
            @RequestParam(name = "saveH") String saveH,
            @RequestParam(name = "newGame") String newGame, Model model) {
        // если игрок решил сохранить игру
        if (saveH.equalsIgnoreCase("Да")) {
            // Сохраняем историю игры в базу данных
            ticTacToeService.saveHistory();
        }
        // если хочет начать новую игру
        if (newGame.equalsIgnoreCase("Да")) {
            // очищаем все поля игры
            ticTacToeService.clearAll();
            model.addAttribute("name", ticTacToeService.getMovePlayer().getName());
            model.addAttribute("field", ticTacToeService.showPlayingField(fields.getPLAYING_FIELD()));
            ticTacToeService.getStringSymbol(model);
            return "/field";
        } else // закрытие игры
            model.addAttribute("save", "История игры успешно сохранена в базу данных!");
        model.addAttribute("message", "Спасибо за игру! Заходите поиграть еще ;)");
        return "/finalResult";
    }

    // Решаем вопрос сохранения рейтинга и заканчиваем игру
    @PostMapping("/gameplay/final-result")
    public String finalResult(@RequestParam(name = "saveR") String saveR, Model model) {
        if (saveR.equalsIgnoreCase("Да")) {
            ticTacToeService.saveRating(model);
        }
        ticTacToeService.getRating(model);
        model.addAttribute("continue", "Можно начать новую игру с внесением новых имен ;)");
        ticTacToeService.gameHistoryIsEmpty(model);
        return "/goodBye";
    }

    @GetMapping("/gameplay/final-result")
    public String showRating(@RequestParam(name = "id", required = false) String id, Model model) {
        ticTacToeService.gameHistoryIsEmpty(model);
        model.addAttribute("continue", "Можно начать новую игру с внесением новых имен ;)");
        ticTacToeService.getRating(model);
        try {
            ticTacToeService.showGameHistory(id, model);
        } catch (GameHistoryNotFoundException | InputIdIsNullStringException e) {
            model.addAttribute("gameHistoryNotFound", e.getMessage());
        }
        return "goodBye";
    }
}


