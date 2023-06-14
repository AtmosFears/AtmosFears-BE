package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.model.CustomDateFormat;
import atmosfears.AtmosFearsBE.service.AirParticulatesService;
import atmosfears.AtmosFearsBE.service.SensorDataService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class DataEndpointController {

    private final SensorDataService sensorDataService;
    private final AirParticulatesService airParticulatesService;

    public DataEndpointController(SensorDataService sensorDataService, AirParticulatesService airParticulatesService) {
        this.sensorDataService = sensorDataService;
        this.airParticulatesService = airParticulatesService;
    }


    @CrossOrigin()
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

    @CrossOrigin()
    @GetMapping("/data/recent")
    public ResponseEntity<List<Map<String, Object>>> getRecentParticulates(){
        return ResponseEntity.ok(sensorDataService.getRecentParticulatesList().stream().map(JSONObject::toMap).toList());
    }

    @CrossOrigin()
    @GetMapping("/data/sensors/average")
    public ResponseEntity<List<Map<String, Object>>> getAverageParticulatesForSensors(
            @RequestParam(name="format")String dateFormat
            ){
        CustomDateFormat format = CustomDateFormat.fromString(dateFormat);
        if(format == null){
            return ResponseEntity.ok(sensorDataService.getRecentParticulatesList().stream().map(JSONObject::toMap).toList());
        }
        return ResponseEntity.ok(sensorDataService.getAverageParticulatesForSensorsList(format).stream().map(JSONObject::toMap).toList());
    }
}
