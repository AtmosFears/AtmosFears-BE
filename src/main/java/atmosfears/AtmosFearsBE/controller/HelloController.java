package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.service.AirParticulatesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final AirParticulatesService airParticulatesService;

    public HelloController(AirParticulatesService airParticulatesService) {
        this.airParticulatesService = airParticulatesService;
    }

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot! <br> Repository findAll result: " + airParticulatesService.findAll();
    }

}
