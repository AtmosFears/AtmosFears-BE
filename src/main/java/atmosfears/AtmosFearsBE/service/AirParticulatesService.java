package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.database.AirParticulatesRepository;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AirParticulatesService {

    private final AirParticulatesRepository repository;

    public List<AirParticulates> findAll(){
        return repository.findAll();
    }

    public void insertEmpty(){
        repository.insert(new AirParticulates());
    }

    public void insertDummyData(){
        AirParticulates airParticulates1 = new AirParticulates();
        AirParticulates airParticulates2 = new AirParticulates();
        airParticulates1.setPm10Quantity(10.1);
        airParticulates2.setPm10Quantity(20.2);
        airParticulates1.setWindSpeed(1.1);
        airParticulates1.setWindDirection(45);
        airParticulates2.setWindSpeed(2.2);
        airParticulates2.setWindDirection(180);
        airParticulates1.setDatetime(LocalDateTime.of(2022,10,11,15,00));
        airParticulates2.setDatetime(LocalDateTime.of(2022,12, 12, 14,00));
        repository.insert(airParticulates1);
        repository.insert(airParticulates2);
    }

    public List<AirParticulates> findAllBetweenDates(LocalDateTime start, LocalDateTime finish){
        return repository.getAirParticulatesByDatetimeBetween(start, finish);
    }
}
