package ru.kotikov.tictactoe_rest_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kotikov.tictactoe_rest_app.model.Step;

public interface StepRepo extends JpaRepository<Step, Long> {
}
