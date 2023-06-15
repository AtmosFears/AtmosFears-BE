package atmosfears.AtmosFearsBE.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

@Document(collection = "air_particulates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedParticulates {
    @Id
    private String id;
    private Date date;
    private ArrayList<AirParticulates> values;
}
