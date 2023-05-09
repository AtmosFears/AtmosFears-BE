package atmosfears.AtmosFearsBE.service;

import atmosfears.AtmosFearsBE.model.AirParticulatesFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

  /**
   * Get predictions for given data (currently a mock)
   * @param filePath Path to csv file
   * @return Map from data row to prediction (in future it will be object holding the data, not string)
   */
  public AirParticulatesFile getPredictionsForFile(String filePath) {
    File dataFile = new File(filePath);
    try (FileReader fr = new FileReader(dataFile);
        BufferedReader br = new BufferedReader(fr)) {
      String line = "";
      List<String> particulates = new ArrayList<>();
      List<Double> predictions = new ArrayList<>();
      while((line = br.readLine()) != null) {
        particulates.add(line);
        predictions.add(new Random().nextDouble());
      }
      return AirParticulatesFile.builder()
          .predictions(predictions)
          .particulates(particulates)
          .id(UUID.randomUUID().toString())
          .build();
    } catch (IOException e ) {
      throw new RuntimeException(e);
  }
  }
}
