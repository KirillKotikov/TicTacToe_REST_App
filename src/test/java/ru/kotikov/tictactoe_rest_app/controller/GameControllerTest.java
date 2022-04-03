package ru.kotikov.tictactoe_rest_app.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.kotikov.tictactoe_rest_app.controller.GameController;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameController gameController;

    @Test
    public void startContextLoads() throws Exception {
        this.mockMvc.perform(get("/gameplay"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Привет! Добро пожаловать в игру крестики-нолики:)")))
                .andExpect(content().string(containsString("Введите имя первого игрока (ходит крестиками):")))
                .andExpect(content().string(containsString("Введите имя второго игрока (ходит ноликами):")));
    }
}
