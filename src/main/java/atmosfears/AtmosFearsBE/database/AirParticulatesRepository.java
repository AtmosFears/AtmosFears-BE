package atmosfears.AtmosFearsBE.database;

import atmosfears.AtmosFearsBE.model.AirParticulates;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AirParticulatesRepository extends MongoRepository<AirParticulates, String> {
    List<AirParticulates> getAirParticulatesByDatetimeBetween(LocalDateTime start, LocalDateTime finish);
}
