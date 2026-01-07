package fr.miage.groupe2projetpoo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello MIAGE, backend is running!";
    }
}

// http://localhost:8080/hello
