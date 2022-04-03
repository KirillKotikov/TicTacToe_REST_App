package ru.kotikov.tictactoe_rest_app.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.kotikov.tictactoe_rest_app.exceptions.*;
import ru.kotikov.tictactoe_rest_app.model.Player;
import ru.kotikov.tictactoe_rest_app.repository.GameHistoryRepo;
import ru.kotikov.tictactoe_rest_app.repository.PlayerRepo;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GameServiceTest {

    private final Model model = new ConcurrentModel();

    @Autowired
    GameService gameService;

    @MockBean
    private PlayerRepo playerRepo;

    @MockBean
    private GameHistoryRepo gameHistoryRepo;

    @Before
    public void clear() {
        gameService.clearAll();
    }

    // при внесении имен, они не должны быть пустыми и должны совпадать с введенными
    @Test
    public void namesShouldNotBeEmpty() {
        gameService.setNames("Kirill", "Alena");
        Assert.assertNotNull(gameService.getFirstPlayer().getName());
        Assert.assertNotNull(gameService.getSecondPlayer().getName());
        Assert.assertEquals("Kirill", gameService.getFirstPlayer().getName());
        Assert.assertEquals("Alena", gameService.getSecondPlayer().getName());
    }

    // при нечетном счетчике должен ходить крестик, при четном нолик
    @Test
    public void moveNameShouldBeFirstPlayerNameThenSecond() {
        gameService.setNames("Kirill", "Alena");
        Assert.assertEquals(gameService.getFirstPlayer().getName(), gameService.getMovePlayer().getName());
        gameService.setMovesCounter((byte) 2);
        Assert.assertEquals(gameService.getSecondPlayer().getName(), gameService.getMovePlayer().getName());
    }

    // должен возвращать игровое поле в одномерном массиве
    @Test
    public void playingFieldShouldBeConvert() {
        String[] expected = {"|1| |2| |3| ", "|4| |5| |6| ", "|7| |8| |9| "};
        Assert.assertArrayEquals(expected, gameService.showPlayingField(gameService.getPLAYING_FIELD()));
    }

    // координаты должны быть от 1 до 9
    @Test
    public void coordinatesShouldBeValid() {
        Assert.assertArrayEquals(new int[]{0, 0}, gameService.coordinateValid("1"));
        Assert.assertArrayEquals(new int[]{0, 1}, gameService.coordinateValid("2"));
        Assert.assertArrayEquals(new int[]{0, 2}, gameService.coordinateValid("3"));
        Assert.assertArrayEquals(new int[]{1, 0}, gameService.coordinateValid("4"));
        Assert.assertArrayEquals(new int[]{1, 1}, gameService.coordinateValid("5"));
        Assert.assertArrayEquals(new int[]{1, 2}, gameService.coordinateValid("6"));
        Assert.assertArrayEquals(new int[]{2, 0}, gameService.coordinateValid("7"));
        Assert.assertArrayEquals(new int[]{2, 1}, gameService.coordinateValid("8"));
        Assert.assertArrayEquals(new int[]{2, 2}, gameService.coordinateValid("9"));
        Assert.assertArrayEquals(new int[]{9, 9}, gameService.coordinateValid("0"));
        Assert.assertArrayEquals(new int[]{9, 9}, gameService.coordinateValid("t"));
    }

    // проверка свободно ли поле при пустом поле
    @Test
    public void fieldShouldBeFree() {
        Assert.assertTrue(gameService.freeField(0, 0));
        Assert.assertTrue(gameService.freeField(0, 1));
        Assert.assertTrue(gameService.freeField(0, 2));
        Assert.assertTrue(gameService.freeField(1, 0));
        Assert.assertTrue(gameService.freeField(1, 1));
        Assert.assertTrue(gameService.freeField(1, 2));
        Assert.assertTrue(gameService.freeField(2, 0));
        Assert.assertTrue(gameService.freeField(2, 1));
        Assert.assertTrue(gameService.freeField(2, 2));
    }

    // проверка занято ли поле
    @Test
    public void fieldShouldBeOccupied() {
        for (int i = 0; i < gameService.getPLAYING_FIELD().length; i++) {
            for (int j = 0; j < gameService.getPLAYING_FIELD().length; j++) {
                gameService.getPLAYING_FIELD()[i][j] = "|X|";
            }
        }
        Assert.assertFalse(gameService.freeField(0, 0));
        Assert.assertFalse(gameService.freeField(0, 1));
        Assert.assertFalse(gameService.freeField(0, 2));
        Assert.assertFalse(gameService.freeField(1, 0));
        Assert.assertFalse(gameService.freeField(1, 1));
        Assert.assertFalse(gameService.freeField(1, 2));
        Assert.assertFalse(gameService.freeField(2, 0));
        Assert.assertFalse(gameService.freeField(2, 1));
        Assert.assertFalse(gameService.freeField(2, 2));
    }

    // Проверка правильности внесения символа игрока
    @Test
    public void charShouldBeX() {
        gameService.drawMove(0, 0);
        Assert.assertEquals(gameService.getPLAYING_FIELD()[0][0], "|X|");
    }

    // Проверка правильности внесения символа игрока
    @Test
    public void charShouldBeO() {
        gameService.setMovesCounter((byte) 2);
        gameService.drawMove(0, 1);
        Assert.assertEquals(gameService.getPLAYING_FIELD()[0][1], "|O|");
    }

    @Test
    public void firstPlayerShouldBeWinner() {
        gameService.setNames("Kirill", "Alena");
        for (int i = 0; i < gameService.getPLAYING_FIELD().length; i++) {
            for (int j = 0; j < gameService.getPLAYING_FIELD().length; j++) {
                if (gameService.getMovesCounter() % 2 == 1) {
                    gameService.getPLAYING_FIELD()[i][j] = "|X|";
                } else gameService.getPLAYING_FIELD()[i][j] = "|O|";
                if (gameService.getMovesCounter() == 7) break;
                gameService.setMovesCounter((byte) (gameService.getMovesCounter() + 1));
            }
        }
        gameService.result(model);
        Assert.assertEquals(1, gameService.getFirstPlayer().getNumberOfWins());
        Assert.assertEquals(1, gameService.getSecondPlayer().getNumberOfLoses());
    }

    @Test
    public void secondPlayerShouldBeWinner() {
        gameService.setNames("Kirill", "Alena");
        gameService.getPLAYING_FIELD()[2][1] = "|X|";
        gameService.getPLAYING_FIELD()[0][0] = "|O|";
        gameService.getPLAYING_FIELD()[0][1] = "|X|";
        gameService.getPLAYING_FIELD()[1][0] = "|O|";
        gameService.getPLAYING_FIELD()[0][2] = "|X|";
        gameService.getPLAYING_FIELD()[2][0] = "|O|";
        gameService.setMovesCounter((byte) 6);
        gameService.result(model);
        Assert.assertEquals(1, gameService.getSecondPlayer().getNumberOfWins());
        Assert.assertEquals(1, gameService.getFirstPlayer().getNumberOfLoses());
    }

    @Test
    public void shouldBeDraw() {
        gameService.setNames("Kirill", "Alena");
        gameService.getPLAYING_FIELD()[0][0] = "|X|";//1
        gameService.getPLAYING_FIELD()[0][1] = "|O|";//2
        gameService.getPLAYING_FIELD()[0][2] = "|O|";//3
        gameService.getPLAYING_FIELD()[1][0] = "|O|";//4
        gameService.getPLAYING_FIELD()[1][1] = "|X|";//5
        gameService.getPLAYING_FIELD()[1][2] = "|X|";//6
        gameService.getPLAYING_FIELD()[2][0] = "|X|";//7
        gameService.getPLAYING_FIELD()[2][1] = "|X|";//8
        gameService.getPLAYING_FIELD()[2][2] = "|O|";//9
        gameService.setMovesCounter((byte) 9);
        gameService.result(model);
        Assert.assertEquals(1, gameService.getFirstPlayer().getNumberOfDraws());
        Assert.assertEquals(1, gameService.getSecondPlayer().getNumberOfDraws());
    }

    @Test
    public void allShouldBeClear() {
        for (int i = 0; i < gameService.getPLAYING_FIELD().length; i++) {
            for (int j = 0; j < gameService.getPLAYING_FIELD().length; j++) {
                gameService.getPLAYING_FIELD()[i][j] = "|X|";
            }
            gameService.setMovesCounter((byte) 9);
            gameService.setGameOver(true);
            gameService.setDraw(true);
        }
        gameService.clearAll();
        String[][] expectedPlayingField = new String[][]{
                {"|1|", "|2|", "|3|"},
                {"|4|", "|5|", "|6|"},
                {"|7|", "|8|", "|9|"}};
        Assert.assertArrayEquals(expectedPlayingField, gameService.getPLAYING_FIELD());
        Assert.assertEquals(1, gameService.getMovesCounter());
        Assert.assertFalse(gameService.isGameOver());
        Assert.assertFalse(gameService.isDraw());
    }

    // Проверка выпадения ошибки при внесении пустой строки в поле для ввода имени первого игрока
    @Test(expected = FirstPlayerNameNullException.class)
    public void firstPlayerShouldInsertNullStringInNameField()
            throws FirstPlayerNameNullException, PlayersNamesNullException, SecondPlayerNameNullException {
        gameService.validInputNames(" ", "Kirill", model);
    }

    // Проверка выпадения ошибки при внесении пустой строки в поле для ввода имени второго игрока
    @Test(expected = SecondPlayerNameNullException.class)
    public void secondPlayerShouldInsertNullStringInNameField()
            throws FirstPlayerNameNullException, PlayersNamesNullException, SecondPlayerNameNullException {
        gameService.validInputNames("Kirill", " ", model);
    }

    // Проверка выпадения ошибки при внесении пустой строки в поле для ввода имени обоих игроков
    @Test(expected = PlayersNamesNullException.class)
    public void PlayersShouldInsertNullStringInNameField()
            throws FirstPlayerNameNullException, PlayersNamesNullException, SecondPlayerNameNullException {
        gameService.validInputNames(" ", " ", model);
    }

    @Test(expected = CoordinatesNotValidException.class)
    public void needRepeatFieldShouldThrowCoordinatesNotValidException()
            throws CoordinatesNotValidException, FieldsCellOccupiedException {
        for (int i = 0; i < gameService.getPLAYING_FIELD().length; i++) {
            for (int j = 0; j < gameService.getPLAYING_FIELD().length; j++) {
                gameService.getPLAYING_FIELD()[i][j] = "|X|";
            }
        }
        gameService.needRepeatField("0", model);
        gameService.needRepeatField("10", model);
        gameService.needRepeatField("h", model);
    }

    @Test(expected = FieldsCellOccupiedException.class)
    public void needRepeatFieldShouldBeThrowFieldsCellOccupiedException()
            throws CoordinatesNotValidException, FieldsCellOccupiedException {
        for (int i = 0; i < gameService.getPLAYING_FIELD().length; i++) {
            for (int j = 0; j < gameService.getPLAYING_FIELD().length; j++) {
                gameService.getPLAYING_FIELD()[i][j] = "|X|";
            }
        }
        for (int j = 1; j < 10; j++) {
            gameService.needRepeatField(String.valueOf(j), model);
        }
    }

    @Test
    public void needRepeatFieldShouldNotThrowExceptions()
            throws CoordinatesNotValidException, FieldsCellOccupiedException {
        for (int j = 1; j < 10; j++) {
            gameService.needRepeatField(String.valueOf(j), model);
        }
    }

    // Проверка, что при ничье, не должно быть победителя
    @Test
    public void winnerSearchShouldBeFalseNoWinner() {
        String[][] playingField = new String[][]{
                {"|X|", "|O|", "|X|"},
                {"|O|", "|X|", "|O|"},
                {"|O|", "|X|", "|O|"}};

        Assert.assertFalse(gameService.winnerSearch(playingField));
    }

    // Проверяем при наличии победителя
    @Test
    public void winnerSearchShouldBeTrueWithWinnerX() {
        String[][] playingField = new String[][]{
                {"|X|", "|O|", "|X|"},
                {"|O|", "|X|", "|O|"},
                {"|X|", "|X|", "|O|"}};

        Assert.assertTrue(gameService.winnerSearch(playingField));
    }

    // Проверяем при наличии победителя
    @Test
    public void winnerSearchShouldBeTrueWithWinnerO() {
        String[][] playingField = new String[][]{
                {"|O|", "|X|", "|O|"},
                {"|X|", "|O|", "|X|"},
                {"|O|", "|X|", "|9|"}};

        Assert.assertTrue(gameService.winnerSearch(playingField));
    }

    // Тест на обновление/очиску игрового поля
    @Test
    public void playingFieldShouldBeNew() {
        String[][] playingField = new String[][]{
                {"|X|", "|O|", "|X|"},
                {"|O|", "|X|", "|O|"},
                {"|X|", "|X|", "|O|"}};

        String[][] newPlayingField = new String[][]{
                {"|1|", "|2|", "|3|"},
                {"|4|", "|5|", "|6|"},
                {"|7|", "|8|", "|9|"}};

        gameService.refreshPlayingField(playingField);

        Assert.assertArrayEquals(playingField, newPlayingField);
    }

    @Test
    public void saveRating() {
        gameService.setFirstPlayer(new Player("Kirill", 0, 2, 1));
        gameService.setSecondPlayer(new Player("Alena", 2, 0, 1));
        gameService.saveRating(model);
        Assert.assertEquals(-3, gameService.getFirstPlayer().getRating());
        Assert.assertEquals(7, gameService.getSecondPlayer().getRating());
        Mockito.verify(playerRepo, Mockito.times(1)).save(gameService.getFirstPlayer());
        Mockito.verify(playerRepo, Mockito.times(1)).save(gameService.getSecondPlayer());
        Assert.assertEquals("Рейтинг успешно сохранен!", model.getAttribute("message"));
        // проверка медота getRating, который запускается внутри saveRating
        Mockito.verify(playerRepo, Mockito.times(1)).findAll();
    }

    @Test
    public void saveHistory() {
        gameService.saveHistory();
        Mockito.verify(gameHistoryRepo, Mockito.times(1)).save(gameService.getGameHistory());
    }

    @Test
    public void newPlayersShouldBeWithNullNames() {
        gameService.setFirstPlayer(new Player("Kirill", 0, 2, 1));
        gameService.setSecondPlayer(new Player("Alena", 2, 0, 1));
        gameService.newPlayers();
        Assert.assertNull(gameService.getFirstPlayer().getName());
        Assert.assertNull(gameService.getSecondPlayer().getName());
    }

    @Test
    public void gameHistoryIsEmpty() {
        gameService.gameHistoryIsEmpty(model);
        Mockito.verify(gameHistoryRepo, Mockito.times(1)).findAll();
    }

    @Test(expected = GameHistoryNotFoundException.class)
    public void showGameHistoryShouldThrowGameHistoryNotFoundException()
            throws GameHistoryNotFoundException, InputIdIsNullStringException {
        gameService.showGameHistory("1", model);
    }

    @Test(expected = InputIdIsNullStringException.class)
    public void showGameHistoryShouldThrowInputIdIsNullStringException()
            throws GameHistoryNotFoundException, InputIdIsNullStringException {
            gameService.showGameHistory("   ", model);
    }
}
