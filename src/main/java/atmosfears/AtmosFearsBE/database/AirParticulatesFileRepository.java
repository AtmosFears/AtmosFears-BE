package atmosfears.AtmosFearsBE.database;

import atmosfears.AtmosFearsBE.model.AirParticulatesFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AirParticulatesFileRepository extends MongoRepository<AirParticulatesFile, String> {
}
