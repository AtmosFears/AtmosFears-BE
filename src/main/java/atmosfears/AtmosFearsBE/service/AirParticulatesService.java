package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.database.AirParticulatesRepository;
import atmosfears.AtmosFearsBE.database.Particulate;
import atmosfears.AtmosFearsBE.database.SensorCode;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.model.AirParticulatesWindroseInfo;
import atmosfears.AtmosFearsBE.model.Direction;
import atmosfears.AtmosFearsBE.util.AirParticulatesConverter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
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

    private static final String[] ranges = {
            "0-1",
            "1-2",
            "2-3",
            "3-4",
            "4-5",
            "5-6",
            "6-7",
            "7+"
    };

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

    public JSONArray getAggregatedWindroseData(Date startDate, Date endDate, Particulate particulate) {
        List<AirParticulates> filtered = airParticulatesRepository.findByDateBetween(startDate, endDate);

        List<AirParticulatesWindroseInfo> windroseParametersList = filtered.stream()
                .map(airParticulates -> AirParticulatesConverter.convertToWindroseParameters(airParticulates, particulate)).toList();

        int totalParameters = windroseParametersList.size();

        double totalPercentage = 0;
        JSONArray jsonArray = new JSONArray();

        for (Direction direction : Direction.values()) {
            List<AirParticulatesWindroseInfo> windroseParametersForDirection =
                    windroseParametersList.stream().filter(windroseParameters -> windroseParameters.getDirection().equals(direction)).toList();
            double totalForDirectionPercentage = windroseParametersForDirection.size() / (double)totalParameters;
            totalPercentage += totalForDirectionPercentage;
            Map<String, Object> chartDataForDirection = new HashMap<>();
            chartDataForDirection.put("angle", direction.toString());
            chartDataForDirection.put("total", totalForDirectionPercentage);

            for (String range : ranges) {
                int index = Arrays.binarySearch(ranges, range);
                long count = windroseParametersForDirection.stream().filter(windroseParameters -> windroseParameters.getSection() == index).count();
                double directionRangePercentage = count / (double) totalParameters;
                chartDataForDirection.put(range, directionRangePercentage);
            }

            jsonArray.put(chartDataForDirection);
        }
        System.out.println(totalPercentage);
        return jsonArray;
    }

    public List<AirParticulates> findByDateBetween(Date from, Date to){
        return airParticulatesRepository.findByDateBetween(from, to);
    }

    public List<AirParticulates> findByDateBetweenAndSensorCodeIn(Date from, Date to, List<SensorCode> sensorCodes) {
        return airParticulatesRepository.findByDateBetweenAndSensorCodeIn(from, to, sensorCodes);
    }
}
