package ru.kotikov.tictactoe_rest_app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

// структура журнала истории игры
@Data
@NoArgsConstructor
@Entity
public class GameHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String firstPlayer;
    private String secondPlayer;
    @Size(max = 600)
    private String steps;
    private String gameResult;

    public GameHistory(String firstPlayer, String secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }
}

