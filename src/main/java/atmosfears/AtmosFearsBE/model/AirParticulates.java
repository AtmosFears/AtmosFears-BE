package atmosfears.AtmosFearsBE.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "air_particulates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirParticulates {
    @Id
    private String id;

    private String deviceLocation;
    private double pm10Quantity;

    //TODO check what data is needed and fix it. It is only an example. Try to do automatic ID creation
}
