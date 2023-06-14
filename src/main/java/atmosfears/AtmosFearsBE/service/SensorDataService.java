package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.database.AirParticulatesRepository;
import atmosfears.AtmosFearsBE.database.Particulate;
import atmosfears.AtmosFearsBE.database.Sensor;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.model.CustomDateFormat;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@AllArgsConstructor
public class SensorDataService {
    private final AirParticulatesRepository airParticulatesRepository;

    public List<JSONObject> getRecentParticulatesList(){
        List<Sensor> sensorList = SensorsProvider.getInstance().values();
        List<JSONObject> jsonObjectList = new LinkedList<>();

        for (Sensor sensor : sensorList) {
            AirParticulates recent = airParticulatesRepository.findFirstByDateBeforeAndCode(new Date(), sensor.getCode());
            if(recent == null) continue;
            JSONObject json = new JSONObject();
            json.put("sensorCode", sensor.getCode());
            json.put("latitude", sensor.getLatitude());
            json.put("longitude", sensor.getLongitude());
            json.put("co", recent.getCO());
            json.put("no2", recent.getNO2());
            json.put("pm10", recent.getPM10());
            json.put("pm25", recent.getPM25());
            json.put("o3", recent.getO3());
            json.put("so2", recent.getSO2());
            json.put("date", recent.getDate());
            jsonObjectList.add(json);

        }
        return jsonObjectList;
    }

    public List<JSONObject> getAverageParticulatesForSensorsList(CustomDateFormat dateFormat){
        List<Sensor> sensorList = SensorsProvider.getInstance().values();
        List<JSONObject> jsonObjectList = new LinkedList<>();
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minus(dateFormat.getAmount(), dateFormat.getUnit());


        for (Sensor sensor : sensorList) {
            List<AirParticulates> sensorAirParticulatesList = airParticulatesRepository.findByDateBetweenAndCode(
                    Date.from(start.atZone(ZoneId.systemDefault()).toInstant()),
                    Date.from(end.atZone(ZoneId.systemDefault()).toInstant()),
                    sensor.getCode()
            );


            Map<Particulate, AirParticulatesService.AverageParticulateCounter> averages = new HashMap<>();
            Arrays.stream(Particulate.values()).forEach(p -> averages.put(p, new AirParticulatesService.AverageParticulateCounter(p)));

            sensorAirParticulatesList.forEach(airParticulates -> {
                averages.get(Particulate.CO).addValue(airParticulates.getCO());
                averages.get(Particulate.NO2).addValue(airParticulates.getNO2());
                averages.get(Particulate.O3).addValue(airParticulates.getO3());
                averages.get(Particulate.PM10).addValue(airParticulates.getPM10());
                averages.get(Particulate.PM25).addValue(airParticulates.getPM25());
                averages.get(Particulate.SO2).addValue(airParticulates.getSO2());
            });

            JSONObject json = new JSONObject();
            json.put("sensorCode", sensor.getCode());
            json.put("latitude", sensor.getLatitude());
            json.put("longitude", sensor.getLongitude());
            json.put("date", Date.from(start.atZone(ZoneId.systemDefault()).toInstant()));
            Arrays.stream(Particulate.values()).forEach(p ->
                    json.put(p.name().toLowerCase(), averages.get(p).getValue()));

            jsonObjectList.add(json);

        }
        return jsonObjectList;
    }
}
