package org.fdu;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WordleController {

    @GetMapping("/wordle")
    public String intro() {
        return "Welcome to Wordle (Web Spike)  Version 0.2";
    }
}
