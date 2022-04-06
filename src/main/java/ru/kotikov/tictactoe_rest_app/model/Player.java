package ru.kotikov.tictactoe_rest_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(unique = true)
    private String name;
    private String symbol;
    private int numberOfWins = 0;
    private int numberOfLoses = 0;
    private int numberOfDraws = 0;
    private int rating = 0;

    @ManyToMany
    @JoinTable(
            name = "player_game_histories",
            joinColumns = {@JoinColumn(name = "player_id")}
    )
    private List<GameHistory> gameHistories;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private List<Step> steps;

    public Player(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public Player(Long id, String name, String symbol) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
    }

    public Player(String name, String symbol, int numberOfWins, int numberOfLoses, int numberOfDraws) {
        this.name = name;
        this.symbol = symbol;
        this.numberOfWins = numberOfWins;
        this.numberOfLoses = numberOfLoses;
        this.numberOfDraws = numberOfDraws;
    }
}
