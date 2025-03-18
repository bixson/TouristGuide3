package com.example.touristguide2.models;

import java.util.List;


public class TouristAttraction {
    private int id;
    private String name;
    private int locationId;
    private String city;
    private String description;
    private List<String> tags;

    public TouristAttraction(int id, String name, int locationId, String city, String description, List<String> tags) {
        this.id = id;
        this.name = name;
        this.locationId = locationId;
        this.city = city;
        this.description = description;
        this.tags = tags;
    }

    public TouristAttraction() {}

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}