package de.dreja.quiz.interfaces.web;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class GameEndpoint {
    
    @GetMapping({"/","/index"})
    public String getMainPage(Model model) {
        model.addAttribute("now", LocalDate.now());
        return "index";
    }

    @GetMapping("/game/{gameId}")
    public String getGameOuterPage(@PathVariable("gameId") String gameId, Model model) {
        model.addAttribute("gameId", gameId);
        return "game";
    }

    @GetMapping("/game/{gameId}/content")
    public String getGameInnerPage(@PathVariable("gameId") String gameId, Model model) {
        model.addAttribute("gameId", gameId);
        model.addAttribute("time", LocalTime.now());
        return "game-content";
    }
}
