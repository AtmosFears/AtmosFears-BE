package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.database.AirParticulatesRepository;
import atmosfears.AtmosFearsBE.database.Particulate;
import atmosfears.AtmosFearsBE.database.SensorCode;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.model.AggregatedParticulates;
import atmosfears.AtmosFearsBE.model.AirParticulatesWindroseInfo;
import atmosfears.AtmosFearsBE.model.Direction;
import atmosfears.AtmosFearsBE.util.AirParticulatesConverter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
            System.out.println((occurrences == 0 ? 0 : valuesSum / occurrences) + ", " + valuesSum + ", " + valuesSum);
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

    public List<AirParticulates> findByDateBetween(Date from, Date to) {
        return airParticulatesRepository.findByDateBetween(from, to);
    }

    public JSONObject getAverageParticulatesValues(
            Date startDate,
            Date endDate) {
        List<AirParticulates> filteredParticulates = airParticulatesRepository
                .findByDateBetween(startDate, endDate);
        return createAverageParticulatesValuesJson(filteredParticulates);
    }

    public List<AggregatedParticulates> findByDateBetweenAndSensorCodeIn(
            Date from,
            Date to,
            List<SensorCode> sensorCodes) {
        return airParticulatesRepository.aggregateByDay(from, to, sensorCodes);
    }

    public JSONArray getAggregatedWindroseData(Date startDate,
                                               Date endDate,
                                               Particulate particulate) {
        List<AirParticulates> filteredParticulates =
                airParticulatesRepository.findByDateBetween(startDate, endDate);
        List<AirParticulatesWindroseInfo> windroseParametersList =
                getAirParticulatesWindroseInfos(
                        particulate,
                        filteredParticulates
                );

        int totalParameters = windroseParametersList.size();

        JSONArray jsonArray = new JSONArray();

        for (Direction direction : Direction.values()) {
            Map<String, Object> chartDataForDirection =
                    getWindroseDirectionChartData(
                            windroseParametersList,
                            (double) totalParameters,
                            direction
                    );
            jsonArray.put(chartDataForDirection);
        }

        return jsonArray;
    }

    private static Map<String, Object> getWindroseDirectionChartData(
            List<AirParticulatesWindroseInfo> windroseParametersList,
            double totalParameters,
            Direction direction) {
        List<AirParticulatesWindroseInfo> windroseParametersForDirection =
                windroseParametersList
                        .stream()
                        .filter(windroseParameters -> windroseParameters.getDirection()
                                                                        .equals(direction))
                        .toList();

        double totalForDirectionPercentage =
                windroseParametersForDirection.size() /
                totalParameters;

        Map<String, Object> chartDataForDirection = new HashMap<>();
        chartDataForDirection.put("angle", direction.toString());
        chartDataForDirection.put("total", totalForDirectionPercentage);

        for (String range : ranges) {
            int index = Arrays.binarySearch(ranges, range);
            long count = windroseParametersForDirection
                    .stream()
                    .filter(windroseParameters -> windroseParameters.getSection() ==
                                                  index)
                    .count();

            double directionRangePercentage =
                    count / totalParameters;
            chartDataForDirection.put(range, directionRangePercentage);
        }
        return chartDataForDirection;
    }

    private static List<AirParticulatesWindroseInfo> getAirParticulatesWindroseInfos(
            Particulate particulate,
            List<AirParticulates> filteredParticulates) {
        List<AirParticulatesWindroseInfo> windroseParametersList =
                filteredParticulates
                        .stream()
                        .map(airParticulates -> AirParticulatesConverter
                                .convertToWindroseParameters(
                                        airParticulates,
                                        particulate
                                )
                        ).toList();
        return windroseParametersList;
    }

    private Set<String> getLocations(List<AirParticulates> filtered) {
        return filtered
                .stream()
                .map(AirParticulates::getCode)
                .collect(Collectors.toSet());
    }

    private static JSONArray createAverageParticulatesValuesResponse(
            Set<String> locations,
            Map<String, Map<Particulate, AverageParticulateCounter>> averagesForLocations) {
        JSONArray jsonArray = new JSONArray();

        for (String location : locations) {
            Map<String, Object> locationProperties = new HashMap<>();
            Map<Particulate, AverageParticulateCounter> averages =
                    averagesForLocations.get(location);
            locationProperties.put(
                    "location",
                    SensorCode.valueOf(location).getAddress()
            );
            for (Particulate p : averages.keySet()) {
                locationProperties.put(p.name(), averages.get(p).getValue());
            }
            jsonArray.put(locationProperties);
        }
        return jsonArray;
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
}
