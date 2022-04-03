package ru.kotikov.tictactoe_rest_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kotikov.tictactoe_rest_app.exceptions.*;
import ru.kotikov.tictactoe_rest_app.services.GameService;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    // Отображает страницу с вводом имен игроков
    @GetMapping("/gameplay")
    public String startGame() {
        gameService.clearAll();
        gameService.newPlayers();
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
            gameService.validInputNames(nameX, nameO, model);
            // сохраняем имена
            gameService.setNames(nameX, nameO);
            // выдаем ответ из шаблона /gameplay/field
            return gameService.getFieldViewTemplate(model);
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
            gameService.needRepeatField(coordinate, model);
        } catch (CoordinatesNotValidException | FieldsCellOccupiedException e) {
            model.addAttribute("message", e.getMessage());
            return "field";
        }
        // внесение хода на поле
        gameService.drawMove(gameService.coordinateValid(coordinate)[0],
                gameService.coordinateValid(coordinate)[1]);
        // ищем победителя или ничью
        if (gameService.getMovesCounter()> 4 && gameService.getMovesCounter() < 9) {
            gameService.setGameOver(gameService.winnerSearch(gameService.getPLAYING_FIELD()));
        } else if (gameService.getMovesCounter() > 8) gameService.setGameOver(true);
        // Выводим результаты если игра окончена
        if (gameService.isGameOver()) {
            return gameService.result(model);
        } else { // продолжаем игру если она не окончена
            gameService.setMovesCounter((byte) (gameService.getMovesCounter() + 1));
            return gameService.getFieldViewTemplate(model);
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
            gameService.saveHistory();
        }
        // если хочет начать новую игру
        if (newGame.equalsIgnoreCase("Да")) {
            // очищаем все поля игры
            gameService.clearAll();
            model.addAttribute("name", gameService.getMovePlayer().getName());
            model.addAttribute("field", gameService.showPlayingField(gameService.getPLAYING_FIELD()));
            gameService.getStringSymbol(model);
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
            gameService.saveRating(model);
        }
        gameService.getRating(model);
        model.addAttribute("continue", "Можно начать новую игру с внесением новых имен ;)");
        gameService.gameHistoryIsEmpty(model);
        return "/goodBye";
    }

    @GetMapping("/gameplay/final-result")
    public String showRating(@RequestParam(name = "id", required = false) String id, Model model) {
        gameService.gameHistoryIsEmpty(model);
        model.addAttribute("continue", "Можно начать новую игру с внесением новых имен ;)");
        gameService.getRating(model);
        try {
            gameService.showGameHistory(id, model);
        } catch (GameHistoryNotFoundException | InputIdIsNullStringException e) {
            model.addAttribute("gameHistoryNotFound", e.getMessage());
        }
        return "goodBye";
    }
}


