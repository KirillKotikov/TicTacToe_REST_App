package ru.kotikov.tictactoe_rest_app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Step {

//    @Transient
//    private Long currentId = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String fieldCell;
    private byte num;
    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
    @ManyToOne
    @JoinColumn(name = "game_history_id", nullable = false)
    private GameHistory gameHistory;

//    public Step() {
//        this.id = currentId;
//        currentId++;
//    }

    public Step(String fieldCell, byte num, Player player) {
//        this.id = currentId;
//        currentId++;
        this.fieldCell = fieldCell;
        this.num = num;
        this.player = player;
    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getFieldCell() {
//        return fieldCell;
//    }
//
//    public void setFieldCell(String fieldCell) {
//        this.fieldCell = fieldCell;
//    }
//
//    public byte getNum() {
//        return num;
//    }
//
//    public void setNum(byte num) {
//        this.num = num;
//    }
//
//    public Player getPlayer() {
//        return player;
//    }
//
//    public void setPlayer(Player player) {
//        this.player = player;
//    }
//
//    public GameHistory getGameHistory() {
//        return gameHistory;
//    }
//
//    public void setGameHistory(GameHistory gameHistory) {
//        this.gameHistory = gameHistory;
//    }
}
