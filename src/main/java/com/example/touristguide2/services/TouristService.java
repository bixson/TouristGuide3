package com.example.touristguide2.services;

import com.example.touristguide2.models.TouristAttraction;
import org.springframework.stereotype.Service;
import com.example.touristguide2.repositories.TouristRepository;

import java.util.List;

@Service
public class TouristService {
    private final TouristRepository touristRepository;

    public TouristService(TouristRepository touristRepository) {
        this.touristRepository = touristRepository;
    }

    public List<TouristAttraction> getAllTouristAttractions() {
        List<TouristAttraction> attractions = touristRepository.getAllTouristAttractions();
        for (TouristAttraction attraction : attractions) {
            List<String> tags = touristRepository.getTagsForAttraction(attraction.getId());
            attraction.setTags(tags);
        }
        return attractions;
    }

    public TouristAttraction getTouristAttraction(String name) {
        TouristAttraction attraction = touristRepository.findByName(name).orElse(null);
        if (attraction != null) {
            attraction.setTags(touristRepository.getTagsForAttraction(attraction.getId()));
        }
        return attraction;
    }
    /// singular API endpoint links @index.html
    public TouristAttraction getTouristAttractionByName(String name) {
        return touristRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Attraction not found: " + name));
    }


    public void addTouristAttraction(TouristAttraction touristAttraction) {
        touristRepository.save(touristAttraction);
    }

    public void updateTouristAttraction(String name, TouristAttraction touristAttraction) {
        TouristAttraction existingAttraction = touristRepository.findByName(name).orElse(null);
        if (existingAttraction != null) {
            existingAttraction.setName(touristAttraction.getName());
            existingAttraction.setDescription(touristAttraction.getDescription());
            existingAttraction.setCity(touristAttraction.getCity());
            existingAttraction.setTags(touristAttraction.getTags());
            touristRepository.save(existingAttraction);
        }
    }

    public void deleteTouristAttraction(String name) {
        touristRepository.deleteByName(name);
    }

    public List<String> getAllCities() {
        return touristRepository.getAllCities();
    }

    public List<String> getAllTags() {
        return touristRepository.getAllTags();
    }

    public void saveAttraction(TouristAttraction touristAttraction) {
        if (touristAttraction == null || touristAttraction.getName() == null || touristAttraction.getName().isEmpty() ||
                touristAttraction.getCity() == null || touristAttraction.getCity().isEmpty() ||
                touristAttraction.getDescription() == null || touristAttraction.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Tourist attraction details cannot be null or empty");
        }
        touristRepository.save(touristAttraction);
    }
}