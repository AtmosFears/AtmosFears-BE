package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.database.AirParticulatesRepository;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
