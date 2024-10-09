package de.dreja.quiz.interfaces.web;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class GameEndpoint {
    
    @GetMapping({"/","/index"})
    public String getMainPage(Model model) {
        model.addAttribute("now", LocalDate.now());
        return "index";
    }
}
