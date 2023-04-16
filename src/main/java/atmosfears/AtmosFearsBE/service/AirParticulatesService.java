package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.database.AirParticulatesRepository;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AirParticulatesService {

    @Autowired
    private final AirParticulatesRepository repository;

    public List<AirParticulates> findAll(){
        return repository.findAll();
    }
}
