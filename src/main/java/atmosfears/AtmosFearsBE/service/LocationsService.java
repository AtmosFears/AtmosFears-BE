package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.model.AirParticulatesFile;
import atmosfears.AtmosFearsBE.model.Location;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LocationsService {
    private final String LOCATIONS_DIR = "locations/";
    private final String delimiter = ";";

    /**
     * Collect the data from all the available devices and save it to separate csv files.
     * For now it mocks the process of loading the data - it is always loaded from specified file.
     * @return List of files that were loaded inside
     */
    public List<Location> getLocations() {
        try {
            List<Location> locations = new LinkedList<>();
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
                        locations.add(new Location(values[1], Double.parseDouble(values[13]), Double.parseDouble(values[14])));
                    }
                } catch (IOException e ) {
                    throw new RuntimeException(e);
                }
            });
            return locations;
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }
    }


}
