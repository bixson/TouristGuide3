import com.example.touristguide2.controllers.TouristController;
import com.example.touristguide2.controllers.TouristRestController;
import com.example.touristguide2.models.TouristAttraction;
import com.example.touristguide2.services.TouristService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
class TouristRestControllerTest {

    @Mock
    private TouristService touristService;  // Mock the service

    @InjectMocks
    private TouristRestController touristRestController;  // Inject into the controller

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(touristRestController).build();

        when(touristService.getAllTouristAttractions()).thenReturn(
                new ArrayList<>(List.of(
                        new TouristAttraction(1, "Eiffel Tower", 101, "Paris", "Iconic tower", List.of("Landmark")),
                        new TouristAttraction(2, "Colosseum", 202, "Rome", "Ancient arena", List.of("Historical"))
                ))
        );
    }

    @Test
    void getAllAttractions_ShouldReturnJsonList() throws Exception {
        mockMvc.perform(get("/api/attractions").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Eiffel Tower"));
    }
}