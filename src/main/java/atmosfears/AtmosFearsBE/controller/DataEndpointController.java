package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.service.AirParticulatesService;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class DataEndpointController {

    private AirParticulatesService airParticulatesService;

    public DataEndpointController(AirParticulatesService airParticulatesService) {
        this.airParticulatesService = airParticulatesService;
    }


    @CrossOrigin
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

    @GetMapping("/data/windrose")
    public ResponseEntity<Map<String,Object>> getWindroseData(@RequestParam(required = false) Optional<String> pollutant,
                                                              @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd-hh:mm") Date startDate,
                                                              @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd-hh:mm") Date endDate){
        HashMap<String, Object> response = new HashMap<>();
        response.put("responseList", new ArrayList<Map<String, Object>>());
        List<AirParticulates> airParticulatesList =  airParticulatesService.findByDateBetween(startDate, endDate);
        for(AirParticulates airParticulates: airParticulatesList){
            HashMap<String, Object> item = new HashMap<>();
            item.put("id", airParticulates.getId());
            item.put("date", airParticulates.getDate());
            item.put("code", airParticulates.getCode());
            item.put("ws", airParticulates.getWindSpeed());
            item.put("wd", airParticulates.getWindDirection());
            if(pollutant.isPresent()){
                if(pollutant.get().equals("PM10"))      item.put("PM10", airParticulates.getPM10());
                else if(pollutant.get().equals("PM25")) item.put("PM25", airParticulates.getPM25());
                else if(pollutant.get().equals("CO"))   item.put("CO", airParticulates.getCO());
                else if(pollutant.get().equals("NO2"))  item.put("NO2", airParticulates.getNO2());
                else if(pollutant.get().equals("SO2"))  item.put("SO2", airParticulates.getSO2());
                else if(pollutant.get().equals("O3"))   item.put("O3", airParticulates.getO3());
                else return ResponseEntity.badRequest().build();
            }else{
                item.put("PM10", airParticulates.getPM10());
                item.put("PM25", airParticulates.getPM25());
                item.put("CO", airParticulates.getCO());
                item.put("NO2", airParticulates.getNO2());
                item.put("SO2", airParticulates.getSO2());
                item.put("O3", airParticulates.getO3());
            }
            ((ArrayList) response.get("responseList")).add(item);
        }
        return ResponseEntity.ok(response);
    }

}
