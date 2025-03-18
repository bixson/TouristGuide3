import com.example.touristguide2.models.TouristAttraction;
import com.example.touristguide2.repositories.TouristRepository;
import com.example.touristguide2.services.TouristService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TouristServiceTest {

    @Mock
    private TouristRepository touristRepository;

    @InjectMocks
    private TouristService touristService;

    private TouristAttraction attraction;

    @BeforeEach
    void setUp() {
        attraction = new TouristAttraction(1, "Eiffel Tower", 101, "Paris", "Iconic tower", Arrays.asList("Landmark", "Historical"));
    }

    @Test
    void getAllTouristAttractions_ShouldReturnAllAttractions() {
        List<TouristAttraction> attractions = Arrays.asList(attraction);
        when(touristRepository.getAllTouristAttractions()).thenReturn(attractions);

        List<TouristAttraction> result = touristService.getAllTouristAttractions();

        assertEquals(1, result.size());
        assertEquals("Eiffel Tower", result.get(0).getName());
    }

    @Test
    void getTouristAttraction_ShouldReturnAttractionIfExists() {
        when(touristRepository.findByName("Eiffel Tower")).thenReturn(Optional.of(attraction));

        TouristAttraction result = touristService.getTouristAttraction("Eiffel Tower");

        assertNotNull(result);
        assertEquals("Eiffel Tower", result.getName());
    }

    @Test
    void getTouristAttraction_ShouldReturnNullIfNotFound() {
        when(touristRepository.findByName("Unknown Place")).thenReturn(Optional.empty());

        TouristAttraction result = touristService.getTouristAttraction("Unknown Place");

        assertNull(result);
    }
}