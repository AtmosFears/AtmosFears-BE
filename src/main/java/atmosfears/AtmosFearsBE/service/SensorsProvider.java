package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.database.Sensor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SensorsProvider {
    private static final String sensorsFilePath = "data/sensors.json";
    private static final SensorsProvider instance = new SensorsProvider();
    private final List<Sensor> sensors = new LinkedList<>();
    private SensorsProvider() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(sensorsFilePath);
        assert inputStream != null;
        String JSONString;
        try {
            JSONString = new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject jsonObject = new JSONObject(JSONString);
        JSONArray jsonArray = jsonObject.getJSONArray("sensors");
        jsonArray.forEach(value -> {
            JSONObject sensor = (JSONObject) value;
            sensors.add(new Sensor(
                    sensor.getString("code"),
                    sensor.getString("address"),
                    sensor.getDouble("latitude"),
                    sensor.getDouble("longitude")));
        });
    }

    public static SensorsProvider getInstance(){
        return instance;
    }

    public List<Sensor> values(){
        return sensors;
    }

    public Sensor valueOf(String code){
        Optional<Sensor> optionalSensor = sensors.stream().filter(value -> Objects.equals(value.code, code)).findAny();
        return optionalSensor.orElse(null);
    }
}
