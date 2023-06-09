package atmosfears.AtmosFearsBE.database;

import lombok.Getter;

public class Sensor {

    @Getter
    public final String code;
    @Getter
    public final String address;
    @Getter
    public final double latitude;
    @Getter
    public final double longitude;


    public Sensor(String code, String address, double latitude, double longitude) {
        this.code = code;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
