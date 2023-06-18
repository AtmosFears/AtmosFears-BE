package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.database.Particulate;
import atmosfears.AtmosFearsBE.database.Sensor;
import atmosfears.AtmosFearsBE.database.SensorCode;
import atmosfears.AtmosFearsBE.model.AggregatedParticulates;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.service.AirParticulatesService;
import atmosfears.AtmosFearsBE.service.SensorsProvider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/timeseries")
public class TimeSeriesController {

    private final AirParticulatesService airParticulatesService;

    public TimeSeriesController(AirParticulatesService airParticulatesService) {
        this.airParticulatesService = airParticulatesService;
    }

    @CrossOrigin
    @GetMapping(value = "/locations", produces = "application/json")
    public String stations() {
        JSONObject jsonObject = new JSONObject();
        for (Sensor sensor : SensorsProvider.getInstance().values()) {
            JSONObject locationInfo = new JSONObject();
            locationInfo.put("code", sensor.toString());
            locationInfo.put("name", sensor.address());
            locationInfo.put("latitude", sensor.latitude());
            locationInfo.put("longitude", sensor.longitude());
            jsonObject.append("locations", locationInfo);
        }
        return jsonObject.toString();
    }

    @CrossOrigin
    @GetMapping(value = "/data", produces = "application/json")
    public String data(
            @RequestParam("dateFrom") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam("dateTo") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam("pollution1") String pollution1,
            @RequestParam(name = "pollution2", required = false) String pollution2,
            @RequestParam(value = "sensors") String[] sensors
    ) {
        List<SensorCode> codeList = Arrays.stream(sensors).map(SensorCode::valueOf).toList();
        List<AggregatedParticulates> aggregatedParticulates =
                airParticulatesService.findByDateBetweenAndSensorCodeIn(from, to, codeList);

        // Aggregate pollution data
        JSONObject json = new JSONObject();
        json.put("stations", sensors);
        json.put("data", Collections.emptyList());

        for (AggregatedParticulates aggregated : aggregatedParticulates) {
            json.append("data", getAggregateParticulateJson(aggregated));
        }

        // Create chart data
        Particulate particulate = Particulate.valueOf(pollution1);
        json.append("lines", getChartLine(particulate,  "#8884d8", "left-axis"));
        json.put("leftAxis", getChartAxis(particulate.getDisplayName()));

        if (pollution2 != null) {
            Particulate particulate2 = Particulate.valueOf(pollution2);
            json.append("lines", getChartLine(particulate2, "#441111", "right-axis"));
            json.put("rightAxis", getChartAxis(particulate2.getDisplayName()));
        }

        return json.toString();
    }

    private JSONObject getSensorParticulateJson(AirParticulates sensorParticulate) {
        JSONObject sensorParticulateJson = new JSONObject();
        sensorParticulateJson.put("CO", sensorParticulate.getCO());
        sensorParticulateJson.put("NO2", sensorParticulate.getNO2());
        sensorParticulateJson.put("O3", sensorParticulate.getO3());
        sensorParticulateJson.put("PM10", sensorParticulate.getPM10());
        sensorParticulateJson.put("PM25", sensorParticulate.getPM25());
        sensorParticulateJson.put("SO2", sensorParticulate.getSO2());
        return sensorParticulateJson;
    }

    private JSONObject getAggregateParticulateJson(AggregatedParticulates aggregatedParticulates) {
        JSONObject aggregatedJson = new JSONObject();
        aggregatedJson.put("date", aggregatedParticulates.getDate().getTime());
        aggregatedJson.put("displayDate", new SimpleDateFormat("yyyy-MM-dd")
                .format(aggregatedParticulates.getDate()));
        JSONObject aggregatedSensors = new JSONObject();

        for (AirParticulates sensorParticulate : aggregatedParticulates.getValues()) {
            aggregatedSensors.put(sensorParticulate.getCode(), getSensorParticulateJson(sensorParticulate));
        }
        aggregatedJson.put("sensors", aggregatedSensors);
        return aggregatedJson;
    }

    private JSONObject getChartLine(Particulate particulate, String color, String axisId) {
        JSONObject line = new JSONObject();
        line.put("dataKey", particulate.name());
        line.put("stroke", color);
        line.put("yAxisId", axisId);
        return line;
    }

    private JSONObject getChartAxis(String label) {
        JSONObject axis = new JSONObject();
        axis.put("label", label);
        return axis;
    }
}
