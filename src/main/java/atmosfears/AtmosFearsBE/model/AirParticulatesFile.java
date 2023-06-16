package atmosfears.AtmosFearsBE.model;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirParticulatesFile {

  @Id
  String id;

  List<String> particulates;

  List<Double> predictions;
}
