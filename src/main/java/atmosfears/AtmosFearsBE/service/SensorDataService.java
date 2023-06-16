package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.database.AirParticulatesRepository;
import atmosfears.AtmosFearsBE.database.Sensor;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.model.CustomDateFormat;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@AllArgsConstructor
public class SensorDataService {
    private final AirParticulatesRepository airParticulatesRepository;

    private final AirParticulatesService airParticulatesService;

    public List<JSONObject> getRecentParticulatesList() {
        List<Sensor> sensorList = SensorsProvider.getInstance().values();
        List<JSONObject> jsonObjectList = new LinkedList<>();

        for (Sensor sensor : sensorList) {
            AirParticulates recent = airParticulatesRepository.findFirstByDateBeforeAndCode(new Date(), sensor.code());
            if(recent == null) continue;
            JSONObject json = new JSONObject();
            json.put("sensorCode", sensor.code());
            json.put("latitude", sensor.latitude());
            json.put("longitude", sensor.longitude());
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
                    sensor.code()
            );

            JSONObject json = airParticulatesService
                    .createAverageParticulatesValuesJson(sensorAirParticulatesList);

            json.put("sensorCode", sensor.code());
            json.put("latitude", sensor.latitude());
            json.put("longitude", sensor.longitude());
            json.put("date", Date.from(start.atZone(ZoneId.systemDefault()).toInstant()));

            jsonObjectList.add(json);

        }
        return jsonObjectList;
    }
}
