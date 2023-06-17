package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.database.Particulate;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.model.CustomDateFormat;
import atmosfears.AtmosFearsBE.service.AirParticulatesService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import atmosfears.AtmosFearsBE.service.SensorDataService;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataEndpointController {

    private final AirParticulatesService airParticulatesService;
    private final SensorDataService sensorDataService;

    public DataEndpointController(AirParticulatesService airParticulatesService,
                                  SensorDataService sensorDataService) {
        this.airParticulatesService = airParticulatesService;
        this.sensorDataService = sensorDataService;
    }

    @CrossOrigin()
    @GetMapping("/data/average")
    public ResponseEntity<Map<String, Object>> getAverageParticulates(
            @RequestParam(name = "start") String startDateStr,
            @RequestParam(name = "end") String endDateStr) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd-hh:mm");
        Date startDate, endDate;
        try {
            startDate = simpleDateFormat.parse(startDateStr);
            endDate = simpleDateFormat.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(airParticulatesService.getAverageParticulatesValues(
                startDate,
                endDate
        ).toMap());
    }

    @CrossOrigin()
    @GetMapping("/data/recent")
    public ResponseEntity<List<Map<String, Object>>> getRecentParticulates() {
        return ResponseEntity.ok(sensorDataService.getRecentParticulatesList()
                                                  .stream()
                                                  .map(JSONObject::toMap)
                                                  .toList());
    }

    @CrossOrigin()
    @GetMapping("/data/sensors/average")
    public ResponseEntity<List<Map<String, Object>>> getAverageParticulatesForSensors(
            @RequestParam(name = "format") String dateFormat
    ) {
        CustomDateFormat format = CustomDateFormat.fromString(dateFormat);
        if (format == null) {
            return ResponseEntity.ok(sensorDataService.getRecentParticulatesList()
                                                      .stream()
                                                      .map(JSONObject::toMap)
                                                      .toList());
        }
        return ResponseEntity.ok(sensorDataService.getAverageParticulatesForSensorsList(
                format).stream().map(JSONObject::toMap).toList());
    }

    @GetMapping("/data/windrose")
    public ResponseEntity<Map<String, Object>> getWindroseData(
            @RequestParam(required = false) Optional<String> pollutant,
            @RequestParam(name = "start")
            @DateTimeFormat(pattern = "yyyy-MM-dd-hh:mm") Date startDate,
            @RequestParam(name = "end")
            @DateTimeFormat(pattern = "yyyy-MM-dd-hh:mm") Date endDate) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        List<AirParticulates> airParticulatesList =
                airParticulatesService.findByDateBetween(startDate, endDate);

        for (AirParticulates airParticulates : airParticulatesList) {
            HashMap<String, Object> item = new HashMap<>();
            item.put("id", airParticulates.getId());
            item.put("date", airParticulates.getDate());
            item.put("code", airParticulates.getCode());
            item.put("ws", airParticulates.getWindSpeed());
            item.put("wd", airParticulates.getWindDirection());

            if (pollutant.isPresent()) {
                if (pollutant.get().equals("PM10"))
                    item.put("PM10", airParticulates.getPM10());
                else if (pollutant.get().equals("PM25"))
                    item.put("PM25", airParticulates.getPM25());
                else if (pollutant.get().equals("CO"))
                    item.put("CO", airParticulates.getCO());
                else if (pollutant.get().equals("NO2"))
                    item.put("NO2", airParticulates.getNO2());
                else if (pollutant.get().equals("SO2"))
                    item.put("SO2", airParticulates.getSO2());
                else if (pollutant.get().equals("O3"))
                    item.put("O3", airParticulates.getO3());
                else
                    return ResponseEntity.badRequest().build();
            } else {
                item.put("PM10", airParticulates.getPM10());
                item.put("PM25", airParticulates.getPM25());
                item.put("CO", airParticulates.getCO());
                item.put("NO2", airParticulates.getNO2());
                item.put("SO2", airParticulates.getSO2());
                item.put("O3", airParticulates.getO3());
            }
            result.add(item);
        }

        return ResponseEntity.ok(new HashMap<String, Object>() {
            {
                put("responseList", result);
            }
        });
    }

    @CrossOrigin
    @GetMapping("/data/windrose/aggr")
    public ResponseEntity<List<?>> getWindroseData(
            @RequestParam String pollutant,
            @RequestParam(name = "start") String startDateStr,
            @RequestParam(name = "end") String endDateStr) {
        Date startDate, endDate;
        try {
            startDate = convertDate(startDateStr, "yyyy-MM-dd");
            endDate = convertDate(endDateStr, "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

        List<Particulate> particulates = Arrays.stream(Particulate.values())
                                               .filter(p -> p.toString()
                                                             .equals(pollutant))
                                               .toList();
        if (particulates.size() != 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(airParticulatesService.getAggregatedWindroseData(
                startDate,
                endDate,
                particulates.get(0)
        ).toList());
    }

    private Date convertDate(String dateStr, String format)
            throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(dateStr);
    }
}
