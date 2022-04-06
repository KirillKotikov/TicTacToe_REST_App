package ru.kotikov.tictactoe_rest_app.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// структура журнала истории игры
@Entity
@NoArgsConstructor
public class GameHistory {

    @Transient
    private static Long currentId = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "first_player_id", nullable = false)
    private Player firstPlayer;

    @ManyToOne
    @JoinColumn(name = "second_player_id", nullable = false)
    private Player secondPlayer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gameHistory")
    private List<Step> steps = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private Player winner;

    public void setId() {
        this.id = currentId;
        currentId++;
    }

    public Long getId() {
        return id;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }
}

