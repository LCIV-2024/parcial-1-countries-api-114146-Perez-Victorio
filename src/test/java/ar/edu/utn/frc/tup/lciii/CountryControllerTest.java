package ar.edu.utn.frc.tup.lciii;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ar.edu.utn.frc.tup.lciii.controllers.CountryController;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebMvcTest(CountryController.class)
public class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CountryService countryService;

    @InjectMocks
    private CountryController countryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCountries() throws Exception {
        CountryDTO country1 = new CountryDTO("Argentina", "ARG");
        CountryDTO country2 = new CountryDTO("Brazil", "BRA");
        List<CountryDTO> countries = Arrays.asList(country1, country2);

        when(countryService.getAllCountries(null, null)).thenReturn(countries);

        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Argentina"))
                .andExpect(jsonPath("$[1].name").value("Brazil"));

        verify(countryService, times(1)).getAllCountries(null, null);
    }

    @Test
    void testGetCountriesByContinent() throws Exception {
        CountryDTO country = new CountryDTO("Argentina", "ARG");
        List<CountryDTO> countries = Collections.singletonList(country);

        when(countryService.getCountriesByContinent("South America")).thenReturn(countries);

        mockMvc.perform(get("/api/countries/South America/continent"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Argentina"));

        verify(countryService, times(1)).getCountriesByContinent("South America");
    }

    @Test
    void testGetCountriesByLanguage() throws Exception {
        CountryDTO country = new CountryDTO("Argentina", "ARG");
        List<CountryDTO> countries = Collections.singletonList(country);

        when(countryService.getCountriesByLanguage("Spanish")).thenReturn(countries);

        mockMvc.perform(get("/api/countries/Spanish/language"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Argentina"));

        verify(countryService, times(1)).getCountriesByLanguage("Spanish");
    }

    @Test
    void testGetCountryWithMostBorders() throws Exception {
        CountryDTO country = new CountryDTO("Argentina", "ARG");

        when(countryService.getCountryWithMostBorders()).thenReturn(country);

        mockMvc.perform(get("/api/countries/most-borders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Argentina"));

        verify(countryService, times(1)).getCountryWithMostBorders();
    }

    @Test
    void testSaveCountries() throws Exception {
        Map<String, Integer> request = new HashMap<>();
        request.put("amountOfCountryToSave", 5);
        CountryDTO country = new CountryDTO("Argentina", "ARG");
        List<CountryDTO> savedCountries = Collections.singletonList(country);

        when(countryService.saveCountries(5)).thenReturn(savedCountries);

        mockMvc.perform(post("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amountOfCountryToSave\": 5}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Argentina"));

        verify(countryService, times(1)).saveCountries(5);
    }

}