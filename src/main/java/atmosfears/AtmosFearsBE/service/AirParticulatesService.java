package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.database.AirParticulatesRepository;
import atmosfears.AtmosFearsBE.database.Particulate;
import atmosfears.AtmosFearsBE.database.Sensor;
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
            System.out.println(occurrences == 0 ? 0 : valuesSum / occurrences);
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
        List<AirParticulates> filtered = airParticulatesRepository.findByDateBetween(startDate, endDate);
        return createAverageParticulatesValuesJson(filtered);
    }

    public JSONObject createAverageParticulatesValuesJson(List<AirParticulates>  particulates) {
        Map<Particulate, AverageParticulateCounter> averages = new HashMap<>();
        Arrays.stream(Particulate.values()).forEach(p -> averages.put(p, new AverageParticulateCounter(p)));

        particulates.forEach(airParticulates -> {
            averages.get(Particulate.CO).addValue(airParticulates.getCO());
            averages.get(Particulate.NO2).addValue(airParticulates.getNO2());
            averages.get(Particulate.O3).addValue(airParticulates.getO3());
            averages.get(Particulate.PM10).addValue(airParticulates.getPM10());
            averages.get(Particulate.PM25).addValue(airParticulates.getPM25());
            averages.get(Particulate.SO2).addValue(airParticulates.getSO2());
        });

        JSONObject json = new JSONObject();
        Arrays.stream(Particulate.values())
              .forEach(p -> json.put(
                      p.name().toLowerCase(),
                      averages.get(p).getValue()
              ));

        return json;
    }

    public List<AggregatedParticulates> findByDateBetweenAndSensorCodeIn(Date from, Date to, List<Sensor> sensors) {
        return airParticulatesRepository.aggregateByDay(from, to, sensors);
    }
}
