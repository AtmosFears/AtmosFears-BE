package atmosfears.AtmosFearsBE.database;

import lombok.Getter;

public enum Particulate {

    // TODO: Add correct units for each particulate
    CO("mg/m3"),
    NO2("µg/m3"),
    O3("µg/m3"),
    PM10("µg/m3"),
    PM25("µg/m3"),
    SO2("µg/m3");

    @Getter
    public final String unit;

    Particulate(String unit) {
        this.unit = unit;
    }

    public String getDisplayName() {
        return this.name() + " [" + this.unit + "]";
    }
}
