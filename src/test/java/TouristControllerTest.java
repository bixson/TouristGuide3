import com.example.touristguide2.controllers.TouristController;
import com.example.touristguide2.models.TouristAttraction;
import com.example.touristguide2.services.TouristService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TouristControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TouristService touristService;

    @InjectMocks
    private TouristController touristController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(touristController).build();

        when(touristService.getAllTouristAttractions()).thenReturn(
                new ArrayList<>(List.of(
                        new TouristAttraction(1, "Eiffel Tower", 101, "Paris", "Iconic tower", List.of("Landmark")),
                        new TouristAttraction(2, "Colosseum", 202, "Rome", "Ancient arena", List.of("Historical"))
                ))
        );
    }

    @Test
    void showAllTouristAttractions_ShouldReturnAttractionListView() throws Exception {
        mockMvc.perform(get("/attractions"))
                .andExpect(status().isOk())
                .andExpect(view().name("attraction-list"))
                .andExpect(model().attributeExists("attractions"));
    }
}
