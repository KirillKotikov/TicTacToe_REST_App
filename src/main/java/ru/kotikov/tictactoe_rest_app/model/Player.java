package ru.kotikov.tictactoe_rest_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    @Transient
    private int numberOfWins = 0;
    @Transient
    private int numberOfLoses = 0;
    @Transient
    private int numberOfDraws = 0;
    private int rating = 0;

    public Player(String name, int numberOfWins, int numberOfLoses, int numberOfDraws) {
        this.name = name;
        this.numberOfWins = numberOfWins;
        this.numberOfLoses = numberOfLoses;
        this.numberOfDraws = numberOfDraws;
    }
}
