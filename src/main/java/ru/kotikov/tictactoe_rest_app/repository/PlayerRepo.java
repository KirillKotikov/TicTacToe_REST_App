package ru.kotikov.tictactoe_rest_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kotikov.tictactoe_rest_app.model.Player;

public interface PlayerRepo extends JpaRepository<Player, Long> {
    Player findByName(String name);
}
