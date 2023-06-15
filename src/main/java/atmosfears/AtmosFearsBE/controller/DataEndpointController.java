package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.service.AirParticulatesService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataEndpointController {

    private final AirParticulatesService airParticulatesService;

    public DataEndpointController(AirParticulatesService airParticulatesService) {
        this.airParticulatesService = airParticulatesService;
    }

    @CrossOrigin
    @GetMapping("/data/average")
    public ResponseEntity<List<Object>> getAverageParticulates(
            @RequestParam(name = "start") String startDateStr,
            @RequestParam(name = "end") String endDateStr) {
        Date startDate, endDate;
        try {
            startDate = convertDate(startDateStr, "yyyy-MM-dd");
            endDate = convertDate(endDateStr, "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
            List<Object> err = new ArrayList<>(Arrays.asList("Error while parsing date"));
            return ResponseEntity.internalServerError().body(err);
        }
        return ResponseEntity.ok(airParticulatesService.getAverageParticulatesValues(startDate, endDate).toList());
    }

    private Date convertDate(String dateStr, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(dateStr);
    }
}
