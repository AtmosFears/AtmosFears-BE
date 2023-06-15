package atmosfears.AtmosFearsBE.database;

import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.model.AggregatedParticulates;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface AirParticulatesRepository extends MongoRepository<AirParticulates, String> {
    @Query("{'date': {'$gte': ?0, '$lte':?1 }}")
    List<AirParticulates> findByDateBetween(Date from, Date to);

    @Aggregation(
            pipeline = {
                    "{ $match: { code: {'$in': ?2}, date: { $gte: ?0, $lte: ?1 } } }",
                    "{ $group: { _id: { date: { $dateToString: { format: '%Y-%m-%d', date: '$date' } }, code: '$code' }, CO: { $avg: '$CO' }, NO2: { $avg: '$NO2' }, PM10: { $avg: '$PM10' }, PM25: { $avg: '$PM25' }, WindSpeed: { $avg: '$windSpeed' }, WindDirection: { $avg: '$windDirection' }, O3: { $avg: '$O3' }, SO2: { $avg: '$SO2' } } }",
                    "{ $project: { _id: 0, date: {$toDate: '$_id.date'}, code: '$_id.code', CO: 1, NO2: 1, PM10: 1, PM25: 1, WindSpeed: 1, WindDirection: 1, O3: 1, SO2: 1 } }",
                    "{ $group: { _id: '$date', values: { $push: '$$ROOT' } } }",
                    "{ $project: { _id: 0, date: {$toDate: '$_id'}, values: 1 } }",
                    "{ $sort: { date: 1 } }",
            }
    )
    List<AggregatedParticulates> aggregateByDay(Date from, Date to, List<SensorCode> sensorCode);


}
