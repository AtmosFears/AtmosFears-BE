package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.model.AirParticulatesFile;
import atmosfears.AtmosFearsBE.service.AirParticulatesService;
import atmosfears.AtmosFearsBE.service.CalculationService;
import atmosfears.AtmosFearsBE.service.DataLoadService;
import java.util.Set;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/api/ingest")
public class DataIngestController {

  @Autowired
  DataLoadService dataLoadService;

  @Autowired
  CalculationService calculationService;

  @Autowired
  AirParticulatesService airParticulatesService;

  @PostMapping("/load")
  public ResponseEntity<String> loadAndPreprocessData() {
    Set<String> dataFiles = dataLoadService.collectAndSaveData();
    for(String file : dataFiles) {
      AirParticulatesFile airParticulatesFile = calculationService.getPredictionsForFile(file);
    }
    return ResponseEntity.ok("Data ingested!");
  }
}
