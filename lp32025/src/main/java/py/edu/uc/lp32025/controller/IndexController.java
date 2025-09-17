package py.edu.uc.lp32025.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String redirectToHolaMundo() {
        return "redirect:/HolaMundo";
    }
}