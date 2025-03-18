package com.example.touristguide2.repositories;

import com.example.touristguide2.models.TouristAttraction;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TouristRepository {
    private final JdbcTemplate jdbcTemplate;

    public TouristRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Fetch all tourist attractions with descriptions
    public List<TouristAttraction> getAllTouristAttractions() {
        String sql = "SELECT ta.attraction_id, ta.attraction_name, ta.location_id, l.city_name, d.description_text " +
                "FROM tourist_attraction ta " +
                "LEFT JOIN location l ON ta.location_id = l.location_id " +
                "LEFT JOIN description d ON ta.attraction_id = d.attraction_id";

        return jdbcTemplate.query(sql, attractionRowMapper);
    }


    // Fetch tags for a specific attraction
    public List<String> getTagsForAttraction(int attractionId) {
        String sql = "SELECT t.tag_text " +
                "FROM tourist_attraction_tags tat " +
                "JOIN tags t ON tat.tag_id = t.tag_id " +
                "WHERE tat.attraction_id = ?";

        return jdbcTemplate.queryForList(sql, String.class, attractionId);
    }

    // Find a tourist attraction by name
    public Optional<TouristAttraction> findByName(String name) {
        String sql = "SELECT ta.attraction_id, ta.attraction_name, ta.location_id, l.city_name, d.description_text " +
                "FROM tourist_attraction ta " +
                "LEFT JOIN location l ON ta.location_id = l.location_id " +
                "LEFT JOIN description d ON ta.attraction_id = d.attraction_id " +
                "WHERE ta.attraction_name = ?";
        List<TouristAttraction> attractions = jdbcTemplate.query(sql, attractionRowMapper, name);
        return attractions.isEmpty() ? Optional.empty() : Optional.of(attractions.get(0));
    }

    // save tourist attraction
    public void save(TouristAttraction touristAttraction) {
        // Get location_id from the database
        String locationQuery = "SELECT location_id FROM location WHERE city_name = ?";
        Integer locationId = null;
        try {
            locationId = jdbcTemplate.queryForObject(locationQuery, Integer.class, touristAttraction.getCity());
        } catch (EmptyResultDataAccessException e) {
            // Location not found, inserting a new one
            String insertLocation = "INSERT INTO location (city_name) VALUES (?)";
            jdbcTemplate.update(insertLocation, touristAttraction.getCity());
            locationId = jdbcTemplate.queryForObject(locationQuery, Integer.class, touristAttraction.getCity());
        }

        // Insert attraction
        String sql = "INSERT INTO tourist_attraction (attraction_name, location_id) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE attraction_name = VALUES(attraction_name), location_id = VALUES(location_id)";
        jdbcTemplate.update(sql, touristAttraction.getName(), locationId);

        // Insert tags
        String attractionIdQuery = "SELECT attraction_id FROM tourist_attraction WHERE attraction_name = ?";
        Integer attractionId = jdbcTemplate.queryForObject(attractionIdQuery, Integer.class, touristAttraction.getName());

        if (attractionId != null && touristAttraction.getTags() != null) {
            for (String tag : touristAttraction.getTags()) {
                String tagIdQuery = "SELECT tag_id FROM tags WHERE tag_text = ?";
                Integer tagId = null;
                try {
                    tagId = jdbcTemplate.queryForObject(tagIdQuery, Integer.class, tag);
                } catch (EmptyResultDataAccessException e) {
                    // Tag not found, ignore or insert a new one
                }
                if (tagId != null) {
                    String insertTag = "INSERT INTO tourist_attraction_tags (attraction_id, tag_id) VALUES (?, ?) " +
                            "ON DUPLICATE KEY UPDATE attraction_id = VALUES(attraction_id), tag_id = VALUES(tag_id)";
                    jdbcTemplate.update(insertTag, attractionId, tagId);
                }
            }
        }
    }



    public void deleteByName(String name) {
        String attractionIdQuery = "SELECT attraction_id FROM tourist_attraction WHERE attraction_name = ?";
        Integer attractionId = null;

        try {
            attractionId = jdbcTemplate.queryForObject(attractionIdQuery, Integer.class, name);
        } catch (EmptyResultDataAccessException e) {
            // If the attraction doesn't exist, do nothing
            return;
        }

        if (attractionId != null) {
            // First, delete related entries from the tourist_attraction_tags table
            String deleteTags = "DELETE FROM tourist_attraction_tags WHERE attraction_id = ?";
            jdbcTemplate.update(deleteTags, attractionId);

            // Then, delete the attraction itself
            String deleteAttraction = "DELETE FROM tourist_attraction WHERE attraction_id = ?";
            jdbcTemplate.update(deleteAttraction, attractionId);
        }
    }

    // Get all cities
    public List<String> getAllCities() {
        String sql = "SELECT DISTINCT city_name FROM location";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    // Get all tags
    public List<String> getAllTags() {
        String sql = "SELECT DISTINCT tag_text FROM tags";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    // RowMapper for tourist attractions
    private final RowMapper<TouristAttraction> attractionRowMapper = (rs, rowNum) -> {
        return new TouristAttraction(
                rs.getInt("attraction_id"),
                rs.getString("attraction_name"),
                rs.getInt("location_id"),
                rs.getString("city_name"),
                rs.getString("description_text"),
                List.of()
        );
    };

}