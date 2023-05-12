package atmosfears.AtmosFearsBE.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    private String id;

    private String code;
    private Double N;
    private Double E;

    public Location(String code, Double n, Double e) {
        this.code = code;
        N = n;
        E = e;
    }
}
