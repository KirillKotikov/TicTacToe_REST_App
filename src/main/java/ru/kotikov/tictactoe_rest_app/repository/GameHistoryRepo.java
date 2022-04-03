package ru.kotikov.tictactoe_rest_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kotikov.tictactoe_rest_app.model.GameHistory;

public interface GameHistoryRepo extends JpaRepository<GameHistory, Long> {
}
