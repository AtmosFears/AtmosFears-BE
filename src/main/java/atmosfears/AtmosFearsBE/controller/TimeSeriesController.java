package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.database.Particulate;
import atmosfears.AtmosFearsBE.database.SensorCode;
import atmosfears.AtmosFearsBE.model.AggregatedParticulates;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.service.AirParticulatesService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/api/timeseries")
public class TimeSeriesController {

    final AirParticulatesService airParticulatesService;

    public TimeSeriesController(AirParticulatesService airParticulatesService) {
        this.airParticulatesService = airParticulatesService;
    }

    @CrossOrigin()
    @GetMapping(value = "/locations", produces = "application/json")
    public String stations() {
        JSONObject jsonObject = new JSONObject();
        for (SensorCode sensorCode : SensorCode.values()) {
            JSONObject locationInfo = new JSONObject();
            locationInfo.put("code", sensorCode.toString());
            locationInfo.put("name", sensorCode.getAddress());
            locationInfo.put("latitude", sensorCode.getLatitude());
            locationInfo.put("longitude", sensorCode.getLongitude());
            jsonObject.append("locations", locationInfo);
        }
        return jsonObject.toString();
    }

    @CrossOrigin()
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

        JSONObject json = new JSONObject();
        json.put("stations", sensors);
        json.put("data", Collections.emptyList());
        for (AggregatedParticulates aggregated : aggregatedParticulates) {
            JSONObject aggregatedJson = new JSONObject();
            aggregatedJson.put("date", aggregated.getDate().getTime());
            aggregatedJson.put("displayDate", new SimpleDateFormat("yyyy-MM-dd").format(aggregated.getDate()));

            JSONObject aggregatedSensors = new JSONObject();
            for (AirParticulates sensorParticulate : aggregated.getValues()) {
                JSONObject sensorParticulateJson = new JSONObject();
                sensorParticulateJson.put("CO", sensorParticulate.getCO());
                sensorParticulateJson.put("NO2", sensorParticulate.getNO2());
                sensorParticulateJson.put("O3", sensorParticulate.getO3());
                sensorParticulateJson.put("PM10", sensorParticulate.getPM10());
                sensorParticulateJson.put("PM25", sensorParticulate.getPM25());
                sensorParticulateJson.put("SO2", sensorParticulate.getSO2());
                aggregatedSensors.put(sensorParticulate.getCode().toString(), sensorParticulateJson);
            }
            aggregatedJson.put("sensors", aggregatedSensors);

            json.append("data", aggregatedJson);
        }

        Particulate particulate = Particulate.valueOf(pollution1);
        JSONObject line = new JSONObject();
        line.put("dataKey", particulate.name());
        line.put("stroke", "#8884d8");
        line.put("yAxisId", "left-axis");
        json.append("lines", line);

        JSONObject leftAxis = new JSONObject();
        leftAxis.put("label", particulate.getDisplayName());
        json.put("leftAxis", leftAxis);

        if (pollution2 != null) {
            Particulate particulate2 = Particulate.valueOf(pollution2);
            JSONObject line2 = new JSONObject();
            line2.put("dataKey", particulate2.name());
            line2.put("stroke", "#441111");
            line2.put("yAxisId", "right-axis");
            json.append("lines", line2);

            JSONObject rightAxis = new JSONObject();
            rightAxis.put("label", particulate2.getDisplayName());
            json.put("rightAxis", rightAxis);
        }

        return json.toString();
    }


}
