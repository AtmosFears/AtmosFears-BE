package atmosfears.AtmosFearsBE.database;

import atmosfears.AtmosFearsBE.model.AirParticulates;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface AirParticulatesRepository extends MongoRepository<AirParticulates, String> {
    @Query("{'date': {'$gte': ?0, '$lte':?1 }}")
    List<AirParticulates> findByDateBetween(Date from, Date to);

    @Query("{'date': {'$gte': ?0, '$lte': ?1 }, 'code': {'$in': ?2}}")
    List<AirParticulates> findByDateBetweenAndSensorCodeIn(Date from, Date to, List<SensorCode> sensorCodes);



}
