package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.database.Particulate;
import atmosfears.AtmosFearsBE.database.SensorCode;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.service.AirParticulatesService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
            jsonObject.append("locations", locationInfo);
        }
        return jsonObject.toString();
    }

    @CrossOrigin()
    @GetMapping(value = "/data", produces = "application/json")
    public String data(
            @RequestParam("dateFrom") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam("dateTo") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam("pollutions") String[] pollutions,
            @RequestParam("sensors") String[] sensors
    ) {
        List<SensorCode> codeList = Arrays.stream(sensors).map(SensorCode::valueOf).toList();
        List<AirParticulates> airParticulates = airParticulatesService.findByDateBetweenAndSensorCodeIn(from, to, codeList);

        JSONObject json = new JSONObject();
        json.put("data", Collections.emptyList());
        for (AirParticulates particulates : airParticulates) {
            JSONObject particulate = new JSONObject();
            // TODO: Send only requested data
            particulate.put("code", particulates.getCode().toString());
            particulate.put("date", particulates.getDate());
            particulate.put("CO", particulates.getCO());
            particulate.put("NO2", particulates.getNO2());
            particulate.put("O3", particulates.getO3());
            particulate.put("PM10", particulates.getPM10());
            particulate.put("PM25", particulates.getPM25());
            particulate.put("SO2", particulates.getSO2());
            json.append("data", particulate);
        }

        Particulate particulate = Particulate.valueOf(pollutions[0]);
        JSONObject line = new JSONObject();
        line.put("dataKey", particulate.name());
        line.put("stroke", "#8884d8");
        line.put("yAxisId", "left-axis");
        json.append("lines", line);

        JSONObject leftAxis = new JSONObject();
        leftAxis.put("label", particulate.getDisplayName());
        json.put("leftAxis", leftAxis);

        if (pollutions.length > 1) {
            Particulate particulate2 = Particulate.valueOf(pollutions[1]);
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
