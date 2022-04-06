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
import ru.kotikov.tictactoe_rest_app.services.util.TicTacToeFields;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GameServiceTest {

    private final Model model = new ConcurrentModel();

    @Autowired
    private TicTacToeService ticTacToeService;

    @Autowired
    private TicTacToeFields fields;

    @MockBean
    private PlayerRepo playerRepo;

    @MockBean
    private GameHistoryRepo gameHistoryRepo;

    @Before
    public void clear() {
        ticTacToeService.clearAll();
    }

    // при внесении имен, они не должны быть пустыми и должны совпадать с введенными
    @Test
    public void namesShouldNotBeEmpty() {
        ticTacToeService.setNamesForTwoPlayers("Kirill", "Alena");
        Assert.assertNotNull(fields.getFirstPlayer().getName());
        Assert.assertNotNull(fields.getSecondPlayer().getName());
        Assert.assertEquals("Kirill", fields.getFirstPlayer().getName());
        Assert.assertEquals("Alena", fields.getSecondPlayer().getName());
    }

    // при нечетном счетчике должен ходить крестик, при четном нолик
    @Test
    public void moveNameShouldBeFirstPlayerNameThenSecond() {
        ticTacToeService.setNamesForTwoPlayers("Kirill", "Alena");
        Assert.assertEquals(fields.getFirstPlayer().getName(), ticTacToeService.getMovePlayer().getName());
        fields.setMovesCounter((byte) 2);
        Assert.assertEquals(fields.getSecondPlayer().getName(), ticTacToeService.getMovePlayer().getName());
    }

    // должен возвращать игровое поле в одномерном массиве
    @Test
    public void playingFieldShouldBeConvert() {
        String[] expected = {"|1| |2| |3| ", "|4| |5| |6| ", "|7| |8| |9| "};
        Assert.assertArrayEquals(expected, ticTacToeService.showPlayingField(fields.getPLAYING_FIELD()));
    }

    // координаты должны быть от 1 до 9
    @Test
    public void coordinatesShouldBeValid() {
        Assert.assertArrayEquals(new int[]{0, 0}, ticTacToeService.coordinateValid("1"));
        Assert.assertArrayEquals(new int[]{0, 1}, ticTacToeService.coordinateValid("2"));
        Assert.assertArrayEquals(new int[]{0, 2}, ticTacToeService.coordinateValid("3"));
        Assert.assertArrayEquals(new int[]{1, 0}, ticTacToeService.coordinateValid("4"));
        Assert.assertArrayEquals(new int[]{1, 1}, ticTacToeService.coordinateValid("5"));
        Assert.assertArrayEquals(new int[]{1, 2}, ticTacToeService.coordinateValid("6"));
        Assert.assertArrayEquals(new int[]{2, 0}, ticTacToeService.coordinateValid("7"));
        Assert.assertArrayEquals(new int[]{2, 1}, ticTacToeService.coordinateValid("8"));
        Assert.assertArrayEquals(new int[]{2, 2}, ticTacToeService.coordinateValid("9"));
        Assert.assertArrayEquals(new int[]{9, 9}, ticTacToeService.coordinateValid("0"));
        Assert.assertArrayEquals(new int[]{9, 9}, ticTacToeService.coordinateValid("t"));
    }

    // проверка свободно ли поле при пустом поле
    @Test
    public void fieldShouldBeFree() {
        Assert.assertTrue(ticTacToeService.freeField(0, 0));
        Assert.assertTrue(ticTacToeService.freeField(0, 1));
        Assert.assertTrue(ticTacToeService.freeField(0, 2));
        Assert.assertTrue(ticTacToeService.freeField(1, 0));
        Assert.assertTrue(ticTacToeService.freeField(1, 1));
        Assert.assertTrue(ticTacToeService.freeField(1, 2));
        Assert.assertTrue(ticTacToeService.freeField(2, 0));
        Assert.assertTrue(ticTacToeService.freeField(2, 1));
        Assert.assertTrue(ticTacToeService.freeField(2, 2));
    }

    // проверка занято ли поле
    @Test
    public void fieldShouldBeOccupied() {
        for (int i = 0; i < fields.getPLAYING_FIELD().length; i++) {
            for (int j = 0; j < fields.getPLAYING_FIELD().length; j++) {
                fields.getPLAYING_FIELD()[i][j] = "|X|";
            }
        }
        Assert.assertFalse(ticTacToeService.freeField(0, 0));
        Assert.assertFalse(ticTacToeService.freeField(0, 1));
        Assert.assertFalse(ticTacToeService.freeField(0, 2));
        Assert.assertFalse(ticTacToeService.freeField(1, 0));
        Assert.assertFalse(ticTacToeService.freeField(1, 1));
        Assert.assertFalse(ticTacToeService.freeField(1, 2));
        Assert.assertFalse(ticTacToeService.freeField(2, 0));
        Assert.assertFalse(ticTacToeService.freeField(2, 1));
        Assert.assertFalse(ticTacToeService.freeField(2, 2));
    }

    // Проверка правильности внесения символа игрока
    @Test
    public void charShouldBeX() {
        ticTacToeService.drawMove(0, 0);
        Assert.assertEquals(fields.getPLAYING_FIELD()[0][0], "|X|");
    }

    // Проверка правильности внесения символа игрока
    @Test
    public void charShouldBeO() {
        fields.setMovesCounter((byte) 2);
        ticTacToeService.drawMove(0, 1);
        Assert.assertEquals(fields.getPLAYING_FIELD()[0][1], "|O|");
    }

    @Test
    public void firstPlayerShouldBeWinner() {
        ticTacToeService.setNamesForTwoPlayers("Kirill", "Alena");
        for (int i = 0; i < fields.getPLAYING_FIELD().length; i++) {
            for (int j = 0; j < fields.getPLAYING_FIELD().length; j++) {
                if (fields.getMovesCounter() % 2 == 1) {
                    fields.getPLAYING_FIELD()[i][j] = "|X|";
                } else fields.getPLAYING_FIELD()[i][j] = "|O|";
                if (fields.getMovesCounter() == 7) break;
                fields.setMovesCounter((byte) (fields.getMovesCounter() + 1));
            }
        }
        ticTacToeService.result(model);
        Assert.assertEquals(1, fields.getFirstPlayer().getNumberOfWins());
        Assert.assertEquals(1, fields.getSecondPlayer().getNumberOfLoses());
    }

    @Test
    public void secondPlayerShouldBeWinner() {
        ticTacToeService.setNamesForTwoPlayers("Kirill", "Alena");
        fields.getPLAYING_FIELD()[2][1] = "|X|";
        fields.getPLAYING_FIELD()[0][0] = "|O|";
        fields.getPLAYING_FIELD()[0][1] = "|X|";
        fields.getPLAYING_FIELD()[1][0] = "|O|";
        fields.getPLAYING_FIELD()[0][2] = "|X|";
        fields.getPLAYING_FIELD()[2][0] = "|O|";
        fields.setMovesCounter((byte) 6);
        ticTacToeService.result(model);
        Assert.assertEquals(1, fields.getSecondPlayer().getNumberOfWins());
        Assert.assertEquals(1, fields.getFirstPlayer().getNumberOfLoses());
    }

    @Test
    public void shouldBeDraw() {
        ticTacToeService.setNamesForTwoPlayers("Kirill", "Alena");
        fields.getPLAYING_FIELD()[0][0] = "|X|";//1
        fields.getPLAYING_FIELD()[0][1] = "|O|";//2
        fields.getPLAYING_FIELD()[0][2] = "|O|";//3
        fields.getPLAYING_FIELD()[1][0] = "|O|";//4
        fields.getPLAYING_FIELD()[1][1] = "|X|";//5
        fields.getPLAYING_FIELD()[1][2] = "|X|";//6
        fields.getPLAYING_FIELD()[2][0] = "|X|";//7
        fields.getPLAYING_FIELD()[2][1] = "|X|";//8
        fields.getPLAYING_FIELD()[2][2] = "|O|";//9
        fields.setMovesCounter((byte) 9);
        ticTacToeService.result(model);
        Assert.assertEquals(1, fields.getFirstPlayer().getNumberOfDraws());
        Assert.assertEquals(1, fields.getSecondPlayer().getNumberOfDraws());
    }

    @Test
    public void allShouldBeClear() {
        for (int i = 0; i < fields.getPLAYING_FIELD().length; i++) {
            for (int j = 0; j < fields.getPLAYING_FIELD().length; j++) {
                fields.getPLAYING_FIELD()[i][j] = "|X|";
            }
            fields.setMovesCounter((byte) 9);
            fields.setGameOver(true);
            fields.setDraw(true);
        }
        ticTacToeService.clearAll();
        String[][] expectedPlayingField = new String[][]{
                {"|1|", "|2|", "|3|"},
                {"|4|", "|5|", "|6|"},
                {"|7|", "|8|", "|9|"}};
        Assert.assertArrayEquals(expectedPlayingField, fields.getPLAYING_FIELD());
        Assert.assertEquals(1, fields.getMovesCounter());
        Assert.assertFalse(fields.isGameOver());
        Assert.assertFalse(fields.isDraw());
    }

    // Проверка выпадения ошибки при внесении пустой строки в поле для ввода имени первого игрока
    @Test(expected = FirstPlayerNameNullException.class)
    public void firstPlayerShouldInsertNullStringInNameField()
            throws FirstPlayerNameNullException, PlayersNamesNullException, SecondPlayerNameNullException {
        ticTacToeService.validInputTwoNames(" ", "Kirill", model);
    }

    // Проверка выпадения ошибки при внесении пустой строки в поле для ввода имени второго игрока
    @Test(expected = SecondPlayerNameNullException.class)
    public void secondPlayerShouldInsertNullStringInNameField()
            throws FirstPlayerNameNullException, PlayersNamesNullException, SecondPlayerNameNullException {
        ticTacToeService.validInputTwoNames("Kirill", " ", model);
    }

    // Проверка выпадения ошибки при внесении пустой строки в поле для ввода имени обоих игроков
    @Test(expected = PlayersNamesNullException.class)
    public void PlayersShouldInsertNullStringInNameField()
            throws FirstPlayerNameNullException, PlayersNamesNullException, SecondPlayerNameNullException {
        ticTacToeService.validInputTwoNames(" ", " ", model);
    }

    @Test(expected = CoordinatesNotValidException.class)
    public void needRepeatFieldShouldThrowCoordinatesNotValidException()
            throws CoordinatesNotValidException, FieldsCellOccupiedException {
        for (int i = 0; i < fields.getPLAYING_FIELD().length; i++) {
            for (int j = 0; j < fields.getPLAYING_FIELD().length; j++) {
                fields.getPLAYING_FIELD()[i][j] = "|X|";
            }
        }
        ticTacToeService.needRepeatField("0", model);
        ticTacToeService.needRepeatField("10", model);
        ticTacToeService.needRepeatField("h", model);
    }

    @Test(expected = FieldsCellOccupiedException.class)
    public void needRepeatFieldShouldBeThrowFieldsCellOccupiedException()
            throws CoordinatesNotValidException, FieldsCellOccupiedException {
        for (int i = 0; i < fields.getPLAYING_FIELD().length; i++) {
            for (int j = 0; j < fields.getPLAYING_FIELD().length; j++) {
                fields.getPLAYING_FIELD()[i][j] = "|X|";
            }
        }
        for (int j = 1; j < 10; j++) {
            ticTacToeService.needRepeatField(String.valueOf(j), model);
        }
    }

    @Test
    public void needRepeatFieldShouldNotThrowExceptions()
            throws CoordinatesNotValidException, FieldsCellOccupiedException {
        for (int j = 1; j < 10; j++) {
            ticTacToeService.needRepeatField(String.valueOf(j), model);
        }
    }

    // Проверка, что при ничье, не должно быть победителя
    @Test
    public void winnerSearchShouldBeFalseNoWinner() {
        String[][] playingField = new String[][]{
                {"|X|", "|O|", "|X|"},
                {"|O|", "|X|", "|O|"},
                {"|O|", "|X|", "|O|"}};

        Assert.assertFalse(ticTacToeService.winnerSearch(playingField));
    }

    // Проверяем при наличии победителя
    @Test
    public void winnerSearchShouldBeTrueWithWinnerX() {
        String[][] playingField = new String[][]{
                {"|X|", "|O|", "|X|"},
                {"|O|", "|X|", "|O|"},
                {"|X|", "|X|", "|O|"}};

        Assert.assertTrue(ticTacToeService.winnerSearch(playingField));
    }

    // Проверяем при наличии победителя
    @Test
    public void winnerSearchShouldBeTrueWithWinnerO() {
        String[][] playingField = new String[][]{
                {"|O|", "|X|", "|O|"},
                {"|X|", "|O|", "|X|"},
                {"|O|", "|X|", "|9|"}};

        Assert.assertTrue(ticTacToeService.winnerSearch(playingField));
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

        ticTacToeService.refreshPlayingField(playingField);

        Assert.assertArrayEquals(playingField, newPlayingField);
    }

    @Test
    public void saveRating() {
        fields.setFirstPlayer(new Player("Kirill", "X", 0, 2, 1));
        fields.setSecondPlayer(new Player("Alena", "O", 2, 0, 1));
        ticTacToeService.saveRating(model);
        Assert.assertEquals(-3, fields.getFirstPlayer().getRating());
        Assert.assertEquals(7, fields.getSecondPlayer().getRating());
        Mockito.verify(playerRepo, Mockito.times(1)).save(fields.getFirstPlayer());
        Mockito.verify(playerRepo, Mockito.times(1)).save(fields.getSecondPlayer());
        Assert.assertEquals("Рейтинг успешно сохранен!", model.getAttribute("message"));
        // проверка медота getRating, который запускается внутри saveRating
        Mockito.verify(playerRepo, Mockito.times(1)).findAll();
    }

    @Test
    public void saveHistory() {
        ticTacToeService.saveHistory();
        Mockito.verify(gameHistoryRepo, Mockito.times(1)).save(fields.getGameHistory());
    }

    @Test
    public void newPlayersShouldBeWithNullNames() {
        fields.setFirstPlayer(new Player("Kirill", "X", 0, 2, 1));
        fields.setSecondPlayer(new Player("Alena", "O", 2, 0, 1));
        ticTacToeService.newPlayers();
        Assert.assertNull(fields.getFirstPlayer().getName());
        Assert.assertNull(fields.getSecondPlayer().getName());
    }

    @Test
    public void gameHistoryIsEmpty() {
        ticTacToeService.gameHistoryIsEmpty(model);
        Mockito.verify(gameHistoryRepo, Mockito.times(1)).findAll();
    }

    @Test(expected = GameHistoryNotFoundException.class)
    public void showGameHistoryShouldThrowGameHistoryNotFoundException()
            throws GameHistoryNotFoundException, InputIdIsNullStringException {
        ticTacToeService.showGameHistory("1", model);
    }

    @Test(expected = InputIdIsNullStringException.class)
    public void showGameHistoryShouldThrowInputIdIsNullStringException()
            throws GameHistoryNotFoundException, InputIdIsNullStringException {
            ticTacToeService.showGameHistory("   ", model);
    }
}
