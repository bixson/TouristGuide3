package com.example.touristguide2.controllers;

import com.example.touristguide2.models.TouristAttraction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.touristguide2.services.TouristService;

import java.util.List;

@RestController
@RequestMapping("/api/attractions")
public class TouristRestController {

    private final TouristService touristService;

    public TouristRestController(TouristService touristService) {
        this.touristService = touristService;
    }

    @GetMapping
    public List<TouristAttraction> getAllAttractions() {
        return touristService.getAllTouristAttractions();
    }

    @GetMapping("/{name}")
    public TouristAttraction getAttractionByName(@PathVariable String name) {
        return touristService.getTouristAttractionByName(name);
    }
}

/*
   This class is to return json response for API endpoints
   @index.html without fucking up the HTML views
  **/