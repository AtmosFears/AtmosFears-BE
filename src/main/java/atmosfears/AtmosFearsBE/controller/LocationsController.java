package atmosfears.AtmosFearsBE.controller;

import atmosfears.AtmosFearsBE.model.Location;
import atmosfears.AtmosFearsBE.service.LocationsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocationsController {
    private final LocationsService locationsService;

    public LocationsController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }


    @GetMapping("/locations")
    public List<Location> locations(){
        return this.locationsService.getLocations();
    }
}
