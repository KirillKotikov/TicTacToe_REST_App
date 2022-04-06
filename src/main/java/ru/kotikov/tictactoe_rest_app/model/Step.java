package ru.kotikov.tictactoe_rest_app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Step {
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

    public Step(String fieldCell, byte num, Player player) {
        this.fieldCell = fieldCell;
        this.num = num;
        this.player = player;
    }
}
