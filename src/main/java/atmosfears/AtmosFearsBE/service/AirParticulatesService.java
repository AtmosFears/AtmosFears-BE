package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.database.AirParticulatesFileRepository;
import atmosfears.AtmosFearsBE.database.AirParticulatesRepository;
import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.model.AirParticulatesFile;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AirParticulatesService {

    private final AirParticulatesRepository airParticulatesRepository;

    private final AirParticulatesFileRepository airParticulatesFileRepository;

    public List<AirParticulates> findAll(){
        return airParticulatesRepository.findAll();
    }

    public void insertEmpty(){
        airParticulatesRepository.insert(new AirParticulates());
    }

    public void saveFile(AirParticulatesFile airParticulatesFile) {
        airParticulatesFileRepository.save(airParticulatesFile);
    }
}
