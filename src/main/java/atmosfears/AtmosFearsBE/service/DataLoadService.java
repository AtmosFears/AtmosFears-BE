package atmosfears.AtmosFearsBE.service;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class DataLoadService {

  private final String SAMPLE_DATA_DIR =  "sampleData/";

  /**
   * Collect the data from all the available devices and save it to separate csv files.
   * For now it mocks the process of loading the data - it is always loaded from specified file.
   * @return List of files that were loaded inside
   */
    public Set<String> collectAndSaveData() {
      try {
        File source = new File(getClass().getClassLoader().getResource(SAMPLE_DATA_DIR).toURI());
        return Stream.of(source.listFiles())
            .filter(file -> !file.isDirectory())
            .map(File::getPath)
            .collect(Collectors.toSet());
      } catch (URISyntaxException e) {
        throw new RuntimeException();
      }
    }
}
