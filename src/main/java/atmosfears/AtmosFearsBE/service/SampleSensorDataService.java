package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.model.Location;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SampleSensorDataService {

    private final String LOCATIONS_DIR = "sampleSensorData/";
    private final String delimiter = ",";

    public List<AirParticulates> getSampleData() {
        try {
            Set<AirParticulates> sampleData = new HashSet<>();
            File source = new File(getClass().getClassLoader().getResource(LOCATIONS_DIR).toURI());
            Set<String> paths = Stream.of(source.listFiles())
                    .filter(file -> !file.isDirectory())
                    .map(File::getPath)
                    .collect(Collectors.toSet());

            paths.forEach((String filePath) -> {
                File dataFile = new File(filePath);
                try (FileReader fr = new FileReader(dataFile);
                     BufferedReader br = new BufferedReader(fr)) {
                    String line = "";
                    while((line = br.readLine()) != null) {
                        String[] values = line.split(delimiter);
                        if(sampleData.stream().anyMatch(val -> val.getDeviceLocation().equals(values[0]))) continue;
                        sampleData.add(new AirParticulates(values[0], Double.parseDouble(values[5])));
                    }
                } catch (IOException e ) {
                    throw new RuntimeException(e);
                }
            });
            return sampleData.stream().toList();
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }
    }

}
