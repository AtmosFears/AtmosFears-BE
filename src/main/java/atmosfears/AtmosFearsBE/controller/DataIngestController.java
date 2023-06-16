package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.model.AirParticulatesFile;
import atmosfears.AtmosFearsBE.service.CalculationService;
import atmosfears.AtmosFearsBE.service.DataLoadService;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/api/ingest")
public class DataIngestController {

  private final DataLoadService dataLoadService;

  private final CalculationService calculationService;

  @PostMapping("/load")
  public ResponseEntity<String> loadAndPreprocessData() {
    Set<String> dataFiles = dataLoadService.collectAndSaveData();
    for(String file : dataFiles) {
      AirParticulatesFile airParticulatesFile = calculationService.getPredictionsForFile(file);
    }
    return ResponseEntity.ok("Data ingested!");
  }
}
