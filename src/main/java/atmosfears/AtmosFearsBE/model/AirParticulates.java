package atmosfears.AtmosFearsBE.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "air_particulates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirParticulates {
    @Id
    private String id;

    private String deviceLocation;
    private double pm10Quantity;

    private double windSpeed;
    private int windDirection;

    private LocalDateTime datetime;

    //TODO check what data is needed and fix it. It is only an example. Try to do automatic ID creation


    public String getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(String deviceLocation) {
        this.deviceLocation = deviceLocation;
    }

    public double getPm10Quantity() {
        return pm10Quantity;
    }

    public void setPm10Quantity(double pm10Quantity) {
        this.pm10Quantity = pm10Quantity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
}
