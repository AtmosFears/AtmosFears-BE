package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.service.AirParticulatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private AirParticulatesService airParticulatesService;
    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot! <br> Repository findAll result: " + airParticulatesService.findAll();
    }

}
