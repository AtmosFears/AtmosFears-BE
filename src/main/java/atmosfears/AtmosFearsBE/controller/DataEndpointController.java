package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.service.AirParticulatesService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
public class DataEndpointController {

    private AirParticulatesService airParticulatesService;

    public DataEndpointController(AirParticulatesService airParticulatesService) {
        this.airParticulatesService = airParticulatesService;
    }


    @GetMapping("/data/average")
    public ResponseEntity<Map<String, Object>> getAverageParticulates(@RequestParam(name = "start") String startDateStr,
                                                      @RequestParam(name = "end") String endDateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh:mm");
        Date startDate, endDate;
        try {
            startDate = simpleDateFormat.parse(startDateStr);
            endDate = simpleDateFormat.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(airParticulatesService.getAverageParticulatesValues(startDate, endDate).toMap());
    }
}