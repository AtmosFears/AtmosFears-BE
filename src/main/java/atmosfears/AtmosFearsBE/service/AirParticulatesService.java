package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.database.AirParticulatesRepository;
import atmosfears.AtmosFearsBE.database.Particulate;
import atmosfears.AtmosFearsBE.database.SensorCode;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

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

    public JSONArray getAverageParticulatesValues(Date startDate, Date endDate) {
        List<AirParticulates> filtered = airParticulatesRepository.findByDateBetween(startDate, endDate);
        Set<String> locations = filtered.stream().map(AirParticulates::getCode).map(Enum::toString).collect(Collectors.toSet());

        Map<String, Map<Particulate, AverageParticulateCounter>> averagesForLocations = new HashMap<>();
        for (String location : locations) {
            averagesForLocations.put(location, new HashMap<>());
            Arrays.stream(Particulate.values()).forEach(p -> averagesForLocations.get(location).put(p, new AverageParticulateCounter(p)));
        }

        System.out.println(locations);

        filtered.forEach(airParticulates -> {
            String location = airParticulates.getCode().toString();
            Map<Particulate, AverageParticulateCounter> averages = averagesForLocations.get(location);
            averages.get(Particulate.CO).addValue(airParticulates.getCO());
            averages.get(Particulate.NO2).addValue(airParticulates.getNO2());
            averages.get(Particulate.O3).addValue(airParticulates.getO3());
            averages.get(Particulate.PM10).addValue(airParticulates.getPM10());
            averages.get(Particulate.PM25).addValue(airParticulates.getPM25());
            averages.get(Particulate.SO2).addValue(airParticulates.getSO2());
            averagesForLocations.put(location, averages);
        });

        JSONArray jsonArray = new JSONArray();

        for (String location : locations) {
            Map<String, Object> locationProperties = new HashMap<>();
            Map<Particulate, AverageParticulateCounter> averages = averagesForLocations.get(location);
            locationProperties.put("location", SensorCode.valueOf(location).getAddress());
            for (Particulate p : averages.keySet()) {
                locationProperties.put(p.name(), averages.get(p).getValue());
            }
            jsonArray.put(locationProperties);
        }
        return jsonArray;
    }
}
