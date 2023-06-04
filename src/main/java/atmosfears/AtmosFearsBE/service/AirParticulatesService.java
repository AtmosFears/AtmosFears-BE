package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.database.AirParticulatesFileRepository;
import atmosfears.AtmosFearsBE.database.AirParticulatesRepository;
import atmosfears.AtmosFearsBE.database.Particulate;
import atmosfears.AtmosFearsBE.database.SensorCode;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.model.AggregatedParticulates;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class AirParticulatesService {

    @RequiredArgsConstructor
    static class AverageParticulateCounter {
        public final Particulate name;
        private int occurrences = 0;
        private double valuesSum = 0;

        void addValue(Double value) {
            if (value != null) {
                valuesSum += value;
                occurrences++;
            }
        }

        double getValue() {
            return occurrences == 0 ? 0 : valuesSum / occurrences;
        }
    }

    private final AirParticulatesRepository airParticulatesRepository;

    public List<AirParticulates> findAll() {
        return airParticulatesRepository.findAll();
    }

    public void insertEmpty() {
        airParticulatesRepository.insert(new AirParticulates());
    }

    public JSONObject getAverageParticulatesValues(Date startDate, Date endDate) {
        Map<Particulate, AverageParticulateCounter> averages = new HashMap<>();
        Arrays.stream(Particulate.values()).forEach(p -> averages.put(p, new AverageParticulateCounter(p)));

        List<AirParticulates> filtered = airParticulatesRepository.findByDateBetween(startDate, endDate);
        filtered.forEach(airParticulates -> {
            averages.get(Particulate.CO).addValue(airParticulates.getCO());
            averages.get(Particulate.NO2).addValue(airParticulates.getNO2());
            averages.get(Particulate.O3).addValue(airParticulates.getO3());
            averages.get(Particulate.PM10).addValue(airParticulates.getPM10());
            averages.get(Particulate.PM25).addValue(airParticulates.getPM25());
            averages.get(Particulate.SO2).addValue(airParticulates.getSO2());
        });

        JSONObject json = new JSONObject();
        Arrays.stream(Particulate.values()).forEach(p ->
                json.put(p.name(), averages.get(p).getValue()));

        return json;
    }

    public List<AggregatedParticulates> findByDateBetweenAndSensorCodeIn(Date from, Date to, List<SensorCode> sensorCodes) {
        return airParticulatesRepository.aggregateByDay(from, to, sensorCodes);
    }

    public List<JSONObject> getRecentParticulatesList(){
        List<SensorCode> sensorCodeList = Arrays.stream(SensorCode.values()).toList();
        List<JSONObject> jsonObjectList = new LinkedList<>();

        for (SensorCode sensorCode : sensorCodeList) {
            AirParticulates recent = airParticulatesRepository.findFirstByDateBeforeAndCode(new Date(), sensorCode);
            if(recent == null) continue;
            JSONObject json = new JSONObject();
            json.put("sensorCode", sensorCode.name());
            json.put("latitude", sensorCode.getLatitude());
            json.put("longitude", sensorCode.getLongitude());
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
}
