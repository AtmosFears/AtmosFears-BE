package atmosfears.AtmosFearsBE.database;

import lombok.Getter;

public record Sensor(
        @Getter String code,
        @Getter String address,
        @Getter double latitude,
        @Getter double longitude
) {
}
