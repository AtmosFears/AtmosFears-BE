package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.service.AirParticulatesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class HelloController {

    private final AirParticulatesService airParticulatesService;

    public HelloController(AirParticulatesService airParticulatesService) {
        this.airParticulatesService = airParticulatesService;
    }

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot! <br> Repository getAverage result: " +
                airParticulatesService.getAverageParticulatesValues(new Date("01/01/2021 00:00:00"), new Date("01/01/2021 04:00:00"));
    }

}
