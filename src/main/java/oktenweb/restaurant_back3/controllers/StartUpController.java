package oktenweb.restaurant_back3.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartUpController {

    @GetMapping("/")
    public String home(){
        System.out.println("forward:/index.html");
        return "forward:/index.html";

    }
}
