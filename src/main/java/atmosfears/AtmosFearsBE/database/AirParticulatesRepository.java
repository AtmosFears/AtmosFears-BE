package atmosfears.AtmosFearsBE.database;

import atmosfears.AtmosFearsBE.model.AirParticulates;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AirParticulatesRepository extends MongoRepository<AirParticulates, String> {
}
