package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.model.AirParticulates;
import atmosfears.AtmosFearsBE.model.Location;
import atmosfears.AtmosFearsBE.service.LocationsService;
import atmosfears.AtmosFearsBE.service.SampleSensorDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocationsController {
    private final LocationsService locationsService;
    private final SampleSensorDataService sampleSensorDataService;

    public LocationsController(LocationsService locationsService, SampleSensorDataService sampleSensorDataService) {
        this.locationsService = locationsService;
        this.sampleSensorDataService = sampleSensorDataService;
    }


    @GetMapping("/locations")
    public List<Location> locations(){
        return this.locationsService.getLocations();
    }

    @GetMapping("/sampleData")
    public List<AirParticulates> sampleData(){
        return this.sampleSensorDataService.getSampleData();
    }
}
