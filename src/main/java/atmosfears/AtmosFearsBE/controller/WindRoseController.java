package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.service.AirParticulatesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class WindRoseController {

    private final AirParticulatesService airParticulatesService;

    public WindRoseController(AirParticulatesService airParticulatesService) {
        this.airParticulatesService = airParticulatesService;
    }


    @GetMapping("/windrose")
    public List<AirParticulates> getWindRoseData(@RequestParam String pollutant,
                                  @RequestParam(required = false) String from,
                                  @RequestParam(required = false) String to){
        airParticulatesService.insertDummyData();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-hh-mm");
        LocalDateTime start;
        if(from == null){
            start = LocalDateTime.of(2000,1,1,1,0);
        }else{
            from += "-00-00";
            start = LocalDateTime.parse(from, dateTimeFormatter);
        }
        LocalDateTime finish;
        if(to == null){
            finish = LocalDateTime.of(2030,1,1,1,0);
        }else{
            to += "-00-00";
            finish = LocalDateTime.parse(to, dateTimeFormatter);
        }
        return airParticulatesService.findAllBetweenDates(start, finish);
    }
}
