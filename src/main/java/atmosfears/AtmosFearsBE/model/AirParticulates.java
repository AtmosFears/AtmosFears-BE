package atmosfears.AtmosFearsBE.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "air_particulates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirParticulates {
    @Id
    private String id;
    private String code;
    private Date date;
    private Double CO;
    private Double NO2;
    private Double PM10;
    private Double PM25;
    private Double O3;
    private Double SO2;
    private Integer windDirection;
    private Double windSpeed;
}
