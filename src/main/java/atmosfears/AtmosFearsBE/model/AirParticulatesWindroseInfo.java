package atmosfears.AtmosFearsBE.model;


import atmosfears.AtmosFearsBE.database.Particulate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirParticulatesWindroseInfo {

  private Direction direction;

  private Particulate particulate;

  private int section;
}
